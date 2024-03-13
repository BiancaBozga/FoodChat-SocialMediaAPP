package com.example.demo2;


import com.example.demo2.business.FriendshipService;
import com.example.demo2.business.MessageService;
import com.example.demo2.business.UserService;
import com.example.demo2.factory.BuildContainer;
import com.example.demo2.factory.Factory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegisterController {
    MessageService service;
    UserService service_user;
    FriendshipService fr_service;
    @FXML
    private TextField first_name;
    @FXML
    private PasswordField passwd;
    @FXML
    private TextField last_name;
    private Stage primaryStage;
    private Stage secondStage;
    @FXML
    private Label id_user=new Label("Your user id is:");
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Inject the second stage into the controller
    public void setSecondStage(Stage secondStage) {
        this.secondStage = secondStage;
    }
    public void setUserService(UserService userservice) {

        service_user=userservice;

    }
    public void setMessagerService(MessageService messageservice) {

        service=messageservice;

    }
    public void setFrService(FriendshipService frservice) {

        this.fr_service=frservice;

    }
    private String bytesToHex(byte[] bytes) {
        StringBuilder hexStringBuilder = new StringBuilder();
        for (byte b : bytes) {
            hexStringBuilder.append(String.format("%02X", b));
        }
        return hexStringBuilder.toString();
    }
    @FXML
    private void handleRegisterButton(ActionEvent event) throws NoSuchAlgorithmException {
        String firstName = first_name.getText();
        String lastName =last_name.getText();
        String passwd1=passwd.getText();
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(passwd1.getBytes());
        byte[] digest = md.digest();
        String actualHash = bytesToHex(digest).toUpperCase();

        try {
            Long id=service_user.addUser(firstName, lastName,actualHash);

            id_user.setText("Your user id is:"+id.toString());

        } catch (Exception e) {
            System.out.println("Eroare la adăugarea utilizatorului: " + e.getMessage());
        }
    }
    @FXML
    private void handleHomeButton(ActionEvent event){

        try {
            BuildContainer container = Factory.getInstance().build();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();


            // Set up the controller for the message.fxml file
            LogInController LogInController = loader.getController();

            // You can pass data to the MessageController if needed
            LogInController.setmsgService(service);
            LogInController.setfrService(fr_service);
            LogInController.setuserService(service_user);
            LogInController.setSecondStage(primaryStage);
            // Set the content for the second stage
            secondStage.setScene(new Scene(root));
            LogInController.setPrimaryStage(secondStage);

            // Show the second window
            secondStage.show();

            // Hide the first window
            if (primaryStage != null) {
                primaryStage.hide();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
