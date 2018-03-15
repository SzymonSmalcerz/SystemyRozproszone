import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;

public class MulticastReceiver extends Thread 
{	
	private int multicastGroupPort;
	private String multicastGroupAddres;
	private Socket client_socket;
	private MulticastSocket socket; 
	private InetAddress group; 
	private byte[] buf;
 
	public MulticastReceiver(Socket socket, String multicastGroupAddres, int multicastGroupPort)
	{
		this.client_socket = socket; // potrzebne do sprawdzania zeby nadawca nie dostawal wiadomosci
		this.multicastGroupAddres = multicastGroupAddres;
		this.multicastGroupPort = multicastGroupPort;
		this.buf = new byte[1024];
	}
	
	public int getMulticastGroupPort() {
		return this.multicastGroupPort;
	}
	
	public String getMulticastGroupAddres() {
		return this.multicastGroupAddres;
	}
	
	
	public void run() 
	{
		try
		{	
			socket = new MulticastSocket(multicastGroupPort); // nasze gniazdo multicast (port multicast 8888)
			group = InetAddress.getByName(multicastGroupAddres); // adres multicast
			socket.joinGroup(group); // dolacz klienta do grupy multicast 
			
			while(true) 
			{	
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);
				String received = new String(packet.getData(), 0, packet.getLength()); 
				System.out.println("receiver");
//				received = received.substring(0, received.length() - 2); // ucina " M" (tutaj dopiero bo wczesniej ".quit")
				if(packet.getPort() != client_socket.getLocalPort()) {
					// wypisz wszystkim oprocz nadawcy
					System.out.println(packet.getPort() + ">> " + received + " (MULTICAST)");
				}
			}
		}
		catch(Exception e)
		{
			socket.close();
			e.printStackTrace();
		}
	}
}