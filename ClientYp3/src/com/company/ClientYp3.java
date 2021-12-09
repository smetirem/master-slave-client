package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class ClientYp3 {

    private static final int SERVER_PORT = 22333;
    private static final String SERVER_IP = "127.0.0.1";
    protected static Socket socket;
    private static DataInputStream in;
    protected static DataOutputStream out;


    private void connect() {

        try {
            socket = new Socket(SERVER_IP, SERVER_PORT); // client's socket for connection
            System.out.println("Connection to server successful");
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            // creates a thread to receive and print data
            ClientSenderThreadYp3 senderThread = new ClientSenderThreadYp3();
            senderThread.start();
        } catch (IOException e) {
            System.out.println("Unable to connect to Server with IP: " + SERVER_IP);
            System.exit(1);
        }
    }

    private void communicate() {

        try {
            while (ClientYp3.socket.isClosed()) {
            System.out.println(in.readUTF());
            }
            } catch (IOException e) {
                System.out.println("Connection to Server dropped");
            }
    } // process ends by closing the app


    public static void main(String[] args) {
        ClientYp3 client = new ClientYp3();
        client.connect(); // creates new connection to server
        client.communicate(); // accepts and prints results from server

    }
}










