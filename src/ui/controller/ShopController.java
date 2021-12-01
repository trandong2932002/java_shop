package ui.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.product.ObservableProduct;
import com.user.UserType;

import connector.LoadProducts;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.MainApp;

public class ShopController {

    private ObservableList<ObservableProduct> products = null;

    @FXML
    private TableView<ObservableProduct> tableProducts;
    @FXML
    private TableColumn<ObservableProduct, String> nameCol;
    @FXML
    private TableColumn<ObservableProduct, String> quantityCol;
    @FXML
    private TableColumn<ObservableProduct, String> priceCol;
    @FXML
    private TableColumn<ObservableProduct, String> userQuantityCol;
    @FXML
    private TableColumn<ObservableProduct, String> userAmountCol;

    @FXML
    private Button backBtn;
    @FXML
    private Button invoicingBtn;
    @FXML
    private Button loadQuantityBtn;

    @FXML
    private Circle userImage;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label userRankLabel;
    @FXML
    private TextField txtFilterName;

    @FXML
    void backBtnClicked(ActionEvent event) throws IOException {
        // delete user, log out
        MainApp.myUser = null;
        products.clear();

        Parent root = FXMLLoader.load(getClass().getResource("../view/LogIn.fxml"));
        Scene scene = new Scene(root, 600, 400);
        Stage stage = (Stage) backBtn.getScene().getWindow();

        stage.setTitle("Login");
        stage.setScene(scene);
    }

    @FXML
    void invoicingBtnClicked(ActionEvent event) throws IOException {
        // table
        InvoiceController.loadProductsInShoppingCart(products);
        if (InvoiceController.getProductsInvoice().size() >= 1) {
            // window
            Stage invoiceStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../view/Invoice.fxml"));
            Scene scene = new Scene(root);
            invoiceStage.setTitle("Invoice");
            invoiceStage.setScene(scene);
            invoiceStage.setResizable(false);
            /**
             * block all windows except invoice window because: 1 - user buy another
             * product, and it lead to a new invoice 2 - user logout or cancel invoice ? Is
             * there any way to improve this? or other logic?
             */
            invoiceStage.initModality(Modality.APPLICATION_MODAL);

            invoiceStage.show();

            invoiceStage.setOnHiding(e -> {
                InvoiceController.deinitialize();
            });
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Invoice error");
            alert.setHeaderText(null);
            alert.setContentText("Quantity of Products must be greater than 0");
            alert.showAndWait();
        }
    }

    @FXML
    void loadQuantityBtnClicked(ActionEvent event) {
        LoadProducts.loadQuantity();
    }

    @FXML
    void initialize() {
        // logout cancel button
        backBtn.setCancelButton(true);
        // user infomation
        try {
            userImage.setFill(new ImagePattern(new Image(new FileInputStream("./resource/user_icon.png"))));
        } catch (FileNotFoundException e) {
        }
        userNameLabel.setText(MainApp.myUser.getUsername());
        userRankLabel.setText(UserType.valueOf(MainApp.myUser.getRank()).name());

        // load data from database
        products = LoadProducts.load();

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("exportPrice"));

        // editable
        userQuantityCol.setCellValueFactory(new PropertyValueFactory<>("userQuantity"));
        userQuantityCol.setCellFactory(TextFieldTableCell.forTableColumn());
        userQuantityCol.setOnEditCommit(new EventHandler<CellEditEvent<ObservableProduct, String>>() {
            @Override
            public void handle(CellEditEvent<ObservableProduct, String> t) {
                ObservableProduct rowProduct = t.getTableView().getItems().get(t.getTablePosition().getRow());
                Integer newUserQuantity = Integer.parseInt(t.getNewValue());
                ((ObservableProduct) rowProduct).setUserQuantity(newUserQuantity);
            }
        });

        userAmountCol.setCellValueFactory(new PropertyValueFactory<>("userAmount"));

        // wrap the ObservableList in FilteredList (initially display all data)
        FilteredList<ObservableProduct> filteredProducts = new FilteredList<>(products, b -> true);

        // set the filter Predicate whenever the filter change (add listener)
        txtFilterName.textProperty().addListener((observable, oldVal, newVal) -> {
            filteredProducts.setPredicate(product -> {
                // if textfield is empty, display all products
                if (newVal == null || newVal.isEmpty()) {
                    return true;
                }

                // search by name
                if (product.getName().indexOf(newVal) != -1) {
                    return true;
                }
                /* I make string id = type + id in table -> search by type in string id */
                else if (product.getId().indexOf(newVal) != -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        tableProducts.setItems(filteredProducts);
        tableProducts.setEditable(true);

    }
}
