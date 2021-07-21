package view.menus;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import controller.ApplicationManger;
import controller.User;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.UserData;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.regex.Pattern;

public class ScoreboardMenu extends Scene {
    public VBox vBox;


    @FXML
    void initialize() {
        int i, size, counter = 0;
        int[] ranks;
        String result = ApplicationManger.getServerResponse("scoreboard", "scoreboard", null);
        JsonElement jsonElement = JsonParser.parseString(result);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String returnObject = jsonObject.get("returnObject").getAsString();
        String[] parts = returnObject.split(",");
        HashMap<String, Integer> jsonHash = new HashMap<>();
        for (i = 0; i < parts.length; i++) {
            parts[i] = parts[i].replace("\"", "");
            parts[i] = parts[i].replace("{", "");
            parts[i] = parts[i].replace("}", "");
            parts[i] = parts[i].replace(" ", "");
            String[] subparts = parts[i].split("=");
            if (i == 20) break;
            jsonHash.put(subparts[0], Integer.parseInt(subparts[1]));
        }
        size = Math.min(20, parts.length);
        ranks = getRankServer(jsonHash, size);
        for (String j : jsonHash.keySet()) {
            Label rank = new Label();
            Label nickName = new Label();
            Label point = new Label();
            HBox hBox = new HBox();
            hBox.setSpacing(20);
            rank.setAlignment(Pos.CENTER);
            nickName.setAlignment(Pos.CENTER);
            point.setAlignment(Pos.CENTER);
            rank.setStyle("-fx-background-color: rgb(182, 202, 242)");
            nickName.setStyle("-fx-background-color: rgb(182, 202, 242)");
            point.setStyle("-fx-background-color: rgb(182, 202, 242)");
            rank.setPrefWidth(140);
            nickName.setPrefWidth(200);
            point.setPrefWidth(50);
            nickName.setText(j);
            point.setText(String.valueOf(jsonHash.get(j)));
            rank.setText(String.valueOf(ranks[counter]));
            if (j.equals(ApplicationManger.getLoggedInUser().getUserData().getNickname())) {
                nickName.setTextFill(Color.GREEN);
                point.setTextFill(Color.GREEN);
                rank.setTextFill(Color.GREEN);
                rank.setStyle("-fx-background-color: rgb(245, 166, 102)");
                nickName.setStyle("-fx-background-color: rgb(245, 166, 102)");
                point.setStyle("-fx-background-color: rgb(245, 166, 102)");
            }
            hBox.getChildren().clear();
//            vBox.getChildren().clear();

            hBox.getChildren().add(0, rank);
            hBox.getChildren().add(1, nickName);
            hBox.getChildren().add(2, point);
            vBox.getChildren().add(hBox);
            counter++;
        }

//        UserData[] scoreboard = User.getAllUserData();
//        Arrays.sort(scoreboard, new sort());
//        size = Math.min(20, scoreboard.length);
//        UserData[] topPlayers = new UserData[size];
//        for (i = 0; i < size; i++)
//            topPlayers[i] = scoreboard[i];
//        ranks = getRank(topPlayers, size);
//        for (i = 0; i < size; i++) {
//            Label rank = new Label();
//            Label nickName = new Label();
//            Label point = new Label();
//            HBox hBox = new HBox();
//            hBox.setSpacing(20);
//            rank.setAlignment(Pos.CENTER);
//            nickName.setAlignment(Pos.CENTER);
//            point.setAlignment(Pos.CENTER);
//            rank.setStyle("-fx-background-color: rgb(182, 202, 242)");
//            nickName.setStyle("-fx-background-color: rgb(182, 202, 242)");
//            point.setStyle("-fx-background-color: rgb(182, 202, 242)");
//            rank.setPrefWidth(140);
//            nickName.setPrefWidth(200);
//            point.setPrefWidth(50);
//            nickName.setText(topPlayers[i].getNickname());
//            point.setText(String.valueOf(topPlayers[i].getPoint()));
//            rank.setText(String.valueOf(ranks[i]));
//            if (topPlayers[i].getUsername().equals(ApplicationManger.getLoggedInUser().getUsername())) {
//                nickName.setTextFill(Color.GREEN);
//                point.setTextFill(Color.GREEN);
//                rank.setTextFill(Color.GREEN);
//                rank.setStyle("-fx-background-color: rgb(245, 166, 102)");
//                nickName.setStyle("-fx-background-color: rgb(245, 166, 102)");
//                point.setStyle("-fx-background-color: rgb(245, 166, 102)");
//            }
//            hBox.getChildren().clear();
////            vBox.getChildren().clear();
//
//            hBox.getChildren().add(0, rank);
//            hBox.getChildren().add(1, nickName);
//            hBox.getChildren().add(2, point);
//            vBox.getChildren().add(hBox);
//        }
    }

    public static void showScoreboard() {
        UserData[] scoreboard = User.getAllUserData();
        Arrays.sort(scoreboard, new sort());
        int rowNumber = 1;
        int counterOfSamePoint = 0;
        int pointOfPreviousUser = 0;
        for (UserData userData : scoreboard) {
            if (rowNumber == 1) {
                System.out.println(rowNumber + "- " + userData.getNickname() + ": " + userData.getPoint());
                rowNumber++;
            } else {
                if (pointOfPreviousUser == userData.getPoint()) {
                    System.out.println((rowNumber - 1) + "- " + userData.getNickname() + ": " + userData.getPoint());
                    counterOfSamePoint++;
                } else {
                    if (counterOfSamePoint != 0) {
                        rowNumber += counterOfSamePoint;
                        counterOfSamePoint = 0;
                    }
                    System.out.println(rowNumber + "- " + userData.getNickname() + ": " + userData.getPoint());
                    rowNumber++;
                }
            }
            pointOfPreviousUser = userData.getPoint();
        }
    }

    @Override
    protected int getUserCommand() {
        String userInput = scanner.nextLine().trim();
        if ((Pattern.compile("^menu enter ([A-Za-z]+)$").matcher(userInput)).find()) {
            System.out.println("menu navigation is not possible");
            return 1;
        }
        if (Pattern.compile("^scoreboard show$").matcher(userInput).find()) {
            showScoreboard();
        } else if (Pattern.compile("^menu show-current$").matcher(userInput).find()) {
            System.out.println("ScoreBoard Menu");
        } else if (Pattern.compile("^menu exit$").matcher(userInput).find()) {
            ApplicationManger.goToScene(SceneName.MAIN_MENU, false);
            return 0;
        } else {
            System.out.println("invalid command");
        }
        return 1;
    }


    public void back(MouseEvent mouseEvent) {
        ApplicationManger.goToScene1(SceneName.PROFILE_MENU, false);
    }

    public int[] getRank(UserData[] topPlayers, int size) {
        int counterOfSamePoint = 1;
        int pointOfPreviousUser = 0;
        int[] rank = new int[size];
        int i;
        for (i = 0; i < size; i++) rank[i] = i;
        for (i = 0; i < size; i++) {
            if (topPlayers[i].getPoint() == pointOfPreviousUser) {
                rank[i] -= counterOfSamePoint;
                counterOfSamePoint++;
            } else {
                counterOfSamePoint = 1;
            }
            pointOfPreviousUser = topPlayers[i].getPoint();
        }
        for (i = 0; i < size; i++) rank[i]++;
        return rank;
    }

    public int[] getRankServer(HashMap<String, Integer> jsonHash, int size) {
        int counterOfSamePoint = 1;
        int pointOfPreviousUser = 0;
        int[] rank = new int[size];
        int i, counter = 0;
        for (i = 0; i < size; i++) rank[i] = i;
        for (String j : jsonHash.keySet()) {
            if (jsonHash.get(j) == pointOfPreviousUser) {
                rank[counter] -= counterOfSamePoint;
                counterOfSamePoint++;
            } else {
                counterOfSamePoint = 1;
            }
            pointOfPreviousUser = jsonHash.get(j);
            counter++;
        }
        for (i = 0; i < size; i++) rank[i]++;
        return rank;
    }
}


class sort implements Comparator<UserData> {
    public int compare(UserData a, UserData b) {
        if (a.getPoint() != b.getPoint())
            return b.getPoint() - a.getPoint();
        else return a.getNickname().compareTo(b.getNickname());
    }
}