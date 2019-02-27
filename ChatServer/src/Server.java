import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
public class Server {

	ArrayList<PrintWriter> userList;
	PrintWriter printWritter;
	
	public static void main(String[] args) {
		Server s=new Server();
		s.startServer();	
	}

	public void startServer() { //³¹czenie u¿ytkowników do serwera
		userList=new ArrayList<PrintWriter>();
		BufferedReader bufferedReader;
		Random r=new Random();
		try {
			ServerSocket serverSocket= new ServerSocket(5000);//nr portu 
			
			while(true) {
				Socket socket = serverSocket.accept();		//akceptujemy po³aczenia do portu 5000
				System.out.println("Connected:" + serverSocket);
				printWritter = new PrintWriter(socket.getOutputStream()); 

				userList.add(printWritter); //ka¿dy klient jest osobnym obiektem do wysy³ania wiadomosci
				Thread t =new Thread(new ServerUser(socket));
				t.start();
			}
		}
		catch (Exception e){
			e.printStackTrace();	
		}
	}
	
	class ServerUser implements Runnable { //odbieranie i wysy³anie wiadomoœci

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
					System.out.println("received >> " + message);
					Iterator<PrintWriter> it=userList.iterator();
					
					while(it.hasNext()) {
						pw = (PrintWriter)it.next();
						pw.println(message);
						pw.flush(); 	//wys³anie wiadomoœci
					}
				}
			}
			catch(Exception e) {
			}
		}
	}
}
