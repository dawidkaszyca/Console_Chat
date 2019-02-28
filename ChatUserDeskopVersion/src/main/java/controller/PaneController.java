package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

import com.sun.media.jfxmediaimpl.platform.Platform;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class PaneController {
	
	String nick;
	
	public static final int PORT = 5000;		 //port servera
	public static final String IP = "192.168.1.7"; //ip server

	BufferedReader bufferedReader;
	PrintWriter printWritter;
	Socket socket;
	String userName;
	String message;
	
	
	

	class Receiver implements Runnable {

		public void run() {
			String message;
			try {
				while((message=bufferedReader.readLine())!=null) {
					String subString[]=message.split(":");
					if(!subString[0].equals(userName)) {
						ssField.setText(ssField.getText()+"\n"+message);
					}
				}
			}
			catch(Exception e) {
			}
		}
	}

	
    @FXML
    private TextField nickText;

    @FXML
    private Button nickButton;
    
    @FXML
    private VBox Vbox;
    
    @FXML
    private Pane pane;
    
    @FXML
    private VBox vBoxChat;

    @FXML
    private TextField messageText;

    @FXML
    private Button sentButton;
    
    @FXML
    private TextArea ssField;

    Thread t;
    
    @FXML
    void clickbutton(ActionEvent event) {
    	nick = nickText.getText();
    	if(!nick.isEmpty()) {
    		
    		Random r=new Random();
    		int number;
    		number=r.nextInt(5000-1)+1;
    		userName=nick+""+number;
    		try {
    			Socket socket = new Socket(IP,PORT); //³aczymy siê z danym ip,portem
    			ssField.setText("Connected to server : "+socket);
    			printWritter = new PrintWriter(socket.getOutputStream());
    			bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
    			
    			Thread t=new Thread(new Receiver());
    			t.start();
    			
    		}
    		catch(Exception e){	
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
    void sentbutton() {
    	try {
    	message = messageText.getText();
    	}
    	catch(Exception e) {
    		
    	}
    	if(!message.isEmpty()) {
    		if(message.equals("exit")) {
    			printWritter.println(userName+" disconnected");
				printWritter.flush();
				printWritter.close();;
				System.exit(0);
				
    		}
    		else {
				printWritter.println(userName+ ":" +message);
				printWritter.flush();
				ssField.setText(ssField.getText()+"\n"+"You : "+message);
	    		
	    		messageText.setText("");
    	}
    	}
    	
    }
    @FXML
    void enterPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
        	sentbutton();
    	 }
    
    }
}

