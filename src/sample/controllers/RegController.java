package sample.controllers;

import com.mysql.cj.protocol.Message;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.DB;
import sample.User;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class RegController {

    @FXML
    private TextField log_reg;

    @FXML
    private TextField email_reg;

    @FXML
    private PasswordField pass_reg;

    @FXML
    private CheckBox confidentials;

    @FXML
    private Button btn_reg;

    @FXML
    private TextField log_auth;

    @FXML
    private PasswordField pass_auth;

    @FXML
    private Button btn_auth;

    private DB db = new DB();


    @FXML
    void initialize() {
        btn_reg.setOnAction(event->{
            log_reg.setStyle("-fx-border-color: #fafafa");
            email_reg.setStyle("-fx-border-color: #fafafa");
            pass_reg.setStyle("-fx-border-color: #fafafa");
            btn_reg.setText("Check in");

            if(log_reg.getCharacters().length()<=3){
                log_reg.setStyle("-fx-border-color: red");
                return;
            }else if(email_reg.getCharacters().length()<=5) {
                email_reg.setStyle("-fx-border-color: red");
                return;
            } else if(pass_reg.getCharacters().length()<=3) {
                pass_reg.setStyle("-fx-border-color: red");
                return;
            } else if(!confidentials.isSelected()){
                btn_reg.setText("Check the box");
                return;
            }
            String pass =md5String(pass_reg.getCharacters().toString());
            try {
               boolean isReg =  db.regUser(log_reg.getCharacters().toString(),email_reg.getCharacters().toString(),pass);
               if (isReg){
                   log_reg.setText("");
                   email_reg.setText("");
                   pass_reg.setText("");
                   btn_reg.setText("Done");
               }else btn_reg.setText("Enter another Username");
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        btn_auth.setOnAction(event->{
            log_auth.setStyle("-fx-border-color: #fafafa");
            pass_auth.setStyle("-fx-border-color: #fafafa");

            if(log_auth.getCharacters().length()<=3){
                log_auth.setStyle("-fx-border-color: red");
                return;
            } else if(pass_auth.getCharacters().length()<=3) {
                pass_auth.setStyle("-fx-border-color: red");
                return;
            }
            String pass =md5String(pass_auth.getCharacters().toString());
            try {
                boolean isAuth=  db.authUser(log_auth.getCharacters().toString(),pass);
                if (isAuth){

                    FileOutputStream fileOutputStream= new FileOutputStream("user.settings");
                    ObjectOutputStream objectOutputStream= new ObjectOutputStream(fileOutputStream);
                    objectOutputStream.writeObject(new User(log_auth.getCharacters().toString()));
                    objectOutputStream.close();

                    log_auth.setText("");
                    pass_auth.setText("");
                    btn_auth.setText("Done");

                    Parent root = FXMLLoader.load(getClass().getResource("/sample/fxml/main.fxml"));
                    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    primaryStage.setTitle("Registration");
                    primaryStage.setScene(new Scene(root, 600, 400));
                    primaryStage.show();
                }else btn_auth.setText("User is not found");
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
    public static String md5String(String pass){
        MessageDigest messageDigest= null;
        byte [] digest = new byte[0];
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(pass.getBytes());
            digest= messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        BigInteger bigInteger =new BigInteger(1, digest);
        String md5Hex= bigInteger.toString(16);

        while (md5Hex.length()<32)
            md5Hex = "0" + md5Hex;

        return md5Hex;

    }

}
