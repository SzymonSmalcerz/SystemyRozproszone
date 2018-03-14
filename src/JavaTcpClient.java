import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class JavaTcpClient {
	
	private final Socket socket;
	public JavaTcpClient(String hostname,int portNumber,String name) throws UnknownHostException, IOException {
		this.socket = new Socket(hostname, portNumber);
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
	        	            System.out.println("received response: " + in.readLine());
						} catch (IOException e) {
							// TODO Auto-generated catch block
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
        			while(true) {
                        // send msg, read response
        				System.out.println("enter your input here");
        				String str = reader.next();
        				System.out.println(str);
	                    out.println(str);
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

        JavaTcpClient javaTcpClient = new JavaTcpClient("localhost", 12346, "xd");
    }

}
