package concurrency;

/**
 * By default it is repeatable read transaction. It acquires row level locks (IX -> Intention exlusive locks). That's it works best.
 * No. of connections are directly effecting the delete operation.
 *
 * For 3 connections it took 26 seconds to delete 100K records
 * For 10 connections it took 13 seconds to delete 100k records
 * For 20 connections it took 15 seconds to delete 100k records.
 *
 * Seem more like a connection pooling issue.
 *
 */

import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class NaiveRowDeletes {

    static volatile int rowID;
    static long time = 0 ;
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        Runnable r = () -> {
            Connection conn = ddl.Connection.createNewConnection();
            transaction(conn);

        };
        rowID = 0;
        time = System.currentTimeMillis();
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

    private static void transaction(Connection conn){
        try{

            Statement stm = conn.createStatement();
            while(rowID < 100001)
                stm.execute("delete from samsung_sale where id = "+ ++rowID);

            System.out.println(System.currentTimeMillis() - time);

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
