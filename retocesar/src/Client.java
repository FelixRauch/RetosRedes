
// A Java program for a Client
import java.net.*;
import java.io.*;

import static java.lang.Math.abs;
import static java.lang.Math.floorMod;

public class Client
{
    // initialize socket and input output streams
    private Socket socket            = null;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    // constructor to put ip address and port
    public Client(String address, int port)
    {
        // establish a connection
        try
        {
            socket = new Socket(address, port);
            System.out.println("Connected");

            objectInputStream  = new ObjectInputStream(socket.getInputStream());

            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

        }
        catch(UnknownHostException u)
        {
            System.out.println(u);
        }
        catch(IOException i)
        {
            System.out.println(i);
        }

        // string to read message from input
        String line = "";

        // keep reading until "Over" is input
            try
            {
                //abcedfghijklmnopqrstuvwxyz
                objectOutputStream.writeObject("CaliEsCaliAbcDeFgHiJkLmNoPqRsTuVwXyZ");
                String encrypted = (String)objectInputStream.readObject();
                int cipher = (int)objectInputStream.readObject();
                //System.out.println(encrypted);
                System.out.println(cipher);

                StringBuffer decrypted = new StringBuffer();

                for(char c : encrypted.toCharArray())
                {
                    if((int)c < 0x5B && (int)c > 0x40)
                    {
                        //System.out.println("Enc: "+ (int)c + "," + c + ",Cipher: " +  cipher);
                        int enc = (c - cipher - 65);
                        enc = (char)((floorMod(enc, 26) >= 0 ? floorMod(enc, 26) : floorMod(enc , 26) + 26));
                        enc += 65;
                        decrypted.append((char)enc);
                        //System.out.println("Dec: " + (int)enc + "," + (char)enc);
                        //System.out.println(c);
                    }
                    else
                    {
                        //System.out.println("Enc: "+ (int)c + "," + c + ",Cipher: " +  cipher);
                        int enc = (c - cipher - 97);
                        enc = (char)((floorMod(enc, 26) >= 0 ? floorMod(enc, 26) : floorMod(enc , 26) + 26));
                        enc += 97;
                        decrypted.append((char)enc);
                        //System.out.println("Dec: " + (int)enc + "," + (char)enc);



                    }
                }
                System.out.println(decrypted.toString());
            }
            catch(Exception e)
            {
                System.out.println(e);
            }

        // close the connection
        try
        {
            objectInputStream.close();
            objectOutputStream.close();
            socket.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }

    public static void main(String args[])
    {
        Client client = new Client("localhost", 8000);
    }
}
