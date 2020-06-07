package view;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws IOException {

			Parent root = FXMLLoader.load(getClass().getResource("fxmls/view.fxml"));
			Scene scene = new Scene(root);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setResizable(true);
			primaryStage.setTitle("Risiko!");
			//Image icon = new Image("C:\\Users\\Luca\\Documents\\Progetto-B20\\src\\view\\fxmls\\images\\App_Logo.png");
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("fxmls/images/App_Logo.png")));

			primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}