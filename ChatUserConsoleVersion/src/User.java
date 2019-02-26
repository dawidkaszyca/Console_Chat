import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class User {

	public static final int PORT = 5000;		 //port servera
	public static final String IP = "127.0.0.1"; //ip server
	
	BufferedReader bufferedReader;
	String userName;
	
	public static void main(String[] args) {
		User u=new User();
        u.startUser();
	}

	public void startUser() {
		Scanner sc=new Scanner(System.in);
		System.out.println("State your name:");
		userName=sc.nextLine();
		String message;
		try {
			Socket socket = new Socket(IP,PORT); //³aczymy siê z danym ip,portem
			System.out.println("Connected to " + socket);
			
			PrintWriter printWritter = new PrintWriter(socket.getOutputStream());
			bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			Thread t=new Thread(new Receiver());
			t.start();
			
			while(true) {
				System.out.print(">> ");
				message=sc.nextLine();
				if(!message.equalsIgnoreCase("q")){
					printWritter.println(userName+ ":" +message);
					printWritter.flush();
				}
				else {
					printWritter.println(userName+ "disconnected");
					printWritter.flush();
					printWritter.close();
					sc.close();
					socket.close();
				}

			}
		}
		catch(Exception e){
			
		}
	}

	class Receiver implements Runnable {

		@Override
		public void run() {
			String message;
			try {
				while((message=bufferedReader.readLine())!=null) {
					String subString[]=message.split(":");
					if(!subString[0].equals(userName)) {
						System.out.println(message);
						System.out.print(">> ");
					}
				}
			}
			catch(Exception e) {
				System.out.println("Connection closed.");
			}
			
		}
		
	}
}
