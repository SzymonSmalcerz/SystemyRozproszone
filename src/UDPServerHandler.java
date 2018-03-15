import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

public class UDPServerHandler extends Thread{
	
	private DatagramSocket socket = null;
	private ServerCore server = null; // potrzebne do handlera
	private byte[] receiveBuffer = null; // bufor do odbierania danych w UDP

	public UDPServerHandler(ServerCore server, DatagramSocket socket)
	{  
		super();
		this.socket = socket;
		this.server = server;
		receiveBuffer = new byte[1024];
	}
	
	@Override
	public void run()
	{  
		while(true) 
		{
			try
			{
				Arrays.fill(receiveBuffer, (byte)0);
		        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
		        socket.receive(receivePacket); // czekamy az przyjdzie jakas wiadomosc
		        String msg = new String(receivePacket.getData(), 0, receivePacket.getLength() - 2); // uciac " U"
		        
		        server.sendMessage(msg + " (UDP)",receivePacket.getAddress() + ":" + receivePacket.getPort()); // oznaczenie ze przyszlo z UDP
		        
			}
			catch(Exception e)
			{
				close();
				e.printStackTrace();
			}
        }
	}
	
	public void close()
	{
		socket.close();
	}
}
