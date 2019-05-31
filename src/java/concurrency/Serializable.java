package concurrency;

/**
 * Try this code with single entry of products in transaction table.
 *
 * 1 "redmi 6A" 100000
 * 2 "samsung m10" 100000
 * .....
 * .....
 *
 * This is giving deadlock. It is simple.
 * First thread takes S (Shared) lock then another thread requires X lock on same row.
 * Then first thread tries to acquire X lock on the same row without releasing S lock on same row.
 * That's how deadlock error comes. First thread gives the deadlock exception. That's how all other threads except first end up in deadlock.
 *
 * Repeatable read or read committed never exhibit this behavior because they don't take S lock.
 * They only acquire X lock that too on update or write. So one transaction can hold only one type of lock i.e. X
 * and request again for same X lock only which it already holds.
 *
 */

import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Serializable {

    static volatile int updates;
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Runnable r = () -> {
            Connection conn = ddl.Connection.createNewConnection();
            try{
                conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            }
            catch (SQLException e){
                e.printStackTrace();
            }
            transaction(conn);

        };

        executorService.execute(r);
        executorService.execute(r);
        executorService.execute(r);
        executorService.execute(r);
        executorService.execute(r);
        executorService.shutdown();

    }

    private static void transaction(Connection conn){
        try{

            System.out.println("Thread started : "+Thread.currentThread().getName());
            int quantity = 0;
            conn.setAutoCommit(false);

            do{
                Statement stm = conn.createStatement();
                stm.execute("select quantity from transaction");
                System.out.println(Thread.currentThread().getName() + " acquired read lock.");

                ResultSet rs = stm.getResultSet();
                rs.next();
                quantity = rs.getInt(1);
//                System.out.println(quantity);
                quantity--;
                if(quantity > 0){
                    System.out.println(Thread.currentThread().getName() + " acquiring write lock.");
                    PreparedStatement preparedStatement = conn.prepareStatement("update transaction set quantity = ? where name = ?");
                    preparedStatement.setInt(1, quantity);
                    preparedStatement.setString(2, "redmi 6A");

                    preparedStatement.executeUpdate();
                    System.out.println(Thread.currentThread().getName() + " acquired write lock.");
                    updates++;

                }
                else
                    System.out.println(updates);

                conn.commit();

            }
            while(quantity > 0);

        }
        catch (SQLException e){

            System.out.println("Thread gave exception : "+Thread.currentThread().getName()+ " "+e.getMessage());

            try{
                conn.rollback();
            }
            catch (SQLException er){
                er.printStackTrace();
            }
        }
    }
}
