package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final int PORT = 8778;

    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(PORT);
            System.out.println("Budget_1.5 Server started...");

            while(true) {
                Socket clientSocket = ss.accept();
                System.out.println("Client collecting...");
                ServerClientThread sct = new ServerClientThread(clientSocket);
                sct.start();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
