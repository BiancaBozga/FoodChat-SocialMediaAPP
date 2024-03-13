package com.example.demo2.factory;


import com.example.demo2.business.FriendshipService;
import com.example.demo2.business.MessageService;
import com.example.demo2.business.UserService;
import com.example.demo2.ui.UserInterface;

public class BuildContainer {
    private final UserService userService;
    private final MessageService messageService;
    private final FriendshipService fr_service;
    private final UserInterface ui;
    private final SampleGenerator sampleGenerator;

    public MessageService getMessageService() {
        return messageService;
    }

    public SampleGenerator getSampleGenerator() {
        return sampleGenerator;
    }

    public BuildContainer(UserService userService, UserInterface ui, SampleGenerator sampleGenerator,MessageService messageService,FriendshipService fr_service) {
        this.userService = userService;
        this.ui = ui;
        this.sampleGenerator = sampleGenerator;
        this.messageService=messageService;
        this.fr_service=fr_service;
    }

    public UserInterface getUi() {
        return ui;
    }

    public UserService getUserService() {
        return userService;
    }
    public FriendshipService getFr_service(){
        return fr_service;
    }
}
