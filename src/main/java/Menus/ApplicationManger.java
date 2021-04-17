package Menus;

public class ApplicationManger {
    private Scene currentScene;

    public void run(){
        goToScene(SceneName.REGISTER_MENU);
    }

    public void goToScene(SceneName sceneName){
        switch (sceneName){
            case REGISTER_MENU:
                currentScene = new RegisterScene();
                currentScene.start();
                break;
            // TODO: ۱۸/۰۴/۲۰۲۱ other cases
        }
    }
}
