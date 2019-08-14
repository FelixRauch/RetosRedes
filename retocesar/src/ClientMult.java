

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import static java.lang.Math.floorMod;

public class ClientMult
{
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    /***
     *
     * @param address The servers ip address
     * @param port The port to use for the servef conncetion
     * @param active Wether the client will receive or write a message
     * @param msg The message to send
     *
     * This constructor tries to estblish a connection to the server and encrypts or decrypts a message with the cesar
     * cipher and sends it to or receives it from the server.
     *
     */
    private ClientMult(String address, int port, boolean active /*source/receiver*/, String msg)
    {
        Socket socket = null;
        try
        {
            socket = new Socket(address, port);
            System.out.println("Connected");

            objectInputStream  = new ObjectInputStream(socket.getInputStream());

            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch(IOException i)
        {
            System.out.println(i);
        }


        if(active) {
            try {
                //abcedfghijklmnopqrstuvwxyz
                objectOutputStream.writeObject("request");
                String stringCipher = (String) objectInputStream.readObject();
                int cipher = Integer.parseInt(stringCipher, 16);

                String encrypted_msg = cesarCipher(cipher, msg);
                objectOutputStream.writeObject(encrypted_msg);
                System.out.println("Cipher: " + stringCipher);
                System.out.println("Plain text: " + msg);
                System.out.println("Encrypted message: " + encrypted_msg);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            try {
                objectInputStream.close();
                objectOutputStream.close();
                socket.close();
            } catch (Exception i) {
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
                    String decrypted = cesarCipher((-1)*cipher, message);
                    System.out.println("Cipher: " + stringCipher);
                    System.out.println("Plain text: " + decrypted);
                    System.out.println("Encrypted message: " + message);

                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    /***
     *
     * @param args
     *
     * Takes input form the client such as wether it wants to be a receiver or write a message and the message to send
     * to the other clients
     *
     */

    public static void main(String args[])
    {
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


    /***
     *
     * @param cipher the cesat cipher used for encryption/decryption
     * @param msg the message to encrypt/decrypt
     * @return the resulting string
     *
     * This function is used to encrypt or decrypt a message accoring the the cesar cipher cryptographic algorithm.
     * It is designed to work with Strings containing only the letters [A-Z] and [a-z]
     * */

    private String cesarCipher(int cipher, String msg)
    {

        StringBuilder encrypted_msg = new StringBuilder();
        for (char c : msg.toCharArray()) {
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
        return encrypted_msg.toString();
    }
}
