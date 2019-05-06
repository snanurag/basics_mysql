package concurrency;

/**
 *
 * Transaction level None is not supported by mysql.
 */

import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TransactionNone {

    static volatile int updates;
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Runnable r = () -> {
            Connection conn = ddl.Connection.createNewConnection();
            try{
                conn.setTransactionIsolation(Connection.TRANSACTION_NONE);
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

            int quantity = 0;
            conn.setAutoCommit(false);

            do{
                Statement stm = conn.createStatement();
                stm.execute("select quantity from transaction");
                ResultSet rs = stm.getResultSet();
                rs.next();
                quantity = rs.getInt(1);
//                System.out.println(quantity);
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
