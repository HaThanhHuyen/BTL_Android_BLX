package com.dhtl.btl_ptud.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.dhtl.btl_ptud.R;
import com.dhtl.btl_ptud.activity.AnswerActivity;
import com.dhtl.btl_ptud.common.RippleView;
import com.dhtl.btl_ptud.model.Items;
import java.util.ArrayList;
import java.util.List;

public class MyAnswerAdapter extends RecyclerView.Adapter<MyAnswerAdapter.RecyclerViewHolder> {
    private List<Items> list;
    private Context context;

    public MyAnswerAdapter(List<Items> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView btnExam;
        public ImageView imageView;
        RippleView rippleView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            //itemView.setOnClickListener(this);
            btnExam = (TextView) itemView.findViewById(R.id.btnExam);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            rippleView = (RippleView) itemView.findViewById(R.id.more);

            rippleView.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int question = list.indexOf(list.get(getPosition())) + 1;
                    Intent intent = new Intent(v.getContext(), AnswerActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) list);
                    intent.putExtras(bundle);
                    intent.putExtra("question", question);
                    v.getContext().startActivity(intent);
                }
            }, 200);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public MyAnswerAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.exam_layout, parent, false);
        return new MyAnswerAdapter.RecyclerViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(MyAnswerAdapter.RecyclerViewHolder holder, int position) {
        int a = list.indexOf(list.get(position)) + 1;
        holder.btnExam.setText("Câu " + a);
        if (list.get(position).getAnswer().replace(",", "").equals(list.get(position).getMyAnswer()))
            holder.imageView.setImageDrawable(context.getDrawable(R.drawable.correct));
        else
            holder.imageView.setImageDrawable(context.getDrawable(R.drawable.incorrect));
    }

}