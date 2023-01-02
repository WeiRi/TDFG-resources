package pl.sda;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ChuckNorrisRandomJokeFromJsonAPI {

    public static void main(String[] args) throws IOException {


        for (int i = 0; i < 10; i++){
            String joke = GetJoke();
            System.out.println(joke);
        }

    }

    private static String GetJoke() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://api.chucknorris.io/jokes/random");
        CloseableHttpResponse response1 = httpclient.execute(httpGet);
        //to zwraca nam strona internetowa w formacie json
        InputStream content = response1.getEntity().getContent();
        //wyciagamy napis z zartem z value z pomoca biblioteki Gson

        JsonParser jp = new JsonParser(); //from gson
        JsonElement root = jp.parse(new InputStreamReader((InputStream) content)); //Convert the input stream to a json element
        JsonObject rootobj = root.getAsJsonObject(); //May be an array, may be an object.
        //w jSonie nasz zart to w konwencji wartość i klucz - value to nasz zart
        return rootobj.get("value").getAsString();
    }

}
