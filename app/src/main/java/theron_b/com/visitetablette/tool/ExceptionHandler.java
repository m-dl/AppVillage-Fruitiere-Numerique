package theron_b.com.visitetablette.tool;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;

import java.io.StringWriter;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {


    private Activity    m_Activity;
    private final String LINE_SEPARATOR = "\n";

    public ExceptionHandler(Activity activity) {
        m_Activity = activity;
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        StringWriter stackTrace = new StringWriter();
//        exception.printStackTrace(new PrintWriter(stackTrace));
        StringBuilder errorReport = new StringBuilder();
        errorReport.append("************ CAUSE OF ERROR ************\n\n");
        errorReport.append(stackTrace.toString());

        errorReport.append("\n************ DEVICE INFORMATION ***********\n");
        errorReport.append("Brand: ");
        errorReport.append(Build.BRAND);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Device: ");
        errorReport.append(Build.DEVICE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Model: ");
        errorReport.append(Build.MODEL);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Id: ");
        errorReport.append(Build.ID);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Product: ");
        errorReport.append(Build.PRODUCT);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("\n************ FIRMWARE ************\n");
        errorReport.append("SDK: ");
        errorReport.append(Build.VERSION.SDK);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Release: ");
        errorReport.append(Build.VERSION.RELEASE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Incremental: ");
        errorReport.append(Build.VERSION.INCREMENTAL);
        errorReport.append(LINE_SEPARATOR);

        Intent intent = new Intent(m_Activity, ErrorActivity.class);
        intent.putExtra("error", errorReport.toString());
        m_Activity.startActivity(intent);

//        android.os.Process.killProcess(android.os.Process.myPid());
//        System.exit(10);
    }
}
