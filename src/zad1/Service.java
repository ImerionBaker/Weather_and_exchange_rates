/**
 *
 *  @author VILCHUK SIARHEI S20468
 *
 */

package zad1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Service{

    private String country_code = "No";
    private String valute_code_true = "No";




    @FXML
    private Button getData;

    @FXML
    private Text temp_info;

    @FXML
    private Text temp_feels;

    @FXML
    private Text temp_max;

    @FXML
    private Text temp_min;

    @FXML
    private Text pressure;

    @FXML
    private TextField city;

    @FXML
    private WebView browse;

    @FXML
    private Text currency_pln;


    @FXML
    private TextField valute;

    @FXML
    private Text currency;


    @FXML
    void initialize() {
        getData.setOnAction(actionEvent -> {

            getWeather(city.getText().trim());
            getNBPRate();
            getRateFor(valute.getText().trim());


        });
    }



        //Weather------
        String getWeather (String getUserCity ){


           if(!getUserCity.equals("")) {

               String output = getUrlContent("http://api.openweathermap.org/data/2.5/weather?q=" + getUserCity + "&appid=6206501526b8431077ce1ca8a1a2d889");

               if (!output.isEmpty()) {
                   JSONObject obj = new JSONObject(output);
                   double scale = Math.pow(10, 1);
                   temp_info.setText("Temperature: " + Math.ceil((((obj.getJSONObject("main").getDouble("temp")) - 273.15) * scale)) / scale);
                   temp_feels.setText("Feels like: " + Math.ceil((((obj.getJSONObject("main").getDouble("feels_like")) - 273.15) * scale)) / scale);
                   temp_max.setText("Max: " + Math.ceil((((obj.getJSONObject("main").getDouble("temp_max")) - 273.15) * scale)) / scale);
                   temp_min.setText("Min: " + Math.ceil((((obj.getJSONObject("main").getDouble("temp_min")) - 273.15) * scale)) / scale);
                   pressure.setText("Pressure: " + obj.getJSONObject("main").getDouble("pressure"));
                   country_code = obj.getJSONObject("sys").getString("country");

               }

               //----------
               //Web-------
               final WebEngine web = browse.getEngine();
               String urlweb = "https://en.wikipedia.org/wiki/" + getUserCity;
               web.load(urlweb);
           }else {
               temp_info.setText("");
               temp_feels.setText("");
               temp_max.setText("Input some town");
               temp_min.setText("");
               pressure.setText("");
           }
            //---------
            return getUserCity;
        }


        //currency to PLN
        void getNBPRate(){
        if(!city.getText().equals("")) {
            String output_PLN = getUrlContent("https://restcountries.eu/rest/v2/name/" + country_code);
            String valute_code = "No";
            String valute_code_100 = "No";

            JSONArray jsonArray = new JSONArray(output_PLN);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject_country = jsonArray.getJSONObject(i);
                JSONArray arr = jsonObject_country.getJSONArray("currencies");
                for (int j = 0; j < arr.length(); j++) {
                    valute_code_true = arr.getJSONObject(j).getString("code");
                    valute_code = "1 " + arr.getJSONObject(j).getString("code");
                    valute_code_100 = "100 " + arr.getJSONObject(j).getString("code");

                }

            }
            currency_pln.setText("Currency code: " + valute_code);

            //-----------------

            List<Article2> article2List = new ArrayList<>();
            List<Article3> article3List = new ArrayList<>();

            Document doc = null;
            Document doc2 = null;
            try {
                doc = Jsoup.connect("https://www.nbp.pl/kursy/kursya.html").get();
                doc2 = Jsoup.connect("https://www.nbp.pl/kursy/kursyb.html").get();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            Elements td1Elements_kod = doc.getElementsByAttributeValue("class", "bgt1 right");
            Elements td2Elements_kod = doc.getElementsByAttributeValue("class", "bgt2 right");
            Elements td3Elements_kod = doc2.getElementsByAttributeValue("class", "bgt2 right");
            Elements td4Elements_kod = doc2.getElementsByAttributeValue("class", "bgt2 right");


            td1Elements_kod.forEach(td1Element_kod -> {
                String kod = td1Element_kod.text();
                article2List.add(new Article2(kod));
            });


            td2Elements_kod.forEach(td2Element_kod -> {
                String kod = td2Element_kod.text();
                article3List.add(new Article3(kod));
            });

            td3Elements_kod.forEach(td3Element_kod -> {
                String kod = td3Element_kod.text();
                article2List.add(new Article2(kod));
            });
            td4Elements_kod.forEach(td4Element_kod -> {
                String kod = td4Element_kod.text();
                article3List.add(new Article3(kod));
            });



            double PLN_to_USD;
            for (int i = 0; i < article2List.size(); i++) {
                if (article2List.get(i).toString().equals(valute_code)) {

                    PLN_to_USD = 1 / (Double.parseDouble(article2List.get(i + 1).toString().replaceAll(",", ".")));
                    currency_pln.setText("Currency to PLN: " + PLN_to_USD);
                } else if (article2List.get(i).toString().equals(valute_code_100)) {
                    PLN_to_USD = 100 / (Double.parseDouble(article2List.get(i + 1).toString().replaceAll(",", ".")));
                    currency_pln.setText("Currency to PLN: " + PLN_to_USD);
                }
            }

            for (int i = 0; i < article3List.size(); i++) {
                if (article3List.get(i).toString().equals(valute_code)) {
                    PLN_to_USD = 1 / (Double.parseDouble(article3List.get(i + 1).toString().replaceAll(",", ".")));
                    currency_pln.setText("Currency to PLN: " + PLN_to_USD);
                } else if (article3List.get(i).toString().equals(valute_code_100)) {
                    PLN_to_USD = 100 / (Double.parseDouble(article3List.get(i + 1).toString().replaceAll(",", ".")));
                    currency_pln.setText("Currency to PLN: " + PLN_to_USD);
                }

            }
        }else {
            currency_pln.setText("");
        }
        }
//        //-------------------------
//
//        //Course valute
//
        String getRateFor(String kod_waluty)   {

            String getUserCodeValut = valute.getText().trim();


            if(!getUserCodeValut.equals("")) {
                String output_Valute = getUrlContent("https://api.exchangerate.host/latest?base=" + valute_code_true + "&symbols=" + getUserCodeValut);
                JSONObject obj_3 = new JSONObject(output_Valute);
                currency.setText("Currency: " + obj_3.getJSONObject("rates").getDouble(getUserCodeValut));
            } else{
                currency.setText("Currency is empty");
            }
            return kod_waluty;
        }
















    private static String getUrlContent(String urlAdress){
        StringBuffer content = new StringBuffer();

        try {
            URL url = new URL(urlAdress);
            URLConnection urlConn = url.openConnection();


            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String line;

            while ((line = bufferedReader.readLine()) !=null){
                content.append(line + "\n");
            }
            bufferedReader.close();

        }catch (Exception e){
            System.out.println("There no such town");
        }
        return content.toString();
    }




}

class Article2{
    private String kod_waluty;

    Article2(String kod_waluty) {
        this.kod_waluty = kod_waluty;
    }


    @Override
    public String toString() {
        return kod_waluty;
    }
}

class Article3{
    private String kod_waluty_2;

    Article3(String kod_waluty_2) {
        this.kod_waluty_2 = kod_waluty_2;
    }


    @Override
    public String toString() {
        return kod_waluty_2;
    }

}
