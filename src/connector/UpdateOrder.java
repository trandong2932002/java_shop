package connector;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class UpdateOrder {
    private static Connection con = null;
    private static Statement stmt = null;

    public static void update(int customer_id, BigDecimal amount, int reward_point) {
        try {
            String query = generateUpdateOrder(customer_id, amount, reward_point);
            con = ConnectionToDB.ConnectToDB();
            stmt = con.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private static String generateUpdateOrder(int customer_id, BigDecimal amount, int reward_point) {
        String[] q = { "INSERT INTO orders(customer_id, amount, reward_points_used) VALUES (", /* customer_id */ ", ",
                /* amount */", ", /* reward_points_used*/ ");" };

        String query = q[0] + customer_id + q[1] + amount.toString() + q[2] + reward_point + q[3];
        return query;
    }
}
