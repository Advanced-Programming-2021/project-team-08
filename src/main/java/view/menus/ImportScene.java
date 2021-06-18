package view.menus;

import controller.ImportController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImportScene extends Scene {

    private ImportController importController;

    public ImportScene() {
        importController = new ImportController(this);
    }

    @Override
    protected int getUserCommand() {
        String userInput = scanner.nextLine().trim();
        Matcher matcher;
        if ((matcher = Pattern.compile("^import card ([a-zA-Z ]+)$").matcher(userInput)).find()) {
            importController.importCard(matcher.group(1).trim());
            return 1;
        } else if ((matcher = Pattern.compile("^export card ([a-zA-Z ]+)$").matcher(userInput)).find()) {
            importController.exportCard(matcher.group(1).trim());
            return 1;
        } else if (Pattern.compile("^menu show-current$").matcher(userInput).find()) {
            System.out.println("import menu");
            return 1;
        } else if (Pattern.compile("^menu exit$").matcher(userInput).find()) {
            return 0;
        }
        System.out.println("invalid command.");
        return 1;
    }

    public void printMessage(String message) {
        System.out.println(message);
    }
}
