package view.menus;

import controller.ApplicationManger;
import controller.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.UserData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.regex.Pattern;

public class ScoreboardMenu extends Scene {
    public VBox vBox;
    public HBox hBox;

    @FXML
    void initialize(){
        int i;
        Label rank=new Label();
        Label nickName=new Label();
        Label point=new Label();
        UserData[] scoreboard = User.getAllUserData();
        Arrays.sort(scoreboard, new sort());
        int size=Math.min(20,scoreboard.length);
        UserData[] topPlayers=new UserData[20];
        for (i=0;i<size;i++)
            topPlayers[i]=scoreboard[i];
        int[] renks=getRank(topPlayers,size);
        for (i=0;i<size;i++) {
            nickName.setText(topPlayers[i].getNickname());
            point.setText(String.valueOf(topPlayers[i].getPoint()));
            rank.setText(String.valueOf(renks[i]));
            if (topPlayers[i].getUsername().equals(ApplicationManger.getLoggedInUser().getUsername())){
                nickName.setTextFill(Color.GREEN);
                point.setTextFill(Color.GREEN);
            }
            hBox.getChildren().clear();
            vBox.getChildren().clear();
            hBox.getChildren().add(0,rank);
            hBox.getChildren().add(1,nickName);
            hBox.getChildren().add(2,point);
            vBox.getChildren().add(i,hBox);
        }
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
        ApplicationManger.goToScene1(SceneName.PROFILE_MENU,false);
    }

    public int[] getRank(UserData[] topPlayers,int size){
        int pointOfPreviousUser = 0;
        int[] rank=new int[size];
        int i;
        for (i=0;i<size;i++) rank[i]=i;
        for (i=0;i<size;i++){
            if (topPlayers[i].getPoint()==pointOfPreviousUser)
                rank[i]--;
            pointOfPreviousUser=topPlayers[i].getPoint();
        }
        for (i=0;i<size;i++) rank[i]++;
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