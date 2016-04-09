package theron_b.com.visitetablette.tool;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by Maxime
 */
public class FileManager {

    public static String MEDIA_PATH = "/VisiteTablette";
    public static String ZIP = ".zip";

    // Delete a file / directory
    public static void Delete(String path) {
        File file = new File(path);
        if(Exist(file)) {
            try {
                FileUtils.forceDelete(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Check if folder or file exists
    public static boolean Exist(File f) {
        if(f.exists())
            return true;
        return false;
    }
}
