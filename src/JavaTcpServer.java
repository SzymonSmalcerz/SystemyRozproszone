
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JavaTcpServer extends Thread{
	
	private ServerSocket serverSocket = null;
	private List<Socket> clientsSockets = new ArrayList<>();
	
	public JavaTcpServer(int portNumber) throws IOException {
		this.serverSocket = new ServerSocket(portNumber);
	}
	
	@Override
	public void run() {
		
		try {

            while(true){
                // accept client
            	this.addClient(this.serverSocket.accept());
            	this.checkForClientsMessages(); 
            	System.out.println("IM HERREE");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            if (serverSocket != null){
                try {
					serverSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
		
	}

	private void addClient(Socket clientSocket) {
		this.clientsSockets.add(clientSocket);
	}
	
	private void checkForClientsMessages() throws IOException {
		
		
        Iterator<Socket> clientsSocketsIterator = this.clientsSockets.iterator();
        
        while(clientsSocketsIterator.hasNext()) {
        	System.out.println(clientsSockets.size());
        	Socket clientSocket = clientsSocketsIterator.next();
            System.out.println("client connected");

            // in & out streams
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // read msg, send response
            String msg = in.readLine();
            System.out.println("received msg: " + msg);
            out.println("Pong Java Tcp");
        	
        }
	}

}
