package com.example.demo2.ui;



import com.example.demo2.business.UserService;
import com.example.demo2.domain.entities.User;
import com.example.demo2.factory.SampleGenerator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collection;

public class UserConsole implements UserInterface{
    private final UserService userService;
    private final SampleGenerator sampleGenerator;

    public UserConsole(UserService userService, SampleGenerator sampleGenerator) {
        this.userService = userService;
        this.sampleGenerator = sampleGenerator;
    }

    private void viewUsers() {
        Collection<User> users = userService.getAll();
        if (users.isEmpty()) {
            System.out.println("User list is empty.");
        }
        else {
            System.out.println("User list:");
            users.forEach(System.out::println);
        }
    }

    private void generateSample() throws RuntimeException {
        sampleGenerator.generateSample();
        System.out.println("Sample generated successfully.\n");
    }

    @Override
    public void run() {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        while(true) {
            System.out.println("========================");
            System.out.println("> Options:\n");
            System.out.println("0 - Exit program.");
            System.out.println("1 - Generate sample.");
            System.out.println("2 - View users.");
            System.out.println("========================");

            try {
                String input = reader.readLine();
                switch(Integer.parseInt(input)) {
                    case 0:
                        System.out.println("Program closing...");
                        return;
                    case 1:
                        generateSample();
                        break;
                    case 2:
                        viewUsers();
                        break;
                    default:
                        System.out.println("Unknown command");
                }
            }
            catch(Exception e) {
                System.out.println("There has been an error:\n" + e.getMessage() + "\n");
            }
        }
    }
}
