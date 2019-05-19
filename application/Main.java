package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;


public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		
		try {
			//BorderPane root = new BorderPane();
			
			primaryStage.setTitle("New Poker");
			
			Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("gui.fxml"));
			
			Scene scene = new Scene(root,900,600);

			primaryStage.setScene(scene);
			primaryStage.show();	
			
			
		} catch(Exception e) {	
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
