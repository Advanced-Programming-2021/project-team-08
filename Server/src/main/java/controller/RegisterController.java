package controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.User;
import model.enums.MessageType;

import java.security.SecureRandom;
import java.util.Base64;


public class RegisterController extends ServerController {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    private static RegisterController registerController = new RegisterController();

    public static RegisterController getInstance() {
        return registerController;
    }

    public static String registerUser(String input) {
        JsonElement jsonElement = JsonParser.parseString(input);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        if (User.doesUsernameExists(jsonObject.get("username").getAsString()))
            return serverMessage(MessageType.ERROR, "user with username " + jsonObject.get("username") + " already exists", null);
        else if (User.doesNicknameExists(jsonObject.get("nickname").getAsString())) {
            return serverMessage(MessageType.ERROR, "user with nickname " + jsonObject.get("nickname") + " already exists", null);
        } else {
            User user = new User(jsonObject.get("username").getAsString(), jsonObject.get("nickname").getAsString(), jsonObject.get("password").getAsString());
            MessageType messageType = FileManager.getInstance().createUser(user);
            if (messageType.equals(MessageType.SUCCESSFUL))
                return serverMessage(messageType, "you successfully created your account", new Gson().toJson(user));
            else return serverMessage(messageType, "some error in creating account", null);
        }
    }

    public static String loginUser(String input) {
        JsonElement jsonElement = JsonParser.parseString(input);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        try {
            User user = User.getUserByUsername(jsonObject.get("username").getAsString());
            if (!user.getUserData().getPassword().equals(jsonObject.get("password").getAsString())) {
                return serverMessage(MessageType.ERROR, "input password in wrong", null);
            }else if (isUserActive(user)) {
                return serverMessage(MessageType.ERROR, "you are already logged in", null);
            }
            else {
                String token = makeToken();
                ServerController.addUser(token, user);
                return serverMessage(MessageType.SUCCESSFUL, token, new Gson().toJson(user.getUserData()));
            }
        } catch (Exception e) {
            return serverMessage(MessageType.ERROR, "there is no username with this username", null);
        }
    }

    @Override
    public String getServerMessage(String input) {
        JsonElement jsonElement = JsonParser.parseString(input);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String methodName = jsonObject.get("method").getAsString();
        switch (methodName) {
            case "login":
                return loginUser(input);
            case "register":
                return registerUser(input);
            case "logout":
                return logout(input);
            default:
                return serverMessage(MessageType.ERROR, "invalid command", null);
        }
    }

    private String logout(String input) {
        JsonObject jsonObject = JsonParser.parseString(input).getAsJsonObject();
        String token = jsonObject.get("token").getAsString();
        if (ServerController.removeUser(token)) {
            return serverMessage(MessageType.SUCCESSFUL, "you logged out", null);
        }
        return serverMessage(MessageType.ERROR, "you are not online", null);
    }

    private static String makeToken() {
        byte[] randomBytes = new byte[30];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
