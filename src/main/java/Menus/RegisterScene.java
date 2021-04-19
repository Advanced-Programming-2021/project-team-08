package Menus;

public class RegisterScene extends Scene {
    @Override
    protected int getUserCommand() {
        String userInput = scanner.nextLine().trim();
        System.out.println("test commit!");
        
        return 1;
    }
}
