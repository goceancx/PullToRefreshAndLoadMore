package com.oceancx.pulltorefreshandloadmore;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by oceancx on 15/11/18.
 */
public class RecyclerAdapter extends RecyclerView.Adapter {
    String[] items = {"Today My Life Begins", "Talking To The Moon ", "Count On Me ", "Turn Around", "Lost", "Rest", "Take The Long Way Home", "Lights", "Faded", "Watching Her Move", "No Promises", "Falling Slowly (From )", "You're Not Alone", "No Promises", "The Way You Were", "Moving Target", "Someone To Love", "Easy To Love You", "About You Now", "Must Be A Reason Why (featuring J. Pearl)", "Wanna Be Close", "Get Away", "Everything About You", "Lie About Us", "You & I", "Makin' Good Love", "Toast to Love", "Your Body Is The Business", "80 in 30", "You Know What", "Take It Off", "Tik Tok", "Feels Like Rain", "Tik Tok", "Die Young (Remix)", "TiK ToK (Fred Falke Club Remix)", "Chain Reaction", "C U Next Tuesday", "Slow Motion", "Friday Night Bitch Fight"};

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        VH vh = (VH) holder;
        vh.tv.setText(items[position]);
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    private class VH extends RecyclerView.ViewHolder {
        TextView tv;

        public VH(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv);
        }
    }
}
