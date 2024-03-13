package com.example.demo2;

import com.example.demo2.factory.BuildContainer;
import com.example.demo2.factory.Factory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class StartApplication extends Application {
    BuildContainer container = Factory.getInstance().build();

    @Override
    public void start(Stage stage) throws IOException, NoSuchAlgorithmException {



        FXMLLoader userLoader = new FXMLLoader();
        userLoader.setLocation(getClass().getResource("login.fxml"));
        AnchorPane userLayout = userLoader.load();
        stage.setScene(new Scene(userLayout));

        LogInController userController = userLoader.getController();

        userController.setmsgService(container.getMessageService());
        userController.setuserService(container.getUserService());
        userController.setfrService(container.getFr_service());



        Stage secondStage = new Stage();

        // Set up

        userController.setPrimaryStage(stage);
        userController.setSecondStage(secondStage);






        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}