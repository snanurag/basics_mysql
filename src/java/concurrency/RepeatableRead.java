package concurrency;

/**
 * It takes almost 50k updates. Because every thread apparently read at same time so all reads same value. And in effect the value is
 * finally decreased by only 1 by all the 5 threads.
 *
 * This is default isolation level which is repeatable read. In this, complete transaction is not locked. Only Updates/writes are locked
 * between two transactions that too only on phantom writes.
 *
 */

import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class RepeatableRead {

    static volatile int updates;
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

            do{
                Statement stm = conn.createStatement();
                stm.execute("select quantity from transaction");
                ResultSet rs = stm.getResultSet();
                rs.next();
                quantity = rs.getInt(1);
                System.out.println(quantity);
                quantity--;
                if(quantity > 0){
                    PreparedStatement preparedStatement = conn.prepareStatement("update transaction set quantity = ? where name = ?");
                    preparedStatement.setInt(1, quantity);
                    preparedStatement.setString(2, "redmi 6A");

                    preparedStatement.executeUpdate();
                    updates++;

                }
                else
                    System.out.println(updates);

                conn.commit();

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
