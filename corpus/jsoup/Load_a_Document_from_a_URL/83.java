package com.samuel.jsoupexam;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


/**
 *
 * @author Kazuto
 */
public class Main {
    
    public static void main(String[] args) {
        
        try {
            
            // Me conecto a la página
            Document doc = Jsoup.connect("https://weather.com/es-ES/tiempo/hoy/l/SPXX0084:1:SP").get();
            
            // Muestro el titulo de página
            System.out.println("Titulo de la página: " + doc.title());
            
            // Recojo la temperatura y el tiempo de la página (ambos lo coge por el nombre de la clase, solo selecciona el 1º)
            Element temperatura = doc.select("div.today_nowcard-temp").first();
            Element descTiempo = doc.select("div.today_nowcard-phrase").first();
            
            // Muestro la temperatura y el tiempo recogidos por consola.
            System.out.println("La temperatura: " + temperatura.text());
            System.out.println("El tiempo: " + descTiempo.text());
        } catch (IOException ex) {
            
            // En caso de no carga la página por algun motivo manda el siguiente mensaje
            System.out.println("La página no existe, o no tienes conexión a internet.");
        }
    }
    
}
