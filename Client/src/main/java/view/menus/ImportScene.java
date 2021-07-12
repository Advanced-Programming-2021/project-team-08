package view.menus;

import controller.ApplicationManger;
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
        if ((Pattern.compile("^menu enter ([A-Za-z]+)$").matcher(userInput)).find()) {
            System.out.println("menu navigation is not possible");
            return 1;
        }
        if ((matcher = Pattern.compile("^import card ([^\\n]+)$").matcher(userInput)).find()) {
            importController.importCard(matcher.group(1).trim());
            return 1;
        } else if ((matcher = Pattern.compile("^export card ([^\\n]+)$").matcher(userInput)).find()) {
            importController.exportCard(matcher.group(1).trim());
            return 1;
        } else if (Pattern.compile("^menu show-current$").matcher(userInput).find()) {
            System.out.println("import menu");
            return 1;
        } else if (Pattern.compile("^menu exit$").matcher(userInput).find()) {
            ApplicationManger.goToScene(SceneName.MAIN_MENU, false);
            return 0;
        }
        System.out.println("invalid command.");
        return 1;
    }

    public void printMessage(String message) {
        System.out.println(message);
    }
}
