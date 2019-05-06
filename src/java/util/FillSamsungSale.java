package util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 */
public class FillSamsungSale {
    static volatile int updates;

    public static void main(String[] args) {
        Connection conn = ddl.Connection.createNewConnection();
        transaction(conn);
//        System.out.println(updates);
    }

    private static void transaction(Connection conn) {
        try {

            conn.setAutoCommit(false);
            Statement stm = conn.createStatement();

            for(int i=0; i<100000; i++)
                stm.execute("insert into samsung_sale values()");

            conn.commit();

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
