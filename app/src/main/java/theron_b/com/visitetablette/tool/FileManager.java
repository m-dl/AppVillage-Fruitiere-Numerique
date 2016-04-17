package theron_b.com.visitetablette.tool;

import android.content.Intent;
import android.os.Environment;
import android.support.design.widget.NavigationView;

import com.google.android.gms.drive.HomeActivity;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import theron_b.com.visitetablette.R;
import theron_b.com.visitetablette.main.MainActivity;

/**
 * Created by Maxime
 */
public class FileManager {

    public static File SD_CARD = Environment.getExternalStorageDirectory();
    public static String MEDIA_PATH = "/VisiteTablette";
    public static String ZIP = ".zip";

    // Delete a file / directory
    public static void Delete(String path) {
        File file = new File(path);
        if (Exist(file)) {
            try {
                FileUtils.forceDelete(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Check if folder or file exists
    public static boolean Exist(File f) {
        if (f.exists())
            return true;
        return false;
    }

    // Update media
    public static void UpdateMedia() {
        // By precaution, delete zip
        Delete(SD_CARD + MEDIA_PATH + ZIP);
        Intent intent = new Intent(MainActivity.getContext(), HomeActivity.class);
        MainActivity.getContext().startActivity(intent);
    }

    // Create menu and list visits
    public static void ListVisits(NavigationView m_NavigationView, boolean french) {
        File folder = new File(SD_CARD + MEDIA_PATH);
        boolean emptyVisitList = true;
        int groupId = 0, itemId = 1, order = 0;
        if (Exist(folder)) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        emptyVisitList = false;
                        m_NavigationView.getMenu().add(groupId, itemId, order, f.getName());
                        itemId++;
                        order++;
                    }
                }
            }
        }
        if (!emptyVisitList)
            order--;
        if (french) {
            m_NavigationView.getMenu().addSubMenu(groupId, itemId, order, R.string.option_section).add(R.string.action_section_1);
        } else {
            m_NavigationView.getMenu().addSubMenu(groupId, itemId, order, R.string.option_section).add(R.string.action_section_english_1);
        }
        itemId--;
        if (french) {
            m_NavigationView.getMenu().getItem(itemId).getSubMenu().add(R.string.action_section_2);
            m_NavigationView.getMenu().getItem(itemId).getSubMenu().add(R.string.action_section_3);
        } else {
            m_NavigationView.getMenu().getItem(itemId).getSubMenu().add(R.string.action_section_english_2);
            m_NavigationView.getMenu().getItem(itemId).getSubMenu().add(R.string.action_section_english_3);
        }
    }

    public static String TranslateFREN(boolean french, String s) {
        if(french)
            return s.substring(s.indexOf("_-_"), s.length());
        else
            return s.substring(s.indexOf("_-_"), s.length());
    }

}
