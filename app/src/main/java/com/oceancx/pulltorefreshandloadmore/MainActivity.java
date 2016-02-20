package com.oceancx.pulltorefreshandloadmore;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv;


    PullToFreshAndLordMoreLayout pullToFreshAndLordMoreLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pullToFreshAndLordMoreLayout = (PullToFreshAndLordMoreLayout) findViewById(R.id.pull_to_refresh);
        pullToFreshAndLordMoreLayout.setRefreshAndLoadMoreListener(new PullToFreshAndLordMoreLayout.RefreshAndLoadMoreListener() {
            @Override
            public void onLoadMore() {

            }

            @Override
            public void onRefresh() {

                /**
                 * on Refresh
                 */
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        pullToFreshAndLordMoreLayout.onFinishRefresh();
                    }
                }.execute();
            }

            @Override
            public void onLoadMoreComplete() {

            }

            @Override
            public void onRefreshComplete() {

            }
        });

//        pull_to_refresh_control_bt = (Button) findViewById(R.id.pull_to_refresh_control_bt);


        rv = (RecyclerView) findViewById(R.id.ryc_views);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new RecyclerAdapter());
    }
}
