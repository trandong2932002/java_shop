package connector;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class UpdateOrder {
    private static Connection con = null;
    private static Statement stmt = null;

    public static void update(int customer_id, BigDecimal amount) {
        try {
            String query = generateUpdateOrder(customer_id, amount);
            con = ConnectionToDB.ConnectToDB();
            stmt = con.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static String generateUpdateOrder(int customer_id, BigDecimal amount) {
        String[] q = { "INSERT INTO orders(customer_id, amount) VALUES (", /* customer_id */ ", ",
                /* amount */");" };

        String query = q[0] + customer_id + q[1] + amount.toString() + q[2];
        return query;
    }
}
