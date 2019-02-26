import java.util.ArrayList;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
public class Server {

	ArrayList userList;
	PrintWriter printWritter;
	
	public static void main(String[] args) {
	

	}

	public void startServer() {
		userList=new ArrayList();
		
		try {
			ServerSocket serverSocket= new  ServerSocket(5000); //nr portu 
			
			while(true) {
				Socket socket = serverSocket.accept();		//akceptujemy po³aczenia do portu 5000
				System.out.println("Connected:" + serverSocket);
				printWritter = new PrintWriter(socket.getOutputStream()); 
				userList.add(printWritter); //ka¿dy klient jest osobnym obiektem do wysy³ania wiadomosci
				
			}
		}
		catch (Exception e){
			e.printStackTrace();	
		}
		
		
		
	}
	
	class ServerUser implements Runnable {

		Socket socket;
		BufferedReader bufferedReader; // odczytuje wiadomoœci danego u¿ytkownika
		
		public ServerUser(Socket socketUser ) {
			try {
				System.out.println("Connected.");
				socket = socketUser;
				bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			String message;
			PrintWriter pw = null;
			try {
				while((message = bufferedReader.readLine()) != null) {
					System.out.println("received >> ");
					Iterator it=userList.iterator();
					
					
				}
			}
			catch(Exception e) {
			}
		}
		
	}
}
