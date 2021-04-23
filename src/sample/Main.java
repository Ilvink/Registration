package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        String scene = "sample.fxml";
        File file = new File("user.settings");
        boolean exist = file.exists();
        if (exist){
            FileInputStream fileInputStream = new FileInputStream("user.settings");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            User user = (User) objectInputStream.readObject();
            if(!user.getLogin().equals(""))
                scene = "main.fxml";

            objectInputStream.close();

        }
        Parent root = FXMLLoader.load(getClass().getResource("fxml/" + scene));
        primaryStage.setTitle("Registration");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
