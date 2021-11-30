package ui.controller;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.invoice.ObservableInvoice;
import com.product.ObservableProduct;

import connector.LoadProducts;
import connector.UpdateOrder;
import connector.UpdateQuantityProducts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.MainApp;

public class InvoiceController {

    private static ObservableList<ObservableInvoice> productsInvoice = FXCollections.observableArrayList();

    private static BigDecimal totalPrice;

    @FXML
    private TableView<ObservableInvoice> invoiceTable;
    @FXML
    private TableColumn<ObservableInvoice, String> noCol;
    @FXML
    private TableColumn<ObservableInvoice, String> nameCol;
    @FXML
    private TableColumn<ObservableInvoice, String> quantityCol;
    @FXML
    private TableColumn<ObservableInvoice, String> unitPriceCol;
    @FXML
    private TableColumn<ObservableInvoice, String> priceCol;

    @FXML
    private Label usernameLabel;
    @FXML
    private Label InvoiceIDLabel;
    @FXML
    private Label dateLabel;

    @FXML
    private Label totalPriceLabel;
    @FXML
    private Button payBtn;

    @FXML
    void payBtnClicked(ActionEvent event) {
        // update quantity in database
        UpdateQuantityProducts.update(productsInvoice);
        UpdateOrder.update(MainApp.myUser.getId(), totalPrice);

        LoadProducts.loadQuantity();
        LoadProducts.resetYourQuantity();
        
        Stage stage = (Stage) usernameLabel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void initialize() {
        noCol.setCellValueFactory(new PropertyValueFactory<>("numeroSign"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        unitPriceCol.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // loadProductsInShoppingCart must be called before
        // initialize(FXMLLoader.load())
        invoiceTable.setItems(productsInvoice);

        // information
        usernameLabel.setText(MainApp.myUser.getUsername());
        InvoiceIDLabel.setText("get from database");
        dateLabel.setText(LocalDate.now().toString());
        // total price
        totalPriceLabel.setText(totalPrice.toString());
    }

    public static void loadProductsInShoppingCart(ObservableList<ObservableProduct> products) {
        int i = 1;
        productsInvoice.clear();
        totalPrice = new BigDecimal("0");
        for (ObservableProduct product : products) {
            int productQuantity = product.getUserQuantity();
            if (productQuantity > 0) {
                String id = product.getId();
                String productName = product.getName();
                BigDecimal productUnitPrice = product.getExportPrice();

                ObservableInvoice productInvoice = new ObservableInvoice(id, i++, productName, productQuantity,
                        productUnitPrice);
                productsInvoice.add(productInvoice);
                // calc total price
                totalPrice = totalPrice.add(productInvoice.getPrice());
            }
        }
    }

    public static ObservableList<ObservableInvoice> getProductsInvoice() {
        return productsInvoice;
    }

    public static void setProductsInvoice(ObservableList<ObservableInvoice> productsInvoice) {
        InvoiceController.productsInvoice = productsInvoice;
    }

    
}