/**
 *
 *  @author VILCHUK SIARHEI S20468
 *
 */

//package zad1;
//
//
//public class Main {
//  public static void main(String[] args) {
//    Service s = new Service("Poland");
//    String weatherJson = s.getWeather("Warsaw");
//    Double rate1 = s.getRateFor("USD");
//    Double rate2 = s.getNBPRate();
//    // ...
//    // część uruchamiająca GUI
//  }
//}


package zad1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception{


    Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
    primaryStage.setTitle("Weather");
    init();
    primaryStage.setScene(new Scene(root,1196, 773));
    primaryStage.show();


  }


  public static void main(String[] args) {
    launch(args);
  }

}

