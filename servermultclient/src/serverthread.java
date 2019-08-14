import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;
import java.util.Vector;

import static java.lang.Integer.toHexString;
import static java.lang.Thread.yield;


/***
 * The class serverthread handles the logic for the individual threads connected to the server
 */


public class serverthread implements Runnable {
    private Socket clientSocket;
    private ObjectInputStream objectInputStream;

    private Vector<ObjectOutputStream> clients;
    private ObjectOutputStream socketOutputStream;

    serverthread(Socket socket, Vector<ObjectOutputStream> clients, ObjectOutputStream socketOutputStream) {
        try {
            this.socketOutputStream = socketOutputStream;
            this.clients = clients;
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            clientSocket = socket;

        } catch (Exception e) {
            System.out.println("ERROR: Could not construct ServerThread");
        }
    }


    public void run() {
        try {

            String argument;
            Object result = null;

            while ((argument = (String) objectInputStream.readObject()).equals("")) ;

            switch (argument) {

                case "request":
                    Random random = new Random();
                    int cipher = random.nextInt(20) + 1;
                    String stringCipher = toHexString(cipher);

                    for (ObjectOutputStream client : clients) {
                        client.writeObject(stringCipher);
                    }
                    clients.remove(socketOutputStream);
                    String msg = (String) objectInputStream.readObject();
                    for (ObjectOutputStream client : clients) {
                        client.writeObject(msg);
                    }
                    break;
                case "receiver":
                    while (true) {
                        yield();
                    }
            }

        } catch (Exception e) {
            System.out.println("ERROR: could not read Client Input");
        }
        try {
            clientSocket.close();
        } catch (Exception e) {
            System.out.println("Socket could not be closed");
        }
    }
}
