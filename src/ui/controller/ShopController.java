package ui.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.product.ObservableProduct;
import com.user.UserType;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import main.MainApp;

public class ShopController {

    @FXML
    private TableView<ObservableProduct> tableProducts;
    @FXML
    private TableColumn<ObservableProduct, String> nameCol;
    @FXML
    private TableColumn<ObservableProduct, Integer> quantityCol;
    @FXML
    private TableColumn<ObservableProduct, Integer> unitPriceCol;
    @FXML
    private TableColumn<ObservableProduct, String> yourQuantityCol;
    @FXML
    private TableColumn<ObservableProduct, String> amountCol;

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

    }
}
