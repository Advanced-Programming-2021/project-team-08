package controller;

import model.enums.MessageType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ServerManager {
    ServerSocket serverSocket;


    public void runServer() {
        try {
            serverSocket = new ServerSocket(7755);
            while (true) {
                Socket socket = serverSocket.accept();
                ServerThread serverThread = new ServerThread();
                serverThread.init(socket, serverSocket);
                serverThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                if (input.equals("")) break;
                ServerController serverController = ServerController.getController(input);
                String result;
                if (serverController == null) {
                    result = ServerController.serverMessage(MessageType.ERROR,"invalid server controller", null);
                }else {
                    result = serverController.getServerMessage(input);
                }
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
