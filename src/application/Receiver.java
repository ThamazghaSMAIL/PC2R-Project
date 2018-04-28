package application;

import java.io.BufferedReader;
import java.io.IOException;

import javafx.application.Platform;

public class Receiver extends Thread {
	// connexion au server
	private BufferedReader in;
	private Controller controller;

	public Receiver(BufferedReader in,Controller controller) {
		this.in = in;
		this.controller = controller;
	}

	@Override
	public void run() {
		super.run();

		while (true) {
			try {
				String rep = in.readLine();
				String[] repSplited = rep.split("/");
				String cmd = repSplited[0];
				if(cmd.equals("BIENVENUE")) {
					controller.afficherTexte(repSplited[0]+" "+repSplited[1]);
					//controller.afficheTirageScores(repSplited[1],repSplited[2]);
				}
				if(cmd.equals("CONNECTE")) {
					Platform.runLater(()->controller.afficherTexte("\nle joueur "+repSplited[1]+" vous a rejoint"));//ok

					Platform.runLater(()->controller.addJoueur(repSplited[1]));
				}
				if(cmd.equals("DECONNEXION")) {
					Platform.runLater(()->controller.afficherTexte("\nLe user "+repSplited[1]+" a quitté la partie"));
				}
				if(cmd.equals("SESSION")) {
					Platform.runLater(()->controller.afficherTexte("\nDebut d’une session"));
				}
				if(cmd.equals("VAINQUEUR")) { 
					Platform.runLater(()->controller.afficherTexte("Fin de la session"));
					Platform.runLater(()->controller.afficherScoresFinaux(repSplited[1]));
				}
				if(cmd.equals("TOUR")) {
					controller.afficherTexte("\nnouveau tour");
					Platform.runLater(()->controller.afficheTirage(repSplited[1]));
					System.out.println("yesssss "+repSplited[1]);
				}
				if(cmd.equals("MVALIDE")) {
					Platform.runLater(()->controller.motValide(repSplited[1]));
				}
				if(cmd.equals("MINVALIDE")) {
					Platform.runLater(()->controller.motInvalide(repSplited[2]));
				}

				if(cmd.equals("RFIN")) { 
					Platform.runLater(()->controller.afficherTexte("\nexpiration du delai de reflexion"));
				}
				if(cmd.equals("BILANMOTS")) { 
					Platform.runLater(()->controller.afficherScoresFinaux(repSplited[2]));
				}

				if(cmd.equals("RECEPTION")) { 
					controller.ajouterListeMessage(repSplited[1]);
				}

				if(cmd.equals("PRECEPTION")) { 			
					controller.messagePrive(repSplited[1]+"de : @"+repSplited[2]);
				}



			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
