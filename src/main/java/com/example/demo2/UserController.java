package com.example.demo2;


import com.example.demo2.business.UserService;
import com.example.demo2.domain.entities.Message;
import com.example.demo2.domain.entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Collection;
import java.util.Objects;

public class UserController {
    UserService service;


    ObservableList<User> model = FXCollections.observableArrayList();
    @FXML
    private TextField first_name;

    @FXML
    private TextField last_name;
    @FXML
    private PasswordField passwd;
    @FXML
    TableView<Message> tableView2;
    @FXML
    TableView<User> tableView;
    @FXML
    TableColumn<User,String> tableColumnLastName;
    @FXML
    TableColumn<User,Long> tableColumnUserID;
    @FXML
    TableColumn<User,String> tableColumnFirstName;
    private void refreshTableView() {

        tableView.getItems().setAll(service.getAll());
    }

    public void setUserService(UserService userservice) {

        service=userservice;
         initModel();
    }
    @FXML
    private void handleAddButton(ActionEvent event) {

        String firstName = first_name.getText();
        String lastName =last_name.getText();
        String passwd1=passwd.getText();
        try {
            service.addUser(firstName, lastName,passwd1);
            refreshTableView();
        } catch (Exception e) {
            System.out.println("Eroare la adăugarea utilizatorului: " + e.getMessage());
        }

    }

    @FXML
    private void handleDeleteButton(ActionEvent event) {

        User selectedUser = tableView.getSelectionModel().getSelectedItem();
        try {
            service.deleteUser(selectedUser);
            refreshTableView();
        } catch (Exception e) {
            System.out.println("Eroare la stergerea utilizatorului: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateButton(ActionEvent event) {
        String firstName = first_name.getText();
        String lastName =last_name.getText();
        String passwd1=passwd.getText();
        User selectedUser = tableView.getSelectionModel().getSelectedItem();
        try {
            if(Objects.equals(firstName, ""))
                firstName=selectedUser.getFirstName();
            if(Objects.equals(lastName, ""))
                lastName=selectedUser.getLastName();
            service.updateUser(firstName,lastName,selectedUser.getId(),passwd1);
            refreshTableView();
        } catch (Exception e) {
            System.out.println("Eroare la adăugarea utilizatorului: " + e.getMessage());
        }
    }
    @FXML
    public void initialize() {
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        tableColumnUserID.setCellValueFactory(new PropertyValueFactory<User, Long>("id"));
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        tableView.setItems(model);

    }

    private void initModel() {
        Collection<User> users = service.getAll();
        model.setAll(users);

    }
}