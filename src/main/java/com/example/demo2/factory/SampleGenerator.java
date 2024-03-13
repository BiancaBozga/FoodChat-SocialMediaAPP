package com.example.demo2.factory;


import com.example.demo2.business.UserService;

public class SampleGenerator {
    private final UserService userService;
    private boolean alreadyGenerated = false;

    public SampleGenerator(UserService userService) {
        this.userService = userService;
    }

    public void generateSample() {
        if (alreadyGenerated) {
            throw new RuntimeException("Samples have already been generated");
        }
        alreadyGenerated = true;

        userService.addUser("firstName1", "lastName1","passwd1");
        userService.addUser("firstName2", "lastName2","passwd2");
        userService.addUser("firstName3", "lastName3","passwd3");
        userService.addUser("firstName4", "lastName4","passwd4");
        userService.addUser("firstName5", "lastName5","passwd5");
        userService.addUser("firstName6", "lastName6","passwd6");
        userService.addUser("firstName7", "lastName7","passwd7");
        userService.addUser("firstName8", "lastName8","passwd8");
        userService.addUser("firstName9", "lastName9","passwd9");

        userService.addUser("firstName10", "lastName10","passwd1");
        userService.addUser("firstName11", "lastName11","passwd1");
    }
}
