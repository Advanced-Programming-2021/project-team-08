package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.User;
import model.UserData;
import model.cards.data.ReadMonsterCardsData;
import model.cards.data.ReadSpellTrapCardsData;
import model.enums.MessageType;
import view.GetCommand;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerManager {
    private static ServerSocket serverSocket;

    private static HashMap<String, Boolean> isInGame = new HashMap<>();

    public static HashMap<String, Boolean> getIsInGame() {
        return isInGame;
    }

    public static ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void runServer() {
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            new GetCommand().start();
            serverSocket = new ServerSocket(12345);
            while (true) {
                Socket socket = serverSocket.accept();
                isInGame.put(socket.getRemoteSocketAddress().toString(), false);
                ServerThread serverThread = new ServerThread(socket, serverSocket);
                serverThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() throws IOException {
        new ReadMonsterCardsData().readCardsData();
        new ReadSpellTrapCardsData().readSpellTrapData();
        File users = new File("users");
        users.mkdir();

        File directoryPath = new File("users");
        File[] filesList = directoryPath.listFiles();

        ArrayList<User> allOfUsers = new ArrayList<>();
        if (filesList != null) {
            for (File file : filesList) {
                String stringOfUserFile = new String(Files.readAllBytes(Paths.get(file.toString())));
                allOfUsers.add(new User(new Gson().fromJson(stringOfUserFile,
                        new TypeToken<UserData>() {
                        }.getType())));
            }

        } else {
            System.out.println("file list is null");
        }

        User.setAllUser(allOfUsers);
    }
}


class ServerThread extends Thread {
    Socket socket;
    ServerSocket serverSocket;

    public ServerThread(Socket socket, ServerSocket serverSocket) {
        this.socket = socket;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        try {
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            ServerController serverController = null;
            while (!ServerManager.getIsInGame().get(socket.getRemoteSocketAddress().toString())) {
                String input = dataInputStream.readUTF();
                System.out.println("input: " + input + ", socket:" + socket.getRemoteSocketAddress());
                System.out.println("socket:" + socket.getRemoteSocketAddress() + "," + ServerManager.getIsInGame().get(socket.getRemoteSocketAddress().toString()));
                String result;
                if (ServerManager.getIsInGame().get(socket.getRemoteSocketAddress().toString())) break;
                if (input.equals("")) break;
                if (input.equals("alive")) break;
                if ((result = ServerController.checkToken(input)) == null) {
                    serverController = ServerController.getController(input);
                    if (serverController instanceof GameConnectionController) {
                        ((GameConnectionController) serverController).addGameWaiter(socket, input);
                    }
                    if (serverController == null) {
                        result = ServerController.serverMessage(MessageType.ERROR, "invalid client controller name", null);
                    } else {
                        result = serverController.getServerMessage(input);
                    }
                }
                System.out.println("result: " + result);
                dataOutputStream.writeUTF(result);
                dataOutputStream.flush();
            }
            System.out.println("socket:" + socket.getRemoteSocketAddress() + "," + ServerManager.getIsInGame().get(socket.getRemoteSocketAddress().toString()));
        } catch (IOException e) {
            if (e instanceof SocketException) {
                System.out.println("one client disconnected");
            } else {
                e.printStackTrace();
            }
        }
    }
}
