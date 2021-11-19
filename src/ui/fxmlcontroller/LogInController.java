package ui.fxmlcontroller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.user.User;

import connector.ConnectionToDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import main.MainApp;

public class LogInController {

    Connection connect = null;
    PreparedStatement pstmt = null;
    ResultSet resultSet = null;

    @FXML
    private Button btnLogIn;

    @FXML
    private Label lblAnnounce;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsername;

    @FXML
    void btnLogInActionEvent(ActionEvent event) throws ClassNotFoundException, SQLException {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        String query = "SELECT * FROM persons WHERE username = ? AND password = ?";

        connect = ConnectionToDB.ConnectToDB();

        pstmt = connect.prepareStatement(query);
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        resultSet = pstmt.executeQuery();

        if (resultSet.next()) {
            MainApp.myUser = new User(resultSet.getString("first_name"), resultSet.getString("last_name"),
                    resultSet.getString("username"), resultSet.getInt("rank"), resultSet.getInt("reward_point"));
            lblAnnounce.setTextFill(Color.GREEN);
            lblAnnounce.setText("Success");
        } else {
            lblAnnounce.setTextFill(Color.TOMATO);
            lblAnnounce.setText("Wrong Username or Password");
        }

    }
}
