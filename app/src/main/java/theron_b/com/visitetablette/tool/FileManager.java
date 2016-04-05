package theron_b.com.visitetablette.tool;

import android.os.Environment;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;

/**
 * Created by Maxime
 */
public class FileManager {
    // Delete a file / directory
    public static void Delete(String path) {
        File file = new File(path);
        try {
            FileUtils.forceDelete(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Update media
    public static void UpdateMedia() {
        String newMedia = Environment.getExternalStorageDirectory().getAbsolutePath() + "/VisiteTablette.zip";
        // TODO: drive download
        Delete(Environment.getExternalStorageDirectory() + "/VisiteTablette");
        ZipManager.unZip(newMedia, Environment.getExternalStorageDirectory().getAbsolutePath());
        Delete(Environment.getExternalStorageDirectory() + "/VisiteTablette.zip");
    }
}
