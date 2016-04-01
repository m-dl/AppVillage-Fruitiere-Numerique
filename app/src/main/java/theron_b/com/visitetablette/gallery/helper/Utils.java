package theron_b.com.visitetablette.gallery.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class Utils {

	private Context _context;

	public Utils(Context context) {
		this._context = context;
	}

	public ArrayList<String> getFilePaths(String path) {
		ArrayList<String> filePaths = new ArrayList<>();

        File sdcard = Environment.getExternalStorageDirectory();
        File checkMounted = new File(sdcard + path);
        if (!checkMounted.isDirectory())
            sdcard = new File("/storage/extSdCard");
        File directory = new File(sdcard + path + "/Photo");
		if (directory.isDirectory()) {
			// getting list of file paths
			File[] listFiles = directory.listFiles();

			// Check for count
			if (listFiles.length > 0) {

				// loop through all files
				for (File listFile : listFiles) {

					// get file path
					String filePath = listFile.getAbsolutePath();

					// check for supported file extension
					if (IsSupportedFile(filePath)) {
						// Add image path to array list
						filePaths.add(filePath);
					}
				}
			} else {
				// image directory is empty
				Toast.makeText(
						_context,
						theron_b.com.visitetablette.gallery.helper.AppConstant.PHOTO_ALBUM
								+ " is empty. Please load some images in it !",
						Toast.LENGTH_LONG).show();
			}

		} else {
			AlertDialog.Builder alert = new AlertDialog.Builder(_context);
			alert.setTitle("!!!!");
			alert.setMessage("Directory path is not valid!");
			alert.setPositiveButton("OK", null);
			alert.show();
		}

		return filePaths;
	}

	/*
	 * Check supported file extensions
	 * 
	 * @returns boolean
	 */
	private boolean IsSupportedFile(String filePath) {
		String ext = filePath.substring((filePath.lastIndexOf(".") + 1),
				filePath.length());

		return AppConstant.FILE_EXTN
				.contains(ext.toLowerCase(Locale.getDefault()));

	}

}
