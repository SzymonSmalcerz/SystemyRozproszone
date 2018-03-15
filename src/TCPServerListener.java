import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;

public class TCPServerListener extends Thread{
	
	private ServerCore tcpServer;
	
	public TCPServerListener(ServerCore tcpServerCore) {
		this.tcpServer = tcpServerCore;
	}
	
	public void run() {
		
		ServerSocket serverSocket = this.tcpServer.getServerSocket();
		try {

            while(true){
                // accept client
            	this.tcpServer.addClient(serverSocket.accept());
            }
        } catch (SocketException e) {
        	System.out.println("user log out");
        } catch (IOException e) {
        	e.printStackTrace();
      	} finally{
            this.tcpServer.close();
        }
		
	}
}
