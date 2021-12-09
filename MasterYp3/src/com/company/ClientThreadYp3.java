package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

import static com.company.MasterYp3.listClients;
import static com.company.MasterYp3.listSlaves;

public class ClientThreadYp3 extends Thread{

    private final Socket socket;
    protected DataInputStream in;
    protected DataOutputStream out;
    private final int clientId;

    public ClientThreadYp3(Socket socket, int clientId) {
        this.socket = socket;
        this.clientId = clientId;
    }

    @Override
    public void run() {

        int number;
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream()); // slave's thread uses this object
            while (socket.isConnected()) {
                number = in.readInt(); // accepts the client's number
                System.out.println("Client " + clientId + " sent " + number);
                synchronized (listSlaves) {
                    (listSlaves.get(new Random().nextInt(listSlaves.size()))).out.writeInt(number); // sends the number to a random slave
                }
            }

        } catch (IllegalArgumentException ex) { // if slaves' list is empty
            System.out.print(" -> None to handle this task" + "\n");
            this.run(); // returns to run() in case a slave is connected in the future
        }
        catch (IOException e) {
            System.out.println("Connection to Client " + clientId + " dropped");
            synchronized (listClients) {
                MasterYp3.listClients.remove(this); // if connection is ended removes this object from the list
            }
        }
    }
}
