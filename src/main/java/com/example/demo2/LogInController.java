package com.example.demo2;


import com.example.demo2.business.FriendshipService;
import com.example.demo2.business.MessageService;
import com.example.demo2.business.UserService;
import com.example.demo2.domain.entities.Message;
import com.example.demo2.factory.BuildContainer;
import com.example.demo2.factory.Factory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class LogInController  {
    MessageService service;
    UserService service_user;
    FriendshipService fr_service;
    @FXML
    private TextField user_id;
    @FXML
    private PasswordField passwd;
    private Stage primaryStage;
    private Stage secondStage;
    @FXML
    private Label warning;
    ObservableList<Message> model = FXCollections.observableArrayList();


    public void setmsgService(MessageService msgservice) {

        service=msgservice;

    }
    public void setuserService(UserService u_service) {

        service_user=u_service;

    }
    public void setfrService(FriendshipService fr1_service) {
        fr_service=fr1_service;

    }
    @FXML
    private void handleregisterButton(ActionEvent event){
        //showSecondWindow();
        try {
            BuildContainer container = Factory.getInstance().build();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("register.fxml"));
            Parent root = loader.load();


            // Set up the controller for the message.fxml file
            RegisterController messageController = loader.getController();

            // You can pass data to the MessageController if needed
           messageController.setUserService(service_user);
           messageController.setMessagerService(service);
           messageController.setFrService(fr_service);

            // Set the content for the second stage
            secondStage.setScene(new Scene(root));
            messageController.setPrimaryStage(secondStage);
            messageController.setSecondStage(primaryStage);
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

private String bytesToHex(byte[] bytes) {
    StringBuilder hexStringBuilder = new StringBuilder();
    for (byte b : bytes) {
        hexStringBuilder.append(String.format("%02X", b));
    }
    return hexStringBuilder.toString();
}
@FXML
private void handleLogInButton(ActionEvent event) {
    try {
        warning.setText("");
        BuildContainer container = Factory.getInstance().build();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("message-view.fxml"));
        Parent root = loader.load();

        // Set up the controller for the message.fxml file
        MessagesController messageController = loader.getController();

        // You can pass data to the MessageController if needed

        messageController.set_u(Long.parseLong(user_id.getText()));
        messageController.setFrindshipService(fr_service);
//        UserService s=container.getUserService();
        messageController.setUserService(service_user);



        messageController.setmsgService(service);


        // Create a new stage for each login
        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));
        messageController.setPrimaryStage(newStage);
        String passwd1=passwd.getText();
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(passwd1.getBytes());
        byte[] digest = md.digest();
        String actualHash = bytesToHex(digest).toUpperCase();


        if(service_user.verifica(actualHash, Long.parseLong(user_id.getText()))){
        // Show the new window
        newStage.show();}
        else warning.setText("Parola incorecta!!!");




    } catch (IOException e) {
        e.printStackTrace();

        throw new RuntimeException(e);
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
    }
}

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Inject the second stage into the controller
    public void setSecondStage(Stage secondStage) {
        this.secondStage = secondStage;
    }
    private void showSecondWindow() {
        Label label = new Label("This is the second window");

        VBox layout2 = new VBox(10);
        layout2.getChildren().addAll(label);

        Scene scene2 = new Scene(layout2, 300, 200);
        secondStage.setScene(scene2);
        secondStage.setTitle("Second Window");

        // Hide the first window
        primaryStage.hide();

        // Show the second window
        secondStage.show();
    }


}
