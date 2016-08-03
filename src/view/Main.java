package view;
	
import java.io.InputStream;

import controller.ViewController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class Main extends Application {
	
	private static final int MAX_HEIGHT = 500;
	private static final int MAX_WIDTH = 630;

	private Stage primaryStage;
	
	@Override
	public void start(Stage primaryStage) {
		try {

			AnchorPane root = new AnchorPane();

			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();

			loader.setLocation(Main.class.getResource("Canvas.fxml"));
			root = (AnchorPane) loader.load();
			
			// Give the controller access to the main app.
			ViewController controller = loader.getController();
			controller.setMain(this);

			Scene scene = new Scene(root, MAX_WIDTH, MAX_HEIGHT);
			scene.addEventFilter(MouseEvent.DRAG_DETECTED , new EventHandler<MouseEvent>() {
			    @Override
			    public void handle(MouseEvent mouseEvent) {
			        scene.startFullDrag();
			    }
			});
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			InputStream is = classloader.getResourceAsStream("art.png");
			
			primaryStage.getIcons().add(new Image(is));
			
			primaryStage.setTitle("Tommy's paint");
			primaryStage.setMaxWidth(MAX_WIDTH);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
			
			this.setPrimaryStage(primaryStage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
}
