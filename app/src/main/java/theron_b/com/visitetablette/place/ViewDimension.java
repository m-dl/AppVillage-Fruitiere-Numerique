package theron_b.com.visitetablette.place;

import android.os.AsyncTask;
import android.view.View;

import theron_b.com.visitetablette.tool.Tool;

public class ViewDimension extends AsyncTask<Void, Void, Void> {
    private View m_View;

    public ViewDimension(View view) {
        m_View = view;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Tool tool = new Tool();
        System.out.println(tool.getViewWidth(m_View));
        return null;
    }
}

