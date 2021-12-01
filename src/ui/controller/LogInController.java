package ui.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.user.User;

import connector.ConnectionToDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import main.MainApp;

public class LogInController {

    private Connection connect = null;
    private PreparedStatement pstmt = null;
    private ResultSet resultSet = null;

    @FXML
    private Button btnLogIn;

    @FXML
    private Label lblAnnounce;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsername;

    @FXML
    void initialize() {
        btnLogIn.setDefaultButton(true);
    }

    @FXML
    void btnLogInActionEvent(ActionEvent event)
            throws ClassNotFoundException, SQLException, InterruptedException, IOException {
        if (checkLogIn()) {
            Parent root = FXMLLoader.load(getClass().getResource("../view/Shop.fxml"));
            Scene scene = new Scene(root, 915, 600);
            Stage stage = (Stage) btnLogIn.getScene().getWindow();

            stage.setTitle("Shop");
            stage.setScene(scene);
        }
    }

    private boolean checkLogIn() throws ClassNotFoundException, SQLException {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        // connect and query
        String query = "SELECT * FROM customers WHERE username = ? AND password = ?";
        connect = ConnectionToDB.ConnectToDB();
        pstmt = connect.prepareStatement(query);
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        resultSet = pstmt.executeQuery();

        if (resultSet.next()) {
            // create user for login session
            int id = resultSet.getInt("id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String userName = resultSet.getString("username");
            int rank = resultSet.getInt("rank");
            int rewardPoint = resultSet.getInt("reward_point");
            MainApp.myUser = new User(id, firstName, lastName, userName, rank, rewardPoint);
            return true;
        } else {
            lblAnnounce.setTextFill(Color.TOMATO);
            lblAnnounce.setText("Wrong Username or Password");
            return false;
        }

    }
}
