package theron_b.com.visitetablette.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.Normalizer;

public class MyString {

    public String stringNormalizer(String string) {
        string = string.replaceAll(" ", "-");
        string = string.replaceAll("'", "-");
        string = string.toLowerCase();
        string = Normalizer.normalize(string, Normalizer.Form.NFD);
        string = string.replaceAll("[^\\p{ASCII}]", "");
        return (string);
    }

    public boolean isImage(String s) {
        return s.contains(".jpg") || s.contains(".png") || s.contains(".gif") || s.contains(".bmp") ||  s.contains(".jpeg") || s.contains(".BMP") ||  s.contains(".GIF") || s.contains(".JPG") || s.contains(".JPEG")|| s.contains(".PNG");
    }

    public StringBuilder getStringFromFile(String location) {
        StringBuilder content = new StringBuilder();

        try {
            BufferedReader bufferedReader;
            bufferedReader = new BufferedReader(new FileReader(new File(location)));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                content.append(line);
                content.append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (content);
    }
}
