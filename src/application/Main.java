package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader fxml = new FXMLLoader(getClass().getResource("interface1.fxml"));
			VBox root = fxml.load();
			Controller controller = fxml.getController();
			controller.init();
			Scene scene = new Scene(root,1024,768);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
			/**afin d'informer le serveur quand le joueur ferme directement
			 *  la fenetre sans quitter la partie, que ce dernier est deconnecté */
			primaryStage.setOnCloseRequest(event -> {
			    System.out.println("Stage is closing");
			    controller.quitterPartie();
			});
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
