package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MasterYp3 extends Thread{

    private static final int CLIENT_PORT = 22333; // port for clients
    private static final int SLAVE_PORT = 22366; // port for slaves

    private final int serverPort; // port for thread's initialization depending on the class (slave or client)
    private Socket socketHosts; // socket to carry a connection
    protected static List<SlaveThreadYp3> listSlaves = Collections.synchronizedList(new ArrayList<>()); // list of slaves connected at a time
    protected static List<ClientThreadYp3> listClients = Collections.synchronizedList(new ArrayList<>()); // list of clients connected at a time
    private static int clientId = 1; // clients' numbering
    private static int slaveId = 1; // slaves' numbering

    public MasterYp3(int serverPort) {
        this.serverPort = serverPort;
    }

    @Override
    public void run() {

        try {
            // server socket depending on the class (slave or client thread )
            ServerSocket serverSocket = new ServerSocket(serverPort); // creates new connection to server
            System.out.println("Server socket " + serverPort + " is ready");

            while (!serverSocket.isClosed()) {
                socketHosts = serverSocket.accept(); // accepts new connection

                // if initialized from constructor port is the clients' port creates a new client thread to handle connection
                if (serverPort == CLIENT_PORT) {
                    ClientThreadYp3 client = new ClientThreadYp3(socketHosts, clientId);
                    synchronized (listClients) {
                        listClients.add(client); // adds object to the appropriate list
                    }
                    System.out.println("New thread has been created for Client " + clientId);
                    clientId++; //increases the id for next connection
                    client.start();
                } else {
                    // if initialized from constructor port is the slaves' port creates a new client thread to handle connection
                    SlaveThreadYp3 slave = new SlaveThreadYp3(socketHosts, slaveId);
                    synchronized (listSlaves) {
                        listSlaves.add(slave); // adds object to the appropriate list
                    }
                    System.out.println("New thread has been created for Slave " + slaveId);
                    slaveId++; //increases the id for next connection
                    slave.start();
                }
            }
            } catch(IOException e){
                System.out.println("Unable to listen to accept new connection using port" + socketHosts);
            }
    }

    public static void main(String[] args) {

        MasterYp3 masterForClients = new MasterYp3(CLIENT_PORT); // creates a thread to accept clients' connections
        MasterYp3 masterForSlaves = new MasterYp3(SLAVE_PORT); // creates a thread to accept slaves' connections
        masterForClients.start();
        masterForSlaves.start();

    }



}
