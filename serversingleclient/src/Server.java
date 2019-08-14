
// A Java program for a Server
import java.net.*;
import java.io.*;
import java.util.Random;

import static java.lang.Math.floorMod;

public class Server
{
	//initialize socket and input stream
	private Socket          socket   = null;
	private ServerSocket    server   = null;
	private Socket clientSocket;
	private ObjectOutputStream objectOutputStream;
	private ObjectInputStream  objectInputStream;
	private Random random;

	// constructor with port
	public Server(int port)
	{



		// starts server and waits for a connection
		try
		{
			server = new ServerSocket(port);
			while(true) {
				socket = server.accept();
				System.out.println("Client connected");

				// takes input from the client socket
				objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
				objectInputStream = new ObjectInputStream(socket.getInputStream());
				clientSocket = socket;
				random = new Random();

				String line = "";

				// reads message from client until "Over" is sent
				try {
						line = (String) objectInputStream.readObject();

						int cipher = random.nextInt(20) + 1;
						//cipher = 14;

						StringBuffer encrypted = new StringBuffer();
						for (char c : line.toCharArray()) {
							if ((int) c < 0x5B && (int) c > 0x40) {
								//System.out.println("Enc: "+ (int)c + "," + c + ",Cipher: " +  cipher);
								int enc = (c + cipher - 65);
								enc = (char) ((floorMod(enc, 26) >= 0 ? floorMod(enc, 26) : floorMod(enc, 26) + 26));
								enc += 65;
								//System.out.println("Dec: " + (int)enc + "," + (char)enc);
								encrypted.append((char) (enc));
								System.out.println(c);

							} else {
								//System.out.println("Enc: "+ (int)c + "," + c + ",Cipher: " +  cipher);
								int enc = (c + cipher - 97);
								enc = (char) ((floorMod(enc, 26) >= 0 ? floorMod(enc, 26) : floorMod(enc, 26) + 26));
								enc += 97;
								//System.out.println("Dec: " + (int)enc + "," + (char)enc);
								encrypted.append((char) enc);
								System.out.println((int) enc + "," + enc);
							}
						}
						objectOutputStream.writeObject(encrypted.toString());
						objectOutputStream.writeObject(cipher);
						System.out.println(cipher);
						System.out.println(line);
						System.out.println(encrypted.toString());
				} catch (Exception e) {
					System.out.println("Error: " + e.getMessage());
				}
				System.out.println("Closing connection");

				// close connection
				socket.close();
				objectInputStream.close();
			}
		}
		catch(IOException i)
		{
			System.out.println(i);
		}
	}

	public static void main(String args[])
	{
		Server server = new Server(8000);
	}
}
