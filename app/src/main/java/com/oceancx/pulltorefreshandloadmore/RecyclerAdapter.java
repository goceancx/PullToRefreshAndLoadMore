package com.oceancx.pulltorefreshandloadmore;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by oceancx on 15/11/18.
 */
public class RecyclerAdapter extends RecyclerView.Adapter {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new VH(v);
    }

    int size = 3;

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    private boolean finish = false;

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public void loadMore() {

        finish = false;
        long sleepTime = 500 * ((size * 17) % 3);
        DebugLog.e("sleep:time: " + sleepTime);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        size += 10;

        DebugLog.e("size:" + size + " finish:" + finish);


        finish = true;
    }

    @Override
    public int getItemCount() {
        return size;
    }

    private class VH extends RecyclerView.ViewHolder {

        public VH(View itemView) {
            super(itemView);
        }
    }
}
