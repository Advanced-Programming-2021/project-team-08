package view.menus;

import controller.ApplicationManger;
import controller.User;
import model.UserData;


import java.util.*;
import java.util.regex.Pattern;

public class ScoreboardMenu extends Scene{

    @Override
    protected int getUserCommand() {
        String userInput = scanner.nextLine().trim();
        if (Pattern.compile("^scoreboard show$").matcher(userInput).find()) {
            showScoreboard();
        }
        else if (Pattern.compile("^menu show-current$").matcher(userInput).find()) {
            System.out.println("ScoreBoard Menu");
        }
        else if (Pattern.compile("^menu exit$").matcher(userInput).find()) {
            ApplicationManger.goToScene(SceneName.MAIN_MENU);
            return 0;
        }
        else {
            System.out.println("invalid command");
        }
        return 1;
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


}

class sort implements Comparator<UserData> {
    public int compare(UserData a, UserData b) {
        if (a.getPoint() != b.getPoint())
            return b.getPoint() - a.getPoint();
        else return a.getNickname().compareTo(b.getNickname());
    }
}