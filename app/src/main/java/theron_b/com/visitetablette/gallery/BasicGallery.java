package theron_b.com.visitetablette.gallery;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.widget.AdapterView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import theron_b.com.visitetablette.R;
import theron_b.com.visitetablette.gallery.adapter.ImageAdapter;
import theron_b.com.visitetablette.tool.Param;

import android.widget.AdapterView.OnItemClickListener;

public class BasicGallery extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar m_Toolbar;

    @Bind(R.id.gridView1)
    GridView m_GridView;

    private ImageAdapter        m_ImageAdapter;
    private ArrayList<String>   m_ImagePaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_basicgallery);
        ButterKnife.bind(this);
        setParam();
        setParamActionBar();
        m_ImagePaths = new ArrayList<>();
        fillGridView();
    }

    ArrayList<File> files1 = new ArrayList<>();
    private void fillGridView() {
        File file = new File(getIntent().getStringExtra("directory"));
        ArrayList<File> files = new ArrayList<>(Arrays.asList(file.listFiles()));
        if (getIntent().getBooleanExtra("interieur", false)) {
            for (int i = 0; i < files.size(); ++i) {
                if (files.get(i).toString().contains("360_")) {
                    m_ImagePaths.add(files.get(i).toString());
                    files1.add(files.get(i));
                }
            }
            m_ImageAdapter = new ImageAdapter(this, files1, getResources().getString(R.string.ImageTransition));
        } else {
            for (int i = 0; i < files.size(); ++i) {
                m_ImagePaths.add(files.get(i).toString());
            }
            m_ImageAdapter = new ImageAdapter(this, files, getResources().getString(R.string.ImageTransition));
        }
        m_GridView.setAdapter(m_ImageAdapter);
        m_GridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if (getIntent().getBooleanExtra("interieur", false)) {
                    Intent intent = new Intent();
                    intent.setAction(android.content.Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(files1.get(position)), "image/*");
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(BasicGallery.this, FullScreenViewActivity.class);
                    intent.putExtra("position", position);
                    String[] stockArr = new String[m_ImagePaths.size()];
                    stockArr = m_ImagePaths.toArray(stockArr);
                    intent.putExtra("filesPath", stockArr);
//                startActivity(intent);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            BasicGallery.this,
                            Pair.create((View) m_GridView, getResources().getString(R.string.ImageTransition))
                    );
                    ActivityCompat.startActivity(BasicGallery.this, intent, options.toBundle());
                }
            }
        });
    }

    private void setParamActionBar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getIntent().getStringExtra("actionBarTitle"));
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(getIntent().getStringExtra("actionBarColor"))));
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowTitleEnabled(true);
        }
    }

    private void setParam() {
        Param param = new Param();
        param.paramWindowFullScreen(getWindow());
        param.paramSetSupportActionBar(m_Toolbar, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        destroyImageAdapter();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        destroyImageAdapter();
        super.onBackPressed();
    }

    private void destroyImageAdapter() {
        if (m_ImageAdapter != null) {
            m_ImageAdapter.onDestroy();
            m_ImageAdapter = null;
        }
    }
}