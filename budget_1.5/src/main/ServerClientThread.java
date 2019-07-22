package main;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class ServerClientThread extends Thread {

    private Socket clientSocket;
    private int fileName;
    private ArrayList<Summary> activeSummaries;
    private ObjectOutputStream out;
    private ObjectInputStream in;


    ServerClientThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
        activeSummaries = new ArrayList<>();
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(clientSocket.getInputStream());
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            fileName = in.readInt();
            boot();
            out.writeObject(activeSummaries);
            out.flush();
        } catch(FileNotFoundException e) {
            //If file is not found, create a new one
            try {
                System.out.println("Rebuilding boot...");
                rebuildBoot();
                boot();
                out.writeObject(activeSummaries);
                out.flush();
            } catch(IOException f) {
                e.printStackTrace();
            }
        } catch(StreamCorruptedException e) { //If file is illegally modified, this deletes and creates a new file.
            System.out.println("File is corrupt. Rebuilding boot...");
            new File("summary_repos/" + fileName + ".dat").delete();
            try {
                rebuildBoot();
                boot();
                out.writeObject(activeSummaries);
                out.flush();
            } catch (IOException f) {
                f.printStackTrace();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

        try {
            while(true) {
                activeSummaries = (ArrayList) in.readObject();
                reformatFile();
            }
        } catch(ClassNotFoundException e) {
            System.out.println("Goodbye...");
        } catch(IOException e) {
            System.out.println("Goodbye...");
        } finally {
            try {
                in.close();
                out.close();
                clientSocket.close();
                reformatFile();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void boot() throws IOException{
        ObjectInputStream os = new ObjectInputStream(
                new FileInputStream(
                        new File("summary_repos/" + fileName + ".dat")));
        while(true) {
            try {
                activeSummaries.add((Summary) os.readObject());
            } catch(Exception e) {
                break;
            }
        }
        os.close();
    }

    private void rebuildBoot() throws IOException {
        ObjectOutputStream os = new ObjectOutputStream(
                new FileOutputStream(
                        new File("summary_repos/" + fileName + ".dat")));

        os.close();
    }

    private void reformatFile() throws IOException{
        ObjectOutputStream os = new ObjectOutputStream(
                new FileOutputStream(
                        new File("summary_repos/" + fileName + ".dat"), false));

        for(Summary s : activeSummaries) {
            os.writeObject(s);
        }

        os.close();
    }
}
