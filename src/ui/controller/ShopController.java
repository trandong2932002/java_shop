package ui.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.product.ObservableProduct;
import com.user.UserType;

import connector.LoadProducts;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import main.MainApp;

public class ShopController {

    ObservableList<ObservableProduct> products = null;

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
    private Circle userImage;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label userRankLabel;

    @FXML
    void backBtnClicked(ActionEvent event) throws IOException {
        // delete user, log out
        MainApp.myUser = null;

        Parent root = FXMLLoader.load(getClass().getResource("../view/LogIn.fxml"));
        Scene scene = new Scene(root, 600, 400);
        Stage stage = (Stage) backBtn.getScene().getWindow();

        stage.setTitle("Login");
        stage.setScene(scene);
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
        LoadProducts loadProducts = new LoadProducts();
        products = loadProducts.load();

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

        tableProducts.setItems(products);
        tableProducts.setEditable(true);

    }
}
