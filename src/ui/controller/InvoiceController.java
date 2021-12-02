package ui.controller;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.invoice.ObservableInvoice;
import com.product.ObservableProduct;

import connector.LoadProducts;
import connector.UpdateOrder;
import connector.UpdateQuantityProducts;
import connector.UpdateRewardPoint;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.MainApp;

public class InvoiceController {

    private static ObservableList<ObservableInvoice> productsInvoice = FXCollections.observableArrayList();
    private static int userRewardPoint;
    private static StringProperty totalPrice = new SimpleStringProperty("0.000");   // tổng tiền
    private static StringProperty total = new SimpleStringProperty("0.000");        // tiền khách trả
    private static StringProperty discount = new SimpleStringProperty("0.000");     // điểm thưởng quy đổi thành tiền

    // total -> (objectBinding = total price - discount) -> discount
    private static ObjectBinding<String> objectBinding = new ObjectBinding<String>() {
        {
            bind(discount);
        }

        @Override
        protected String computeValue() {
            return (new BigDecimal(totalPrice.get()).subtract(new BigDecimal(discount.get()))).toString();
        }
    };

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
    private RadioButton RP1000Btn;
    @FXML
    private RadioButton RP100Btn;
    @FXML
    private RadioButton RP500Btn;
    @FXML
    private ToggleGroup RewardPointGroup;

    @FXML
    private Label usernameLabel;
    @FXML
    private Label dateLabel;

    @FXML
    private Label userRPLabel;
    @FXML
    private Label totalPriceLabel;
    @FXML
    private Label discountLabel;
    @FXML
    private Label totalLabel;
    @FXML
    private Button payBtn;

    @FXML
    void payBtnClicked(ActionEvent event) {
        // update quantity in database
        UpdateQuantityProducts.update(productsInvoice);
        UpdateRewardPoint.update(MainApp.myUser.getId(), new BigDecimal(totalPrice.get()));
        
        int reward_point = 0;
        switch (discount.get()) {
            case "10.000":
            reward_point = 100;
            break;
            case "60.000":
            reward_point = 500;
            break;
            case "150.000":
            reward_point = 1000;
            break;
            default:
            break;
        }
        UpdateRewardPoint.use(MainApp.myUser.getId(), reward_point);
        UpdateOrder.update(MainApp.myUser.getId(), new BigDecimal(totalPrice.get()), reward_point);

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
        userRewardPoint = MainApp.myUser.getReward_point();
        usernameLabel.setText(MainApp.myUser.getUsername());
        dateLabel.setText(LocalDate.now().toString());
        userRPLabel.setText(userRewardPoint + "");

        // set price label
        totalPriceLabel.textProperty().bind(totalPrice);

        // ? bug: because objectBinding bind to discount, if total price has changed,
        // total must be change too
        // multiple binding is not allowed (maybe I not sure)
        // so I change discount to update new total price
        discount.set("1.000");
        discount.set("0.000");
        // smell code :D

        discountLabel.textProperty().bind(discount);

        total.bind(objectBinding);
        totalLabel.textProperty().bind(total);

        // total price - discount = total
        RewardPointGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                if (RewardPointGroup.getSelectedToggle() != null) {
                    RadioButton radioBtnSelected = (RadioButton) RewardPointGroup.getSelectedToggle();
                    // get RP points and Price of RP points
                    String[] point_price = radioBtnSelected.getText().split(" ");
                    int point = Integer.parseInt(point_price[0]);
                    String price = point_price[3].substring(0, point_price[3].length() - 1);
                    BigDecimal rpPrice = new BigDecimal(price);

                    if (point > userRewardPoint) {
                        alertBox("You don't have enough reward points to use this vouchers.");
                        radioBtnSelected.setSelected(false);
                        discount.set("0.000");
                        return;
                    }

                    if (rpPrice.compareTo(new BigDecimal(totalPrice.get())) == 1) {
                        alertBox("This vouchers is greater than amount.");
                        radioBtnSelected.setSelected(false);
                        discount.set("0.000");
                        return;
                    }

                    discount.set(rpPrice.toString());
                }
            }
        });
    }

    public static void deinitialize() {
        total.unbind();
        discount.set("0.000");
    }

    public static void loadProductsInShoppingCart(ObservableList<ObservableProduct> products) {
        int i = 1;
        productsInvoice.clear();
        BigDecimal tmpPrice = BigDecimal.ZERO;
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
                tmpPrice = tmpPrice.add(productInvoice.getPrice());
            }
        }
        totalPrice.set(tmpPrice.toString());
    }

    public static ObservableList<ObservableInvoice> getProductsInvoice() {
        return productsInvoice;
    }

    public static void setProductsInvoice(ObservableList<ObservableInvoice> productsInvoice) {
        InvoiceController.productsInvoice = productsInvoice;
    }

    private static void alertBox(String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}