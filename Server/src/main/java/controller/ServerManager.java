package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.User;
import model.UserData;
import model.cards.data.ReadMonsterCardsData;
import model.cards.data.ReadSpellTrapCardsData;
import model.enums.MessageType;

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

public class ServerManager {
    ServerSocket serverSocket;


    public void runServer() {
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            serverSocket = new ServerSocket(7755);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("local socket is: " + socket.getLocalSocketAddress());
                System.out.println("remote socket is: " + socket.getRemoteSocketAddress());
                ServerThread serverThread = new ServerThread();
                serverThread.init(socket, serverSocket);
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

        }else {
            System.out.println("file list is null");
        }

        User.setAllUser(allOfUsers);
    }
}


class ServerThread extends Thread {
    Socket socket;
    ServerSocket serverSocket;

    public void init(Socket socket, ServerSocket serverSocket) {
        this.socket = socket;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        try {
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            while (true) {
                String input = dataInputStream.readUTF();
                String result;
                if (input.equals("")) break;
                if ((result = ServerController.checkToken(input)) == null) {
                    ServerController serverController = ServerController.getController(input);
                    if (serverController == null) {
                        result = ServerController.serverMessage(MessageType.ERROR,"invalid client message", null);
                    }else {
                        result = serverController.getServerMessage(input);
                    }
                }
                System.out.println(result);
                dataOutputStream.writeUTF(result);
                dataOutputStream.flush();
            }
            dataInputStream.close();
            socket.close();
            serverSocket.close();
        } catch (IOException e) {
            if (e instanceof SocketException) {
                System.out.println("one client disconnected");
            } else {
                e.printStackTrace();
            }
        }
    }


}
