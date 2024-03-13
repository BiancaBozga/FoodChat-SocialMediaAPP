package com.example.demo2.factory;


import com.example.demo2.business.FriendshipService;
import com.example.demo2.business.MessageService;
import com.example.demo2.business.UserService;
import com.example.demo2.domain.entities.Message;
import com.example.demo2.domain.validation.UserValidator;
import com.example.demo2.persistence.Repository;
import com.example.demo2.persistence.dbrepos.FriendshipDBRepository;
import com.example.demo2.persistence.dbrepos.MessageDBRepository;
import com.example.demo2.persistence.dbrepos.UserDBRepository;
import com.example.demo2.ui.UserConsole;
import com.example.demo2.ui.UserInterface;

public class Factory {
    private static Factory instance = null;

    private Factory() {
    }

    public static Factory getInstance() {
        if (instance == null) {
            instance = new Factory();
        }
        return instance;
    }

    public BuildContainer build() {
        String url="jdbc:postgresql://localhost:5432/socialnetwork";
        String username = "postgres";
        String password = "postgres";

        UserDBRepository userRepo =
                new UserDBRepository(url, username, password, UserValidator.getInstance());
        Repository<Long, Message> msgRepo =
                new MessageDBRepository(url, username, password);
        FriendshipDBRepository fr_repo =
                new FriendshipDBRepository(url, username, password);
        UserService userService = new UserService(userRepo);
         MessageService msgService=new MessageService(msgRepo,userRepo,fr_repo);
        FriendshipService fr_service=new FriendshipService(fr_repo,userRepo,userService);
        SampleGenerator sampleGenerator = new SampleGenerator(userService);
        UserInterface ui = new UserConsole(userService, sampleGenerator);

        return new BuildContainer(userService, ui, sampleGenerator,msgService,fr_service);
    }
}
