package com.example.exerciseapp.aty.login;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.TextView;
import com.example.exerciseapp.BaseActivity;
import com.example.exerciseapp.R;
import butterknife.Bind;

public class AtyUserLawItem extends BaseActivity {
    @Bind(R.id.wvUserLawItem)
    WebView webView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_text)
    TextView toolBarTitle;

    String title;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_user_law_item);
        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
        setTitleBar();
        getInfo();
    }

    private void getInfo(){
        webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.loadUrl(url);
    }

    private void setTitleBar() {
        toolbar.setPadding(0, getDimensionMiss(), 0, 0);
        toolbar.setTitle("");
        toolBarTitle.setText(title);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backbtn);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AtyUserLawItem.this.finish();
            }
        });
    }
}
