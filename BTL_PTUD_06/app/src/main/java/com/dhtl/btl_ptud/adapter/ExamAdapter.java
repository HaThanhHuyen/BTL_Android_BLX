package com.dhtl.btl_ptud.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dhtl.btl_ptud.R;
import com.dhtl.btl_ptud.activity.TestActivity;
import com.dhtl.btl_ptud.common.RippleView;

import java.util.List;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.RecyclerViewHolder> {

    private List<Integer> list;
    private Context context;

    public ExamAdapter(List<Integer> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView btnExam;
        public ImageView imageView;
        public RippleView rippleView;

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
                    int id = list.get(getPosition()).intValue();
                    Intent intent = new Intent(v.getContext(), TestActivity.class);
                    intent.putExtra("exam", id);
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
    public ExamAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.exam_layout, parent, false);
        return new ExamAdapter.RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ExamAdapter.RecyclerViewHolder holder, int position) {
        holder.btnExam.setText("Đề " + list.get(position).toString());
        holder.imageView.setVisibility(View.GONE);
    }
}
