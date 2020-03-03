package controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

import javafx.scene.control.TextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

public class PaneController {

    String nick;
    public static final int PORT = 5000;
    public static final String IP = "192.168.1.8";
    BufferedReader bufferedReader;
    PrintWriter printWriter;
    String userName;
    String message;


    class Receiver implements Runnable {

        public void run() {
            String receivedMessage;
            try {
                while ((receivedMessage = bufferedReader.readLine()) != null) {
                    String[] messageAsArray = receivedMessage.split(":");
                    if (!messageAsArray[0].equals(userName)) {
                        System.out.println(receivedMessage);
                        ssField.setText(ssField.getText() + "\n" + receivedMessage);
                    }
                }
            } catch (Exception e) {
            }
        }
    }


    @FXML
    private TextField nickText;

    @FXML
    private VBox Vbox;

    @FXML
    private VBox vBoxChat;

    @FXML
    private TextField messageText;

    @FXML
    private Button sentButton;

    @FXML
    private TextArea ssField;

    @FXML
    void clickButton() {
        nick = nickText.getText();
        if (!nick.isEmpty()) {
            Random random = new Random();
            int number;
            number = random.nextInt(5000 - 1) + 1;
            userName = nick + "" + number;
            try {
                Socket socket = new Socket(IP, PORT);
                ssField.setText("Connected to server");
                printWriter = new PrintWriter(socket.getOutputStream());
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Thread t = new Thread(new Receiver());
                t.start();

            } catch (Exception e) {
                System.out.println("Server is offline");
                System.exit(0);
            }
            Vbox.setVisible(false);
            vBoxChat.setVisible(true);
            sentButton.setVisible(true);
            messageText.setVisible(true);
            ssField.setWrapText(true);
            ssField.setVisible(true);
            ssField.setEditable(false);

        }
    }

    @FXML
    void sendButton() {
        try {
            message = messageText.getText();
        } catch (Exception e) {

        }
        if (!message.trim().isEmpty()) {
            if (message.equals("exit")) {
                printWriter.println(userName + " disconnected");
                printWriter.flush();
                printWriter.close();
                System.exit(0);
            } else {
                printWriter.println(userName + ": " + message);
                printWriter.flush();
                ssField.setText(ssField.getText() + "\n" + "You : " + message);
                messageText.setText("");
            }
        }

    }

    @FXML
    void enterPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            sendButton();
        }

    }
}

