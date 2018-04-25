package application;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class Controller {

	private ObservableList<String> items = FXCollections.observableArrayList();
	private ObservableList<String> itemsJoueurs = FXCollections.observableArrayList();
	private String mot="";
	private int nb = 0;
	@FXML
	private ListView<String> listeMots;

	@FXML
	private ListView<String> listeJoueurs;


	public void init() {
		/**
		 * à faire en cas d'erreur listeMots null **** NE PAS SUPPRIMER
		 * import javafx.application.Platform;
		 * Platform.runLater(()->listeMots.setItems(items)); 
		 */

		listeMots.setItems(items);
		listeJoueurs.setItems(itemsJoueurs);
		itemsJoueurs.add("Mezgha\t\t"+"0 Pts");
		
		
		
	}


	@FXML
	private Text messageErreur;

	
	/**
	 * sert à recuperer le car tapé et le concatener a mot
	 * @param e
	 */
	@FXML
	public void writeText(ActionEvent e) {

		String btnName =((Button) e.getSource()).getText();
		mot = mot + btnName;
		messageErreur.setText(mot);
		System.out.println(mot);
	}

	@FXML
	private Text MonScore;
	
	@FXML
	private Label Moi;
	
	
	
	
	@FXML
	private Button envoyer;

	@FXML
	public void envoyerMot(ActionEvent e) {

		if( mot.length()<=0 ){
			messageErreur.setText("Aucun mot selectionné");
			//System.out.println("Aucun mot selectionné");
		}else {
			nb =nb + mot.length();
			items.add(mot+"\t\t"+mot.length()+" Pts");
			MonScore.setText(nb+" Pts");
			itemsJoueurs.set(0, "Mezgha\t\t"+nb);
			mot="";
			messageErreur.setText("");
		}
	}

	@FXML
	private Button annuler;

	@FXML
	public void annulerMot(ActionEvent e) {
		mot="";
		messageErreur.setText("");
	}

	@FXML
	private Label time ;

	@FXML
	public void writeTime() {
		//		Thread t = new Thread() {
		//			public void run() {
		//				long dernierSalut = System.currentTimeMillis();
		//				while(true)
		//				{
		//					if(dernierSalut + 1000 * 60 * 1 < System.currentTimeMillis())
		//					{
		//						System.out.println("Salut !");
		//						dernierSalut = System.currentTimeMillis();
		//						time.setText(Long.toString(dernierSalut) );
		//					}
		//					System.out.println("Mon traitement");
		//				}
		//			}
		//		};
		//		t.start();
	}




}
