package org.demu.services;

import org.apache.commons.validator.routines.EmailValidator;
import org.demu.models.User;
import org.demu.repos.impl.UserRepo;

public class RegisterService {
    public String register(String username, String password, String password2, String email, String first_name, String last_name) {
        String errorMessage = validate(username, password, password2, email, first_name, last_name);
        if (errorMessage != null){
            return errorMessage;
        }
        createUser(username, password, email, first_name, last_name);
        return "Успешна регистрация";
    }

    private static void createUser(String username, String password, String email, String first_name, String last_name) {
        User user = new User(email, username, password, first_name, last_name);
        new UserRepo().addUser(user);
    }

    private static String validate(String username, String password, String password2, String email, String first_name, String last_name) {
        if (username.length() < 3 || username.length() > 20) {
            return "Името трябва да е между 3 20 букви";
        }
        EmailValidator emailValidator = EmailValidator.getInstance();
        if (!emailValidator.isValid(email)) {
            return "Електронната поща е невалидна";
        }
        if (!password.equals(password2)) {
            return "Паролите са различни";
        }
        if(first_name.isEmpty() || last_name.isEmpty()){
            return "Имената са задължителни!";
        }
        boolean isUsernameExist = new UserRepo().getAllUsers()
            .stream()
            .anyMatch(u -> u.getUsername()
                .equals(username));
        if (isUsernameExist) {
            return "Заето име";
        }
        return null;
    }
}
