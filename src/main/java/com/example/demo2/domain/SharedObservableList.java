package com.example.demo2.domain;


import com.example.demo2.domain.entities.Message;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SharedObservableList {
    private static final ObservableList<Message> messageList = FXCollections.observableArrayList();

    public static ObservableList<Message> getMessageList() {
        return messageList;
    }
}

