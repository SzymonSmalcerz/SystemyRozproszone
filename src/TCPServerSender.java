import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPServerSender extends Thread{
	
	private ServerCore server;
	@SuppressWarnings("unused")
	private Socket clientSocket;
	private PrintWriter out;
    private BufferedReader in;
    private final String ID;
	
	public TCPServerSender(ServerCore server,Socket clientSocket) throws IOException {
		this.server = server;
		this.clientSocket = clientSocket;
		this.ID = clientSocket.getInetAddress() + ":" + clientSocket.getPort();
		 // in & out streams
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

	}
	
	public void run() {
		try {
			while(true) {
				this.checkForClientMessages();
			}
		} catch (IOException e) {
			this.server.sendMessage("user " + this.ID + " log out", "chat");
			try {
				
				this.server.removeClient(this.ID);
				return;
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
//			e.printStackTrace();
		}
		
	}
	
	private void checkForClientMessages() throws IOException {
			
			// read msg, send response
            String msg = in.readLine();
            if(msg == null) {
            	this.close();
            	return;
            }
            
            this.server.sendMessage(msg,ID);
            System.out.println("received msg: " + msg);
	}
	
	public String getID() {
		return this.ID;
	}
	
	public synchronized void sendMessage(String msg, String ID) {
		this.out.println(ID + " >> " + msg);
	}
	
	private void close()
	{
		try {
			this.in.close(); // wystarczy in.close() bo to zamyka i socketa i inne streamy powstale od niego
		} 
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
