package com.company;
import static com.company.ClientYp3.socket;
import static com.company.ClientYp3.out;

import java.io.IOException;
import java.util.Random;


public class ClientSenderThreadYp3 extends Thread {

    @Override
    public void run() {
    try {
    while (!socket.isClosed()) {
        int number = new Random().nextInt(100) + 1; // creates new random int from 1 to 100
        sleep(1000); // waits for 1sec
            System.out.println("I sent " + number);
            out.writeInt(number); // sends number to server
        }
    } catch(IOException e){
        System.out.println("Connection to Server failed");
    } catch(InterruptedException e){
        System.out.println("Process Interrupted");
    }

}

}