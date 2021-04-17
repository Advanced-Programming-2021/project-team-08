package Menus;

public class RegisterScene extends Scene {
    @Override
    protected int getUserCommand() {
        String userInput = scanner.nextLine().trim();

        return 1;
    }
}
