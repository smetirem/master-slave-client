package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static com.company.MasterYp3.listClients;
import static com.company.MasterYp3.listSlaves;

public class SlaveThreadYp3 extends Thread{

    private final  Socket socket;
    protected DataInputStream in;
    protected DataOutputStream out;
    private final int slaveId;

    public SlaveThreadYp3(Socket socket, int slaveId) {
        this.socket = socket;
        this.slaveId = slaveId;
    }

    @Override
    public void run() {

        int number; // stores

        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream()); // client's thread uses this object
            while (socket.isConnected()) {
                number = in.readInt(); // receives from the connected slave the result
                System.out.println("Slave " + slaveId + " found number " + number);
                synchronized (listClients) {
                    for (ClientThreadYp3 c : listClients) { // sends it to all clients connected
                        c.out.writeUTF("Slave " + slaveId + " found number " + number);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Connection to Slave " + slaveId + " dropped");
            synchronized (listSlaves) {
                MasterYp3.listSlaves.remove(this); // if connection is ended removes this object from the list
            }
        }
    }
}

