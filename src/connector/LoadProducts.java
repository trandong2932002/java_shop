package connector;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.product.ObservableProduct;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LoadProducts {
    private ObservableList<ObservableProduct> products = FXCollections.observableArrayList();

    private Connection con = null;
    private Statement stmt = null;
    private ResultSet resultSet = null;
    private String query = "";
    private final static String[] productTypes = { "beer", "coffee", "fruits", "grains", "juice", "meats", "milk",
            "soft_drinks", "tea", "vegetables", "water", "wine", };
    private final static String[] productColName = { "name", "count_per_unit", "quantity", "import_price",
            "export_price", };

    public ObservableList<ObservableProduct> load() {
        query = generateQuery();
        try {
            con = ConnectionToDB.ConnectToDB();
            stmt = con.createStatement();
            resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                String name = resultSet.getString(productColName[0]);
                int countPerUnit = resultSet.getInt(productColName[1]);
                int quantity = resultSet.getInt(productColName[2]);
                BigDecimal importPrice = resultSet.getBigDecimal("import_price");
                BigDecimal exportPrice = resultSet.getBigDecimal("export_price");

                products.add(new ObservableProduct(name, countPerUnit, quantity, importPrice, exportPrice));
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }

        return products;
    }

    private String generateQuery() {
        String q = "";
        for (String productType : productTypes) {
            q += "SELECT " + productColName[0] + "," + productColName[1] + "," + productColName[2] + ","
                    + productColName[3] + "," + productColName[4] + " FROM " + productType + " UNION ";
        }
        q = q.substring(0, q.length() - 6);
        return q;
    }
}
