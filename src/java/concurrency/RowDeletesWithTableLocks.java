package concurrency;

/**
 *
 * With 10 threads and table locks it takes around 60 seconds for 100k rows.
 *
 * With 10 threads and no table locks it takes around 13 seconds for 100k rows. Because by default repeatable_read type of transaction aquires row
 * level locks (IX(Table) -> X(Row)).
 *
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class RowDeletesWithTableLocks {

    static volatile int rowID = 0;
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
        executorService.execute(r);
        executorService.execute(r);
        executorService.execute(r);
        executorService.execute(r);
        executorService.execute(r);
        executorService.shutdown();
    }

    private static void transaction(Connection conn) {
        try {
            conn.setAutoCommit(false);

            while (rowID < 100001) {
                Statement stm = conn.createStatement();
                stm.execute("Lock tables samsung_sale write");
                stm.execute("delete from samsung_sale where id = " + ++rowID);

            }
            System.out.println(Thread.currentThread().getName() + " time taken : " + (System.currentTimeMillis() - time));
            conn.commit();

            PreparedStatement p2 = conn.prepareStatement("Unlock tables");
            p2.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException er) {
                er.printStackTrace();
            }
        }
    }
}
