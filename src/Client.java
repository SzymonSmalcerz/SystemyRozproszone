import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	
	private final Socket socket;
	private DatagramSocket socketUDP = null; // UDP channel
	private final String hostname;
	private final int portNumber;
	
	public Client(String hostname,int portNumber,String name) throws UnknownHostException, IOException {
		this.socket = new Socket(hostname, portNumber);
		this.socketUDP = new DatagramSocket(socket.getLocalPort());
		this.hostname = hostname;
		this.portNumber = portNumber;
		this.startWorking();
	}
	
	@SuppressWarnings("resource")
	public void startWorking() throws IOException {
		System.out.println("JAVA TCP CLIENT");

		
        try {
            // create socket
            // in & out streams
        	BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        	Thread sender = new Thread() {
        		public void run() {
        			while(true) {
        				try {
	        	            System.out.println(in.readLine());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                	}
        		}
        	};
        	
//        	Scanner reader = new Scanner(System.in).useDelimiter("\\n");
        	Scanner reader = new Scanner(System.in);  // Reading from System.in
			PrintWriter out;
			out = new PrintWriter(socket.getOutputStream(), true);
        	Thread listener = new Thread() {
        		public void run() {
        			try {
	        			while(true) {
	                        // send msg, read response
	        				String message = reader.nextLine();
	        				System.out.println("ME >> " + message);
	        				if(message.endsWith(" U")) // jezeli chcemy wyslac przez kanal UDP
	        				{
	        		        	InetAddress address = InetAddress.getByName(hostname); 
	        		        	byte[] sendBuffer = message.getBytes();
	        		        	DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, portNumber);
	        		            
									socketUDP.send(sendPacket);
								 // wyslij na localhost port 12345 - tam slucha nasz serwer
	        				}else {
			                    out.println(message);
	        				}
	                	}
        			} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        		}
        	};
        	
        	sender.start();
        	listener.start();
        } catch (Exception e) {
            e.printStackTrace();
        } 
//        finally {
//            if (socket != null){
//                socket.close();
//            }
//        }
	}

    public static void main(String[] args) throws IOException {

        @SuppressWarnings("unused")
		Client javaClient = new Client("localhost", 12348, "xd");
    }

}
