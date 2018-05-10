package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Controller {

	private ObservableList<String> itemsMotsLocal = FXCollections.observableArrayList();
	private ObservableList<String> itemsJoueurs = FXCollections.observableArrayList();
	private ObservableList<String> itemsChat = FXCollections.observableArrayList();

	private String mot = "";

	@FXML
	private Button A1;
	@FXML
	private Button A2;
	@FXML
	private Button A3;
	@FXML
	private Button A4;
	@FXML
	private Button B1;
	@FXML
	private Button B2;
	@FXML
	private Button B3;
	@FXML
	private Button B4;
	@FXML
	private Button C1;
	@FXML
	private Button C2;
	@FXML
	private Button C3;
	@FXML
	private Button C4;
	@FXML
	private Button D1;
	@FXML
	private Button D2;
	@FXML
	private Button D3;
	@FXML
	private Button D4;

	@FXML
	private TextField nom; 

	@FXML
	private Button connexion;

	@FXML
	private Text erreurConnexion;

	@FXML
	private Text information;

	@FXML
	private ListView<String> listeMots;

	@FXML
	private ListView<String> listeJoueurs;

	@FXML
	private Button quitter;

	@FXML
	private Text messageErreur;

	@FXML
	private Text motChoisi;

	@FXML
	private Text MonScore;

	@FXML
	private Text Moi;

	@FXML
	private Button envoyer;

	@FXML
	private Button annuler;

	@FXML
	private GridPane grille;

	@FXML
	ListView<String> listeMessages;

	@FXML
	private Label time;

	@FXML
	private Label tour;


	@FXML
	private Button diffuser;

	@FXML
	private TextField MessageAenvoyer;


	private Client client;
	private Socket socket;
	private String trajectoire="";

	int score;
	static int globalMinutes= -1;
	static int globalSecondes= -1;

	/**-----------------------------------------------------------------------------------------------------------------------------------------**/

	@FXML
	public void connexion(ActionEvent e) {
		if (nom.getText().equals("")) {
			erreurConnexion.setText("* saisisez votre nom -_-");
		} else {
			Moi.setText(nom.getText());
			connexionBis();

			connexion.setDisable(true);
			diffuser.setDisable(false);
			for (Node bouton : grille.getChildren()) {
				((Button)bouton).setDisable(false);
			}
			envoyer.setDisable(false);
			//quitter.setDisable(false);
			annuler.setDisable(false);
		}
	}

	@FXML
	public void envoyerMot(ActionEvent e) {

		if (Moi.getText().equals("")) {
			messageErreur.setText("* Connectez vous d'abord -_-");
		} else {
			if (mot.length() < 3) {
				messageErreur.setText("* La taille du mot selectionné est < 3");
			} else {
				client.sendToServer("TROUVE/"+mot+"/"+trajectoire);
				System.out.println("trajectoire :"+trajectoire);
				for (Node bouton : grille.getChildren()) {
					((Button)bouton).setDisable(false);
				}
				//				messageErreur.setText("");
				motChoisi.setText("");
				mot="";
				trajectoire="";
			}
		}
	}

	/**
	 * methode executé lors du clique sur l'une des lettres
	 *  ( recuperer le mot selectionné et sa trajectoire)
	 * @param e
	 */
	@FXML
	public void writeText(ActionEvent e) {
		messageErreur.setText("");
		Button btn = (Button) e.getSource();
		String btnText = ((Button) e.getSource()).getText();
		btn.disableProperty().set(true);
		mot = mot + btnText;
		trajectoire += btn.getId();
		motChoisi.setText(mot);
		System.out.println(mot);
	}

	@FXML
	public void annulerMot(ActionEvent e) {
		mot = "";
		motChoisi.setText("");
		trajectoire ="";
		for (Node bouton : grille.getChildren()) {
			((Button)bouton).setDisable(false);
		}
	}

	@FXML
	public void quitterPartie() {
		client.sendToServer("SORT/"+Moi.getText());
		enleverUser(Moi.getText());
	}

	
	@FXML
	public void DiffuserMessage() {
		if( ! MessageAenvoyer.getText().substring(0, 1).equals("@")) {
			itemsChat.add("Moi : " + MessageAenvoyer.getText());
			client.sendToServer("ENVOI/"+nom.getText()+" : "+MessageAenvoyer.getText());
		}else {
			String[] msg = MessageAenvoyer.getText().split(":");
			client.sendToServer("PENVOI/"+msg[0]+"/"+msg[1]);
		}

		MessageAenvoyer.setText("");
	}

		/**-----------------------------------------------------------------------------------------------------------------------------------------**/

	public void init() {
		/**
		 * à faire en cas d'erreur listeMots null **** NE PAS SUPPRIMER import
		 * javafx.application.Platform;
		 * Platform.runLater(()->listeMots.setItems(items));
		 */
		listeMots.setItems(itemsMotsLocal);
		listeJoueurs.setItems(itemsJoueurs);
		listeMessages.setItems(itemsChat);

		diffuser.setDisable(true);
		for (Node bouton : grille.getChildren()) {
			((Button)bouton).setDisable(true);
		}
		envoyer.setDisable(true);
		//quitter.setDisable(true);
		annuler.setDisable(true);

		tour.setText("0");


	}


	/**
	 * sert a afficher les infos pratiques comme debut d'une session, d'un tour,..
	 * (affiché en bleu à droite de la fenetre)
	 */
	String msg="";
	public void afficherTexte(String s) {
		msg+=s;
		information.setText(msg + " :)");
	}

	public void motValide(String s) {
		score+=s.length();
		itemsMotsLocal.add(s + "\t\t" + s.length() + " Pts");

		MonScore.setText(score + " Pts");
		mot = "";
	}

	public void motInvalide(String s) {

		messageErreur.setText(s);
	}

	public void connexionBis() {
		try {
			socket = new Socket("127.0.0.1", 2019);
			client = new Client(socket);
			client.sendToServer("CONNEXION/" + nom.getText());

			try {
				Receiver r =  new Receiver(new BufferedReader(new InputStreamReader(socket.getInputStream())), this);
				r.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addJoueur(String j) {
		itemsJoueurs.add(j);
	}

	public void afficherScoresFinaux(String s) {

		String[] scores = s.split("\\*");

		itemsJoueurs.add("Tour n " + scores[0]);
		int i = 1;
		while (i < scores.length) {
			itemsJoueurs.add(scores[i] + "\t\t" + scores[i + 1] + " Pts");
			i = i + 2;
		}
	}

	public void enleverUser(String s) {
		for (int i = 0 ; i< itemsJoueurs.size() ; i++ )
			if( itemsJoueurs.get(i).substring(0, s.length()).equals(s))
				itemsJoueurs.remove(i);
	}



	public void afficheTirage(String tirage) {

		for (Node bouton : grille.getChildren()) {
			System.out.println("le car "+String.valueOf(tirage.charAt(0)));
			((Button)bouton).setText(String.valueOf(tirage.charAt(0)));
			tirage = tirage.substring(1, tirage.length());
		}
	}

	public void messagePrive(String s) {
		afficherTexte("\n#"+s);
	}


	public void ajouterListeMessage(String s) {
		itemsChat.add(s);
	}
	

	public void afficheTirageScores(String tirage,String score) {
		//		String[] scores;
		//		//String[] tirages = tirage.split("");
		//		if(! score.equals(""))
		//			scores = score.split("");
		//
		//		for (Node bouton : grille.getChildren())
		//			((Button)bouton).setText(String.valueOf(tirage.charAt(0)));
		//		tirage = tirage.substring(1, tirage.length()-1);
		//		{
		//
		//
		//		}
	}


	public void ReinitialiserTime(){
		if( timer!=null )
			timer.cancel();
		else
			timer = new Timer();

		time.setText("00 : 00");
		globalSecondes= 00;
		globalMinutes= 00;

		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				Platform.runLater(new Runnable() {				
					@Override
					public void run() {
						String[] currentTime = time.getText().split(" : ");
						int minutes= Integer.parseInt(currentTime[0]);
						int seconds= Integer.parseInt(currentTime[1]);
						seconds++;
						if( seconds == 59 ) {
							minutes++;
							seconds=0;
						}

						time.setText((minutes<10?"0":"")+minutes+" : "+(seconds<10?"0":"")+seconds);						
					}
				});
			}
		}, 0, 1000);
	}

	public Timer timer=null;
	
	
	int nbtour = 0 ;

	public void afficheTour() {
		nbtour++;
		tour.setText(Integer.toString(nbtour));
	}
}

