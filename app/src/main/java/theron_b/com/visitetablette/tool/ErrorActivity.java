package theron_b.com.visitetablette.tool;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import theron_b.com.visitetablette.R;

public class ErrorActivity extends AppCompatActivity {

    @Bind(R.id.TextViewErrorActivity)
    TextView         m_TextViewErrorActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m_TextViewErrorActivity.setText(getIntent().getStringExtra("error"));
        ButterKnife.bind(this);
    }

}
