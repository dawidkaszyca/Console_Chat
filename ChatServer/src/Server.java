import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    ArrayList<PrintWriter> userList;
    PrintWriter printWriter;

    public static void main(String[] args) {
        Server server = new Server();
        server.startServer();
    }

    public void startServer() {
        userList = new ArrayList<>();
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            while (true) {
                Socket socket = serverSocket.accept();
                addNewUser(serverSocket, socket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addNewUser(ServerSocket serverSocket, Socket socket) throws IOException {
        System.out.println("Connected:" + serverSocket);
        printWriter = new PrintWriter(socket.getOutputStream());
        userList.add(printWriter);
        Thread thread = new Thread(new ServerUser(socket));
        thread.start();
    }

    class ServerUser implements Runnable {
        Socket socket;
        BufferedReader bufferedReader;

        public ServerUser(Socket socketUser) {
            try {
                System.out.println("Connected.");
                socket = socketUser;
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void run() {
            String message;
            PrintWriter pw;
            try {
                while ((message = bufferedReader.readLine()) != null) {
                    System.out.println("received >> " + message);
                    Iterator<PrintWriter> it = userList.iterator();
                    while (it.hasNext()) {
                        pw = it.next();
                        pw.println(message);
                        pw.flush();
                    }
                }
            } catch (Exception e) {
            }
        }
    }
}
