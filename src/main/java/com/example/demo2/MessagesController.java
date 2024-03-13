package com.example.demo2;


import com.example.demo2.business.FriendshipService;
import com.example.demo2.business.MessageService;
import com.example.demo2.business.UserService;
import com.example.demo2.domain.entities.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;

import java.util.stream.Collectors;

public class MessagesController implements Observer {
   MessageService service;
    UserService service_user;
    FriendshipService fr_service;
    @FXML
    private TextField u1;

    @FXML
    private TextField u2;
    @FXML
    private TextField msg;
    @FXML
    private TextField userId;
    ObservableList<Message> model  = FXCollections.observableArrayList();
    ObservableList<FriendRequest> model1  = FXCollections.observableArrayList();
    ObservableList<User> model2  = FXCollections.observableArrayList();
    @FXML
    ComboBox<Long> comboBoxUser1;
    @FXML
    ComboBox<Long> comboBoxUser2;
//    List<Long> customList= Arrays.asList(53L,51L);

    @FXML
    TableView<Message> tableView;
    @FXML
    TableView<FriendRequest> tableView1;
    @FXML
    TableView<User> tableView11;
    @FXML
    TableColumn<FriendRequest,Long> tableColumnID_from;
    @FXML
    TableColumn<User,Long> tableColumnID_Friend;
    @FXML
    TableColumn<FriendRequest,String> tableColumnstatus;
    @FXML
    TableColumn<Message,String> tableColumnMessage;
    @FXML
    TableColumn<Message,Long> tableColumnID;
    @FXML
    TableColumn<Message,Long> tableColumnFrom;
    @FXML
    TableColumn<Message, List<Long>> tableColumnTo;
    @FXML
    TableColumn<Message, Long> tableColumnReply;
    @FXML
    TableColumn<Message, LocalDateTime> tableColumnDate;
    private Stage primaryStage;
    private Stage secondStage;
    Long u;
    public void set_u(Long ul){
        u=ul;
    }
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Inject the second stage into the controller
    public void setSecondStage(Stage secondStage) {
        this.secondStage = secondStage;
    }
    private void refreshTableView() {
        // Aici poți reîncărca datele în TableView
        tableView.getItems().setAll(service.get_conversation_from_one(u));

    }
    @FXML
    private void handleFriendRequestButton(ActionEvent event){
        Long selectedItem1=Long.parseLong(userId.getText());
        service.send_friend_request(u,selectedItem1);
    }
    @FXML
    private void handleChatButton(ActionEvent event){
//        Long selectedItem1 = comboBoxUser1.getValue();
//        Long selectedItem2 = comboBoxUser1.getValue();
       // Long selectedItem1=Long.parseLong(u1.getText());
  //      Long selectedItem2=Long.parseLong(u2.getText());
//        ObservableList<User> selectedItems = tableView11.getSelectionModel().getSelectedItems();
//       List<Long> l=new ArrayList<>();
//        selectedItems.forEach(o->l.add(o.getId()));
        User selecteditem=tableView11.getSelectionModel().getSelectedItem();

        Collection<Message> m=service.get_conversation(u,selecteditem.getId());
       // Collection<Message> m=service.get_conversation_(u,l);
        model.setAll(m);
        tableView.setItems(model);
       // refreshTableView();
    }
    @FXML
    private void handleReplyButton(ActionEvent event){
        Message  selectedmessage = tableView.getSelectionModel().getSelectedItem();
        String mesaj = msg.getText();
        List<Long> l=new ArrayList<>();
        l.add(selectedmessage.getFrom());
        service.save(u,l,mesaj,selectedmessage.getId());
    }
    @FXML
    private void handleSendButton(ActionEvent event){
      //  Long user1=Long.parseLong(u1.getText());
//        Long user2=Long.parseLong(u2.getText());
        ObservableList<User> selectedItems = tableView11.getSelectionModel().getSelectedItems();

        if (!selectedItems.isEmpty()) {
            selectedItems.forEach(o -> System.out.println("Selected User ID: " + o.getId()));
            List<Long> l=new ArrayList<>();
            String mesaj = msg.getText();
            selectedItems.forEach(user -> l.add(user.getId()));
            service.save(u,l,mesaj,0L);
            System.out.println(l);
            model.setAll(service.get_conversation_from_one(u));
            refreshTableView();
        } else {
            System.out.println("No users selected.");
        }



    }
    @FXML
    private void handleAcceptButton(ActionEvent event){

        FriendRequest selectedUser = tableView1.getSelectionModel().getSelectedItem();
        try {
             Long id1=selectedUser.getFrom_id();
             Long id2= selectedUser.getTo_id();
             Long id=selectedUser.getId();
            fr_service.update_fr_req(id,id1,id2,"accepted");
            Friendship newFriendship = new Friendship();
            newFriendship.setId(new Tuple<>(id1, id2));
            fr_service.add(newFriendship);
            refreshTableView();
        } catch (Exception e) {
            System.out.println("Eroare la acceptare friendrequest: " + e.getMessage());
        }
    }
    @FXML
    private void handleRejectButton(ActionEvent event){
        FriendRequest selectedUser = tableView1.getSelectionModel().getSelectedItem();
        try {
            Long id1=selectedUser.getFrom_id();
            Long id2= selectedUser.getTo_id();
            Long id=selectedUser.getId();
            fr_service.update_fr_req(id,id1,id2,"rejected");
            refreshTableView();
        } catch (Exception e) {
            System.out.println("Eroare la resingere friendrequest: " + e.getMessage());
        }
    }
    @FXML
    private void handlerefreshButton(ActionEvent event){
        Collection<Message> msg = service.get_conversation_from_one(u);
        model.setAll(msg);
        tableView.setItems(model);
    }
    public void setUserService(UserService userservice) {

        this.service_user=userservice;
        this.service_user.addObserver(this);
        initModelUser();



    }
    public void setFrindshipService(FriendshipService frservice) {

        this.fr_service=frservice;
        this.fr_service.addObserver(this);
        initModel1();
    }

    private void initModelUser() {
        Collection<Friendship> l=  fr_service.getAll();
        Collection<User> u1=service_user.getAll();
        Collection<User> resultList = new ArrayList<>();


            u1.forEach(user -> {
                Friendship newFriendship = new Friendship();
                newFriendship.setId(new Tuple<>(u, user.getId()));
                if(l.contains(newFriendship))
               {

                    resultList.add(user);
                }
                Friendship newFriendship1 = new Friendship();
                newFriendship1.setId(new Tuple<>(user.getId(),u));
                if(l.contains(newFriendship1))
                {

                    resultList.add(user);
                }
            });

        model2.setAll(resultList);
        tableView11.setItems(model2);
    }

    public void setmsgService(MessageService msgservice) {

        this.service=msgservice;
        this.service.addObserver(this);
        initModel();
    }






    private List<Long> get_lista() {
        List<Long> listaCuDuplicate = new ArrayList<>();
        service.getAll().forEach(x -> {
            listaCuDuplicate.add(x.getFrom());
            x.getTo().forEach(t->listaCuDuplicate.add(t));
        });
        List<Long> listaFaraDuplicate = listaCuDuplicate.stream()
                .distinct()
                .collect(Collectors.toList());
        return listaFaraDuplicate;
    }
    @FXML
    public void initialize() {
       // List<Long> customList=Arrays.asList(49L,55L,53L,51L);


        tableColumnID.setCellValueFactory(new PropertyValueFactory<Message, Long>("id"));
        tableColumnMessage.setCellValueFactory(new PropertyValueFactory<Message, String>("message"));
        tableColumnFrom.setCellValueFactory(new PropertyValueFactory<Message, Long>("from"));
        tableColumnTo.setCellValueFactory(new PropertyValueFactory<Message,List<Long>>("to"));
        tableColumnReply.setCellValueFactory(new PropertyValueFactory<Message,Long>("reply_id"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<Message,LocalDateTime>("date"));
        tableColumnID_from.setCellValueFactory(new PropertyValueFactory<FriendRequest, Long>("from_id"));
        tableColumnstatus.setCellValueFactory(new PropertyValueFactory<FriendRequest,String>("status"));
        tableColumnID_Friend.setCellValueFactory(new PropertyValueFactory<User, Long>("id"));
        tableView11.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView1.setItems(model1);
        tableView11.setItems(model2);
        tableView.setItems(model);


    }

    private void initModel() {
      //  System.out.println(u1);
        Collection<Message> msg = service.get_conversation_from_one(u);
        model.setAll(msg);


    }
    private void initModel1() {
      //  System.out.println(u1);
        Collection<FriendRequest> msg = fr_service.find_all_req(u);
        //msg.forEach(o-> System.out.println(o));
        model1.setAll(msg);



    }

    @Override
    public void update() {

        initModel();
        initModel1();
        initModelUser();
    }


}
