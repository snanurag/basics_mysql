package concurrency;

/**
 * Since it is a repeatable_read transaction, statement reads from the snapshot, it creates first time.
 * It always read the same value every time it reads.
 * That's how value never goes below single value during updates.
 */

import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class NaiveTransaction {

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
        System.out.println(updates);
    }

    private static void transaction(Connection conn){
        try{

            conn.setAutoCommit(false);
            Statement stm = conn.createStatement();
            stm.execute("select quantity from transaction");
            ResultSet rs = stm.getResultSet();
            rs.next();
            int quantity = 0;

            do{
                quantity = rs.getInt(1);
                quantity--;
                if(quantity > 0){
                    PreparedStatement preparedStatement = conn.prepareStatement("update transaction set quantity = ? where name = ?");
                    preparedStatement.setInt(1, quantity);
                    preparedStatement.setString(2, "redmi 6A");

                    preparedStatement.executeUpdate();
                    updates++;
                }
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
