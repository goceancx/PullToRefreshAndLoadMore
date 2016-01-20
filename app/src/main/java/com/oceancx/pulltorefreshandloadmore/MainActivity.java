package com.oceancx.pulltorefreshandloadmore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PullToFreshAndLordMoreLayout pullToFreshAndLordMoreLayout = (PullToFreshAndLordMoreLayout) findViewById(R.id.pull_to_refresh);
        pullToFreshAndLordMoreLayout.setRefreshAndLoadMoreListener(new PullToFreshAndLordMoreLayout.RefreshAndLoadMoreListener() {
            @Override
            public void onLoadMore() {

            }

            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMoreComplete() {

            }

            @Override
            public void onRefreshComplete() {

            }
        });
        rv= (RecyclerView) findViewById(R.id.ryc_views);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new RecyclerAdapter());
    }
}
