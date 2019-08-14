
// A Java program for a Client

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import static java.lang.Math.floorMod;

public class ClientMult
{
    // initialize socket and input output streams
    private Socket socket            = null;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    // constructor to put ip address and port
    public ClientMult(String address, int port, boolean active /*soruce/receiver*/, String msg)
    {
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
        if(active) {
            try {
                //abcedfghijklmnopqrstuvwxyz
                objectOutputStream.writeObject("request");
                String stringCipher = (String) objectInputStream.readObject();
                int cipher = Integer.parseInt(stringCipher, 16);
                String plain_msg = msg;

                StringBuffer encrypted_msg = new StringBuffer();
                for (char c : plain_msg.toCharArray()) {
                    if ((int) c < 0x5B && (int) c > 0x40) {
                        //System.out.println("Enc: "+ (int)c + "," + c + ",Cipher: " +  cipher);
                        int enc = (c + cipher - 65);
                        enc = (char) ((floorMod(enc, 26) >= 0 ? floorMod(enc, 26) : floorMod(enc, 26) + 26));
                        enc += 65;
                        //System.out.println("Dec: " + (int)enc + "," + (char)enc);
                        encrypted_msg.append((char) (enc));
                        //System.out.println(c);

                    } else {
                        //System.out.println("Enc: "+ (int)c + "," + c + ",Cipher: " +  cipher);
                        int enc = (c + cipher - 97);
                        enc = (char) ((floorMod(enc, 26) >= 0 ? floorMod(enc, 26) : floorMod(enc, 26) + 26));
                        enc += 97;
                        //System.out.println("Dec: " + (int)enc + "," + (char)enc);
                        encrypted_msg.append((char) enc);
                        //System.out.println((int) enc + "," + enc);
                    }
                }
                objectOutputStream.writeObject(encrypted_msg.toString());
                //objectOutputStream.writeObject(cipher);
                System.out.println("Cipher: " + stringCipher);
                System.out.println("Plain text: " + msg);
                System.out.println("Encrypted message: " + encrypted_msg.toString());

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            // close the connection
            try {
                objectInputStream.close();
                objectOutputStream.close();
                socket.close();
            } catch (IOException i) {
                System.out.println(i);
            }
        }
        else
        {
            while(true) {
                try {
                    objectOutputStream.writeObject("receiver");
                    String stringCipher = (String) objectInputStream.readObject();
                    int cipher = Integer.parseInt(stringCipher, 16);

                    String message = (String) objectInputStream.readObject();
                    StringBuffer decrypted = new StringBuffer();

                    for (char c : message.toCharArray()) {
                        if ((int) c < 0x5B && (int) c > 0x40) {
                            //System.out.println("Enc: "+ (int)c + "," + c + ",Cipher: " +  cipher);
                            int enc = (c - cipher - 65);
                            enc = (char) ((floorMod(enc, 26) >= 0 ? floorMod(enc, 26) : floorMod(enc, 26) + 26));
                            enc += 65;
                            decrypted.append((char) enc);
                            //System.out.println("Dec: " + (int)enc + "," + (char)enc);
                            //System.out.println(c);
                        } else {
                            //System.out.println("Enc: "+ (int)c + "," + c + ",Cipher: " +  cipher);
                            int enc = (c - cipher - 97);
                            enc = (char) ((floorMod(enc, 26) >= 0 ? floorMod(enc, 26) : floorMod(enc, 26) + 26));
                            enc += 97;
                            decrypted.append((char) enc);
                            //System.out.println("Dec: " + (int)enc + "," + (char)enc);


                        }
                    }
                    System.out.println("Cipher: " + stringCipher);
                    System.out.println("Plain text: " + decrypted.toString());
                    System.out.println("Encrypted message: " + message);

                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String args[])
    {
        int a = 0x0a;
        Scanner scanner = new Scanner(System. in);
        String inputString;
        boolean activate;
        String msg = "";

        do
        {
            System.out.println("For Receiving write R, for writing a message W");
            inputString = scanner.nextLine();

        }
        while (!inputString.equals("W") && !inputString.equals("R"));

        if(inputString.equals("W"))
        {
            do {
                System.out.println("Please enter your Message (only [A-Z] and [a-z]):");
                msg = scanner.nextLine();
            }
            while (!msg.chars().allMatch(Character::isLetter) || msg.contains(" ") || msg.isEmpty());
        }

        activate = inputString.equals("W");
        ClientMult client = new ClientMult("localhost", 8000, activate, msg);
    }
}
