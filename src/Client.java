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
	private MulticastReceiver receiver = null; // receiver multicast (every client receive at the same IP:port )
	
	public Client(String hostname,int portNumber,String multicastGroupAddres, int multicastGroupPort) throws UnknownHostException, IOException {
		this.socket = new Socket(hostname, portNumber);
		this.socketUDP = new DatagramSocket(socket.getLocalPort());
		this.hostname = hostname;
		this.portNumber = portNumber;
		this.receiver = new MulticastReceiver(socket, multicastGroupAddres, multicastGroupPort);
		this.startWorking();
	}
	
	@SuppressWarnings("resource")
	public void startWorking() throws IOException {
		System.out.println("JAVA TCP CLIENT");

		
        try {
        	receiver.start();
            // create socket
            // in & out streams
        	BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        	Thread sender = new Thread() {
        		public void run() {
        			while(true) {
        				try {
	        	            System.out.println(in.readLine());
						} catch (IOException e) {
							e.printStackTrace();
						}
                	}
        		}
        	};
        	
        	Scanner reader = new Scanner(System.in);  // Reading from System.in
			PrintWriter out;
			out = new PrintWriter(socket.getOutputStream(), true);
        	Thread listener = new Thread() {
        		public void run() {
        			try {
	        			while(true) {
	                        // send msg, read response
	        				String message = reader.nextLine();
	        				if(message.endsWith(" U")) { //send by UDP channel
	        		        	InetAddress address = InetAddress.getByName(hostname); 
	        		        	byte[] sendBuffer = message.getBytes();
	        		        	DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, portNumber);
								socketUDP.send(sendPacket);
	        				} else if(message.endsWith(" M")) { // send multicast mesage
	        		            InetAddress group = InetAddress.getByName(receiver.getMulticastGroupAddres());
	        		            byte[] sendBuffer = message.getBytes();
	        		            DatagramPacket packet = new DatagramPacket(sendBuffer, sendBuffer.length, group, receiver.getMulticastGroupPort());
	        		            socketUDP.send(packet);
	        		        }else {
			                    out.println(message);
	        				}
	                	}
        			} catch (IOException e) {
						e.printStackTrace();
					}
        		}
        	};
        	
        	sender.start();
        	listener.start();
        } catch (Exception e) {
            e.printStackTrace();
        } 
	}
	
	
//	@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@2
	
    public static void main(String[] args) throws IOException {

        @SuppressWarnings("unused")
		Client javaClient = new Client("localhost", 10001, "230.1.1.1",8888);
    }

}
