package com.github.irvingryan.demo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by yanwentao on 2017/12/29.
 */

class Adapter extends RecyclerView.Adapter<Adapter.Holder> {
    private List<String> list;
    private Context context;

    public Adapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, null));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.textView.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        TextView textView;

        public Holder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}