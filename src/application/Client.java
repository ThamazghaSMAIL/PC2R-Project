package application;

//Assumptions made (based on what was mentioned by lecturer in class)
//Client only has a maximum of two chances to answer a question			

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.Normalizer;

public class Client{

	private  Socket clientSocket;
	private DataOutputStream outToServer;
	private BufferedReader inFromServer ;
	private String rep;
	public Client(Socket ecouteSocket) {
		this.clientSocket = ecouteSocket;
		try {
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void sendToServer(String msg) {
		if((msg != null) && !(msg.equals(""))) {
			System.out.println(msg);
			try {
				msg = Normalizer.normalize(msg, Normalizer.Form.NFD);
				msg = msg.replaceAll("[^\\p{ASCII}]", "");
				msg+='\n';
				outToServer.write(msg.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	} 
	public void receive(){
		while(true){
			try {
				rep = inFromServer.readLine();
				System.out.println("S->C "+rep);
				
				Controller c = new Controller();
				String [] texteRecu= rep.split("/");
				
//				switch (texteRecu[0]) {
//				case "BIENVENU" :
//					c.afficherTexte("Bienvenue");
//					
//				case "CONNECTE":
//					c.afficherTexte("Vous etes connecte aux autres joueurs");
//				case "DECONNEXION" :
//					c.afficherTexte("Vous etes deconnecte");
//				case "SESSION" :
//					c.afficherTexte("Debut d’une session");
//				case "VAINQUEUR" :
//					c.afficherTexte("Fin de la session");
//					c.afficherScoresFinaux(texteRecu[1]);
//				case "TOUR" :
//					c.afficherTexte("nouveau tour");
//				case "TROUVE" :
//					
//					
//				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}


	public static void main(String[] args) throws Exception{
		System.out.println("yes");
		Socket s = new Socket("132.227.112.132",2019);
		Client c = new Client(s);
		c.sendToServer("CONNEXION/MEZGHA");
		new Thread(()->c.receive()).start();
	}

}