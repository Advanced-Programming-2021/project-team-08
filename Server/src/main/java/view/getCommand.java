package view;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class getCommand extends Thread{

    @Override
    public void run() {
        getAdminCommand();
    }

    public void getAdminCommand() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            Matcher matcher;
            if ((matcher = getCommandMatcher(input, "ban card (.+)")) != null) {

            }
            if ((matcher = getCommandMatcher(input, "free card (.+)")) != null) {

            }
            if ((matcher = getCommandMatcher(input, "change card number (-?\\d+)")) != null) {

            }
        }
    }

    private Matcher getCommandMatcher(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) return matcher;
        else return null;
    }
}
