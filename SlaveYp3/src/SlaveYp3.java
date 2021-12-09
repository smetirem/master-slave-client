import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.*;

public class SlaveYp3 {

    private static final int SERVER_PORT = 22366;
    private static final String SERVER_IP = "127.0.0.1";

    private static Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private final List<Integer> numbersFound = new ArrayList<>(); // list for inspection

    private void connect() {

        try {
            socket = new Socket(SERVER_IP, SERVER_PORT); // creates new connection to server
            System.out.println("Connection to server successful");
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Unable to connect to Server with IP: " + SERVER_IP);
            System.exit(1);
        }
    }

    private void communicate() {

        try {
            int number = in.readInt(); // receives number from server
            System.out.println("Searching for " + number + "...");

            if (numbersFound.contains(number)) { // if this slave has received this number before
                out.writeInt(number); // sends result to server
            } else {
                numbersFound.add(number); // if not adds it for future inspections
            }
        } catch (IOException e) {
            System.out.println("Connection to Server dropped");
            System.exit(-1);
        }
    } // process ends by closing the app

    public static void main(String[] args) {
        SlaveYp3 client = new SlaveYp3();
        client.connect(); // creates new connection to server

        while (!socket.isClosed()) {
            client.communicate(); // accepts and sends results from/to server
        }
    }
}

