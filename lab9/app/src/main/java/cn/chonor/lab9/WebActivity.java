package cn.chonor.lab9;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * Created by Chonor on 2017/12/23.
 */

public class WebActivity extends AppCompatActivity {
    private WebView webView;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);
        webView=(WebView)findViewById(R.id.webView);
        progressBar=(ProgressBar)findViewById(R.id.progressBar2);
        Bundle extras = getIntent().getBundleExtra("repos");
        if(extras!=null){
            webView.loadUrl(extras.getString("url"));
        }
    }
}
