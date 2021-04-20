package Controllers;

import Models.UserData;

public class User {
    private final UserData userData;

    {
        userData = new UserData();
    }

    public UserData getUserData() {
        return userData;
    }
}

