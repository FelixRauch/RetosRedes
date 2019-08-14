
import java.net.*;
import java.io.*;
import java.util.Vector;



public class servermult
{

    /***
     * @param port The port at which clients will connect
     * The constructor servermult creates a serverSocket and a new thread for each client trying to connect to the server
     */

    private servermult(int port)
    {


        Vector<ObjectOutputStream> clients = new Vector<>();

        try
        {
            ServerSocket server = new ServerSocket(port);
            while(true) {
                Socket socket = server.accept();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                clients.add(objectOutputStream);
                new Thread(new serverthread(socket, clients, objectOutputStream)).start();
                System.out.println("Client connected");
            }
        }
        catch(IOException i)
        {
            System.out.println(i.getMessage());
        }
    }

    public static void main(String args[])
    {
        servermult server = new servermult(8000);
    }
}
