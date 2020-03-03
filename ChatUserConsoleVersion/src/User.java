import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class User {

    public static final int PORT = 5000;
    public static final String IP = "192.168.1.8";

    BufferedReader bufferedReader;
    String userName;

    public static void main(String[] args) {
        User user = new User();
        user.startUser();
    }

    public void startUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Username");
        userName = scanner.nextLine();
        String message;
        Random random = new Random();
        int randomNumber = random.nextInt(5000 - 1) + 1;
        this.userName = this.userName + randomNumber;
        try (Socket socket = new Socket(IP, PORT)) {
            System.out.println("Connected to " + socket);
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Thread thread = new Thread(new Receiver());
            thread.start();
            sendMessage(scanner, socket, printWriter);
            return;
        } catch (Exception e) {
        }
    }

    private void sendMessage(Scanner scanner, Socket socket, PrintWriter printWriter) throws IOException {
        String message ="";
        while (!message.equalsIgnoreCase("q")) {
            System.out.print(">> ");
            message = scanner.nextLine();
            if (!message.equalsIgnoreCase("q")) {
                printWriter.println(this.userName + " : " + message);
                printWriter.flush();
            } else {
                printWriter.println(this.userName + " disconnected");
                printWriter.flush();
                printWriter.close();
                scanner.close();
                socket.close();
            }
        }
    }

    class Receiver implements Runnable {
        @Override
        public void run() {
            String message;
            try {
                while ((message = bufferedReader.readLine()) != null) {
                    String[] receivedMessage = message.split(":");
                    if (!receivedMessage[0].equals(userName)) {
                        System.out.println(message);
                        System.out.print(">> ");
                    }
                }
            } catch (Exception e) {
                System.out.println("Connection closed.");
            }
        }
    }
}
