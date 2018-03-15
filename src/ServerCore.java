
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ServerCore {

	
	private ServerSocket serverSocket = null;
	private List<TCPServerSender> clientsThreadsTCP = new ArrayList<>();
	private UDPServerHandler handlerUDP;
	private TCPServerListener listenerTCP;
	
	public ServerCore(int portNumber) throws IOException {
		this.serverSocket = new ServerSocket(portNumber);
		this.listenerTCP = new TCPServerListener(this);
		this.handlerUDP = new UDPServerHandler(this, new DatagramSocket(portNumber));
		
		this.handlerUDP.start(); // start listening for UDP
		this.listenerTCP.start(); // start listening for TCP
		System.out.println("Server launched on port: " + portNumber);
	}

	
	public synchronized ServerSocket getServerSocket() {
		return this.serverSocket;
	}
	
	
	public synchronized void close() {
		if (this.serverSocket != null){
            try {
				this.serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}
	
	public synchronized void addClient(Socket clientSocket) throws IOException {
		TCPServerSender newClient = new TCPServerSender(this,clientSocket);
        System.out.println("client connected");
		this.clientsThreadsTCP.add(newClient);
		newClient.start();
	}


	public void sendMessage(String msg, String ID) {
		
		System.out.println(clientsThreadsTCP.size());
		Iterator<TCPServerSender> clientsThreadsIteartor = this.clientsThreadsTCP.iterator();
		
		while(clientsThreadsIteartor.hasNext()) {
			TCPServerSender clientThread = clientsThreadsIteartor.next();
			System.out.println(clientThread.getID() + " vs " + ID);
			if(!clientThread.getID().equals(ID)) {
				clientThread.sendMessage(msg, ID);
			}
		}
		
	}
	
	



	public synchronized void removeClient(String ID) throws InterruptedException {
		Iterator<TCPServerSender> clientsThreadsIteartor = this.clientsThreadsTCP.iterator();
		
		while(clientsThreadsIteartor.hasNext()) {
			TCPServerSender clientThread = clientsThreadsIteartor.next();
			if(clientThread.getID() == ID) {
				clientsThreadsIteartor.remove();
			}
		}
		
		
		
	}
	
	
	
	//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
	
	

	
	public static void main(String[] args) {
		try {
			@SuppressWarnings("unused")
			ServerCore tcpServerCore = new ServerCore(12348);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
