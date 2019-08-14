
// A Java program for a Server
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import static java.lang.Math.floorMod;

public class servermult
{
    //initialize socket and input stream
    private Socket          socket   = null;
    private ServerSocket    server   = null;
    private Socket clientSocket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream  objectInputStream;
    private Random random;
    private Vector<ObjectOutputStream> clients;

    // constructor with port
    public servermult(int port)
    {


        clients = new Vector<ObjectOutputStream>();

        // starts server and waits for a connection
        try
        {
            server = new ServerSocket(port);
            while(true) {
                socket = server.accept();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                clients.add(objectOutputStream);
                new Thread(new serverthread(socket, clients, objectOutputStream)).start();
                System.out.println("Client connected");
            }
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }

    public static void main(String args[])
    {
        servermult server = new servermult(8000);
    }
}
