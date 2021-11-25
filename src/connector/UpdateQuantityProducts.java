package connector;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.invoice.ObservableInvoice;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UpdateQuantityProducts {
    private static ObservableList<ObservableInvoice> products = FXCollections.observableArrayList();
    private static Connection con = null;
    private static Statement stmt = null;

    public static void update(ObservableList<ObservableInvoice> products) {
        UpdateQuantityProducts.products = products;

        try {
            List<String> queries = generateUpdateProductsQuery();
            con = ConnectionToDB.ConnectToDB();
            stmt = con.createStatement();
            for (String query : queries) {
                stmt.executeUpdate(query);
            }
            stmt.close();
        } catch (ClassNotFoundException | SQLException e) {
            if (e.getMessage().indexOf("out of range") != -1) {
                System.err.println("OUT OF RANGE");
                // create new alert window
                Stage stage = new Stage();
                Label label = new Label("The number of products you need is larger than the number of products available");
                stage.setTitle("OUT OF RANGE");
                stage.setScene(new Scene(new Pane(label)));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
            } else {
                System.err.println(e.getMessage());
            }
        }
    }

    private static List<String> generateUpdateProductsQuery() {
        List<String> queries = new ArrayList<>();
        String[] q0 = { "SET @svQuantity = (SELECT quantity FROM ", /* table_name */ " WHERE id = ", /* id */ ");" };
        String[] q1 = { "INSERT INTO ", /* table_name */ " (id, quantity) VALUES (", /* id */ ", @svQuantity - ",
                /* user_quantity */ ") ON DUPLICATE KEY UPDATE quantity=VALUES(quantity);" };
        // Example: set @myvar = (select quantity from beer where id = 3);
        // insert into beer (id, quantity) values (3,@myvar - 20) on duplicate key
        // update quantity = values(quantity);

        for (ObservableInvoice product : products) {
            // idparse(product_id) = { table_name, id_in_table }
            String[] idParse = product.getId().split("\\.");
            int userQuantity = product.getQuantity();
            String query0 = q0[0] + idParse[0] + q0[1] + idParse[1] + q0[2];
            String query1 = q1[0] + idParse[0] + q1[1] + idParse[1] + q1[2] + userQuantity + q1[3];
            queries.add(query0);
            queries.add(query1);
        }

        return queries;
    }
}
