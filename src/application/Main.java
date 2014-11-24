package application;
	
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			//Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
			MainController mainController = new MainController();
			Scene scene = new Scene(mainController);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("CribbageFX");
			primaryStage.setScene(scene);
			primaryStage.show();
			
			//let's get the game started...
			new Cribbage (mainController);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
