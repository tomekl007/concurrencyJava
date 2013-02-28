/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package savingstatewebcrawler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tomasz Lelek
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws MalformedURLException, InterruptedException {
        WebCrawler wc = new WebCrawler( new URL("http://www.sport.pl/sport/0,0.html") ) {

            @Override
            protected List<URL> processPage(URL url) {
                System.out.println("ProcessPage() utl " + url);
                List<URL> urlList = new LinkedList<>();
                try {
                    urlList.add(new URL("http://www.sport.pl/pilka/1,64946,13101145,Kon__ktory_mowi__czyli_jak_zwierzeta_ratowaly_pilkarskie.html") );
                } catch (MalformedURLException ex) {
                    Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                }
                return urlList;
                
            }
        };
        
        wc.start();
        wc.stop();
        System.out.println(wc.getUrlsToCrawl());
        
    }
}
