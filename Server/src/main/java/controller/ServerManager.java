package controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ServerManager {
    ServerSocket serverSocket;

    {
        try {
            serverSocket = new ServerSocket(7755);
            while (true) {
                Socket socket = serverSocket.accept();
                ServerThread serverThread = new ServerThread();
                serverThread.init(socket, serverSocket, null);
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
    ServerController serverController;

    public void init(Socket socket, ServerSocket serverSocket, ServerController serverController) {
        this.socket = socket;
        this.serverSocket = serverSocket;
        this.serverController = serverController;

    }

    @Override
    public void run() {
        try {
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            while (true) {
                String input = dataInputStream.readUTF();
                if (input.equals("")) break;
                String result = serverController.getServerMessage(input);
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
