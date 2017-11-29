package iridium;

import java.io.*;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class Updater {

    //version check
    public static String httpResponse(String url) {
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet request = new HttpGet(url);
            request.addHeader("content-type", "text/plain");
            HttpResponse result = httpClient.execute(request);
            String text = EntityUtils.toString(result.getEntity(), "UTF-8");
            return text;
        } catch (IOException e) {
            Presentation.update("check your internet Connection",false);
        }
        return null;
    }

    //loading bit stream  and saving it
    public static void download(String URL) {
        try {
            URL url = new URL(URL);
            java.io.InputStream inStream = url.openStream();
            BufferedInputStream bufIn = new BufferedInputStream(inStream);

            File fileWrite = new File("iridium"+httpResponse("http://jannik.ddns.net/iridium.version.txt").trim()+".jar");
            OutputStream out = new FileOutputStream(fileWrite);
            BufferedOutputStream bufOut = new BufferedOutputStream(out);
            byte buffer[] = new byte[1000000000];
            while (true) {
                int nRead = bufIn.read(buffer, 0, buffer.length);
                if (nRead <= 0)
                    break;
                bufOut.write(buffer, 0, nRead);
            }

            bufOut.flush();
            out.close();
            inStream.close();
        } catch (Exception ex){
            Presentation.update("Something went wrong :(",false);
        }
    }

    //delet to delet old version
    public static void delet(String s){
        try {
            File ff = new File(s);
            ff.delete();
        }catch (Exception e){
            Presentation.update("Unable to delet old version",false);

        }
    }

    //full update method
    public static void update(boolean keep){
        if (!httpResponse("http://jannik.ddns.net/iridium.version.txt").trim().equals(Iridium.version)) {
            if (!keep)
            Presentation.update("The app will restart automaticly",false);
            Presentation.update("installing...",false);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    download("http://jannik.ddns.net/iridium.jar");
                    Presentation.update("finished",false);
                    try {
                        File f = new File(Iridium.class.getProtectionDomain().getCodeSource().getLocation().toURI());
                        String a = f.getPath();
                        String[] parts = a.split("/");
                        Process proc;
                        if (!keep)
                            proc = Runtime.getRuntime().exec("java -jar iridium.jar " + parts[parts.length - 1]);
                        else
                            proc = Runtime.getRuntime().exec("java -jar iridium.jar ");
                    } catch (Exception e) {
                        Presentation.update("unable to launch Latest version",false);
                    }
                    System.exit(0);
                }
            });
            t.start();
        } else {
            Presentation.update("Latest version already installed",false);
        }
    }
}