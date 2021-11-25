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

    // the first int -> productTypes(table in database)
    // the second int -> the last product id (to jump to next table)
    private static int[] createNewIDIterator = { 0, 0 };

    private static ObservableList<ObservableProduct> products = FXCollections.observableArrayList();

    private static Connection con = null;
    private static Statement stmt = null;
    private static ResultSet resultSet = null;
    private static String query = "";
    private final static String[] productTypes = { "beer", "coffee", "fruits", "grains", "juice", "meats", "milk",
            "soft_drinks", "tea", "vegetables", "water", "wine", };
    private final static String[] productColName = { "id", "name", "count_per_unit", "quantity", "import_price",
            "export_price", };

    public static ObservableList<ObservableProduct> load() {
        int[] loadColNumber = { 0, 1, 2, 3, 4, 5 };
        query = generateLoadProductsQuery(loadColNumber);
        try {
            con = ConnectionToDB.ConnectToDB();
            stmt = con.createStatement();
            resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt(productColName[0]);
                String newID = createNewID(id);
                String name = resultSet.getString(productColName[1]);
                int countPerUnit = resultSet.getInt(productColName[2]);
                int quantity = resultSet.getInt(productColName[3]);
                BigDecimal importPrice = resultSet.getBigDecimal("import_price");
                BigDecimal exportPrice = resultSet.getBigDecimal("export_price");

                products.add(
                        new ObservableProduct(newID, name, countPerUnit + "", quantity + "", importPrice, exportPrice));
            }
            stmt.close();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }

        return products;
    }

    // for synchronization
    public static void loadQuantity() {
        createNewIDIterator[0] = 0;
        createNewIDIterator[1] = 0;
        int i = 0;
        query = generateLoadProductsQuery(new int[] { 0, 3 });
        try {
            con = ConnectionToDB.ConnectToDB();
            stmt = con.createStatement();
            resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                int quantity = resultSet.getInt(productColName[3]);
                var product = products.get(i);
                product.setQuantity(quantity);
                products.set(i, product);
                i++;
            }
            stmt.close();
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void resetYourQuantity() {
        for (ObservableProduct product : products) {
            product.setUserQuantity(0);
        }
    }

    private static String generateLoadProductsQuery(int[] loadColNumber) {
        String q = "";
        String col = "";
        for (int i : loadColNumber) {
            col += (productColName[i] + ",");
        }
        col = col.substring(0, col.length() - 1);
        for (String productType : productTypes) {
            q += "SELECT " + col + " FROM " + productType + " UNION ALL ";
        }
        q = q.substring(0, q.length() - 6); // delete " UNION "
        return q;
    }

    private static String createNewID(int id) {
        // new_id = name_of_table + id_in_table
        // when load data from database, data is ordered (Ex: 1..9, 1..10,...)
        // if id <= last id -> go to next table
        if (id <= createNewIDIterator[1]) {
            ++createNewIDIterator[0];
        }
        createNewIDIterator[1] = id;
        return productTypes[createNewIDIterator[0]] + "." + id;
    }
}