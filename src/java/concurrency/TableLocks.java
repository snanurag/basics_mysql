package concurrency;

/**
 *
 * Try this code with single entry of products in transaction table.
 *
 * 1 "redmi 6A" 100000
 * 2 "samsung m10" 100000
 * .....
 * .....
 *
 * Lock tables <table_name> write; makes a transaction an atomic transaction.
 * That's why these concurrent operations in multiple threads are possible.
 * But the problem is that these atomic transactions are locking complete table.
 * At a time only one transaction could be performed.
 *
 * To make 10k updates, it takes around 13 seconds.
 * To make 100k updates, it takes around 137 seconds.
 *
 */

import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TableLocks {

//    static volatile int updates;
    static long time = System.currentTimeMillis();
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Runnable r = () -> {
            Connection conn = ddl.Connection.createNewConnection();
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

            int quantity = 0;
            conn.setAutoCommit(false);

            int count = 0;
            do{
                Statement stm = conn.createStatement();
                stm.execute("Lock tables transaction write");
                stm.execute("select quantity from transaction");
                ResultSet rs = stm.getResultSet();
                rs.next();
                quantity = rs.getInt(1);
                quantity--;
                if(quantity > 0){
                    PreparedStatement preparedStatement = conn.prepareStatement("update transaction set quantity = ? where name = ?");
                    preparedStatement.setInt(1, quantity);
                    preparedStatement.setString(2, "redmi 6A");

                    preparedStatement.executeUpdate();
                    count++;

                }
                else{
                    System.out.println(Thread.currentThread().getName() +" count : "+count);
                    System.out.println(Thread.currentThread().getName() +" time taken : "+(System.currentTimeMillis() - time));
                }

                conn.commit();
                PreparedStatement p2 = conn.prepareStatement("Unlock tables");
                p2.execute();
            }
            while(quantity > 0);

        }
        catch (SQLException e){
            e.printStackTrace();
            try{
                conn.rollback();
            }
            catch (SQLException er){
                er.printStackTrace();
            }
        }
    }
}
