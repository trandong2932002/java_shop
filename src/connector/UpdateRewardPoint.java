package connector;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UpdateRewardPoint {
    private static Connection con = null;
    private static Statement stmt = null;

    public static void update(int customer_id, BigDecimal amount) {
        try {
            List<String> queries = generateUpdateRewardPoint(customer_id, amount);
            con = ConnectionToDB.ConnectToDB();
            stmt = con.createStatement();
            stmt.executeUpdate(queries.get(0));
            stmt.executeUpdate(queries.get(1));
            stmt.close();
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void use(int customer_id, int reward_point) {
        try {
            List<String> queries = generateUseRewardPoint(customer_id, reward_point);
            con = ConnectionToDB.ConnectToDB();
            stmt = con.createStatement();
            stmt.executeUpdate(queries.get(0));
            stmt.executeUpdate(queries.get(1));
            stmt.close();
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private static List<String> generateUpdateRewardPoint(int customer_id, BigDecimal amount) {
        String[] q0 = { "SET @UserPoint = (SELECT reward_point FROM customers WHERE id = ", /* id */ ");" };
        String[] q1 = { "UPDATE customers SET reward_point = @UserPoint + ", /* reward_point */" WHERE id = ",
                /* id */ ";" };
        List<String> queries = new ArrayList<>();
        queries.add(q0[0] + customer_id + q0[1]);
        int reward_point = amount.divide(new BigDecimal("10.000")).toBigInteger().intValueExact();
        queries.add(q1[0] + reward_point + q1[1] + customer_id + q1[2]);
        return queries;
    }

    private static List<String> generateUseRewardPoint(int customer_id, int reward_point) {
        String[] q0 = { "SET @UserPoint = (SELECT reward_point FROM customers WHERE id = ", /* id */ ");" };
        String[] q1 = { "UPDATE customers SET reward_point = @UserPoint - ", /* reward_point */" WHERE id = ",
                /* id */ ";" };
        List<String> queries = new ArrayList<>();
        queries.add(q0[0] + customer_id + q0[1]);
        queries.add(q1[0] + reward_point + q1[1] + customer_id + q1[2]);
        return queries;
    }
}
