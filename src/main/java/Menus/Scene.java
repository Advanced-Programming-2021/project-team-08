package Menus;

import java.util.Scanner;

public abstract class Scene {
    protected static Scanner scanner = new Scanner(System.in);

    public void start(){
        while (getUserCommand() != 0);
    }

    protected abstract int getUserCommand();
}
