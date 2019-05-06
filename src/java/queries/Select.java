package queries;

import java.sql.*;

public class Select {
    public static void main(String[] args) {
        Connection conn = ddl.Connection.createNewConnection();

        runSelect(conn);
    }

    private static void runSelect(Connection conn){
        Statement stmt = null;
        ResultSet rs = null;

        try {
            System.out.println(conn.getTransactionIsolation());
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT NAME FROM USER");

            // or alternatively, if you don't know ahead of time that
            // the query will be a SELECT...

            if (stmt.execute("SELECT name FROM user")) {
                rs = stmt.getResultSet();
            }
            while(rs.next()) {
                System.out.println(rs.getString("name"));
            }
            // Now do something with the ResultSet ....
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } finally {
            // it is a good idea to release
            // resources in a finally{} block
            // in reverse-order of their creation
            // if they are no-longer needed

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                } // ignore

                rs = null;
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                } // ignore

                stmt = null;
            }
        }

    }
}