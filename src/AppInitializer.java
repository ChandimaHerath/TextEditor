import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;
import java.util.prefs.Preferences;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        AnchorPane root = FXMLLoader.load(this.getClass().getResource("/view/EditorForm.fxml"));
        Scene mainScene = new Scene(root);
        primaryStage.setScene(mainScene);


        double width = Preferences.userRoot().node("TextEditor-DEP7").getDouble("width", -1);
        double height = Preferences.userRoot().node("TextEditor-DEP7").getDouble("height", -1);
        double x = Preferences.userRoot().node("TextEditor-DEP7").getDouble("x", -1);
        double y = Preferences.userRoot().node("TextEditor-DEP7").getDouble("y", -1);

        if (width != -1 && height !=-1 && x!=-1 && y!= -1){
            primaryStage.setWidth(width);
            primaryStage.setHeight(height);
            primaryStage.setX(x);
            primaryStage.setY(y);
        }else {
            primaryStage.setMaximized(true);
        }


        primaryStage.setOnCloseRequest(event -> {
            Preferences.userRoot().node("TextEditor-DEP7").putDouble("width",primaryStage.getWidth());
            Preferences.userRoot().node("TextEditor-DEP7").putDouble("height",primaryStage.getHeight());
            Preferences.userRoot().node("TextEditor-DEP7").putDouble("x",primaryStage.getX());
            Preferences.userRoot().node("TextEditor-DEP7").putDouble("y",primaryStage.getY());
        });

        primaryStage.setTitle("Text Editor");
        primaryStage.show();


    }


}
