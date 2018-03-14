import java.io.IOException;

public class App {
	
	
	public static void main(String[] args) {
		try {
			JavaTcpServer tcpServ = new JavaTcpServer(12346);
			tcpServ.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
