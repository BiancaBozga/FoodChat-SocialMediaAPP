package com.example.demo2.domain.validation;


import com.example.demo2.domain.entities.User;
import com.example.demo2.exceptions.ValidationException;

public class UserValidator implements Validator<User>{
    private static UserValidator instance = null;

    private UserValidator() {
    }

    public static UserValidator getInstance() {
        if (instance == null) {
            instance = new UserValidator();
        }
        return instance;
    }

    @Override
    public void validate(User entity) throws ValidationException {
        String errors = "";

        if (entity.getId() < 0) {
            errors+="Id must not be a negative number.";
        }

        if (entity.getFirstName().isEmpty()) {
            errors+="First name must not be empty.";
        }

        if (entity.getLastName().isEmpty()) {
            errors+="Last name must not be empty.";
        }
        if(entity.getPasswd().isEmpty()){
            errors+="Passwd must not be null";
        }
        else{
            if(entity.getPasswd().length()<5)
                errors+="Passwd must have more than 5 characters";
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
