package com.dhtl.btl_ptud.adapter;

import static android.os.Build.VERSION_CODES.M;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dhtl.btl_ptud.R;
import com.dhtl.btl_ptud.model.Items;

import java.util.Arrays;
import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.RecyclerViewHolder> {
    private List<Items> itemsList;
    private Context context;

    public TestAdapter(List<Items> itemsList, Context context) {
        this.itemsList = itemsList;
        this.context = context;
    }

    private void setAnswerUser(Items items, String answerUser) {
        String myAnswer = items.getMyAnswer();
        if (myAnswer == null)
            myAnswer = "";
        if (myAnswer.contains(answerUser)) {
            myAnswer = myAnswer.replace(answerUser, "");
        } else {
            myAnswer = myAnswer.concat(answerUser);
        }
        char ch[] = myAnswer.toCharArray();//tách về kí tự
        Arrays.sort(ch);//sắp xếp
        myAnswer = String.copyValueOf(ch);//chuyển chuỗi kí tự thành string
        items.setMyAnswer(myAnswer);
        notifyDataSetChanged();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public CheckBox ckbOption1, ckbOption2, ckbOption3, ckbOption4;
        public TextView txtQuestion;
        public ImageView image;
        public Button btnAnswer;


        public RecyclerViewHolder(View itemView) {
            super(itemView);
            //itemView.setOnClickListener(this);
            txtQuestion = (TextView) itemView.findViewById(R.id.txtQuestion);
            ckbOption1 = (CheckBox) itemView.findViewById(R.id.ckbOption1);
            ckbOption2 = (CheckBox) itemView.findViewById(R.id.ckbOption2);
            ckbOption3 = (CheckBox) itemView.findViewById(R.id.ckbOption3);
            ckbOption4 = (CheckBox) itemView.findViewById(R.id.ckbOption4);
            image = (ImageView) itemView.findViewById(R.id.image);
            btnAnswer = (Button) itemView.findViewById(R.id.btnAnswer);

            ckbOption1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAnswerUser(itemsList.get(getPosition()), "1");
                }
            });
            ckbOption2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAnswerUser(itemsList.get(getPosition()), "2");
                }
            });
            ckbOption3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAnswerUser(itemsList.get(getPosition()), "3");
                }
            });
            ckbOption4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAnswerUser(itemsList.get(getPosition()), "4");
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    @Override
    public TestAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.question_layout, parent, false);
        return new TestAdapter.RecyclerViewHolder(itemView);
    }

    @RequiresApi(api = M)
    @Override
    public void onBindViewHolder(TestAdapter.RecyclerViewHolder holder, final int position) {
        int a = itemsList.indexOf(itemsList.get(position)) + 1;
        holder.txtQuestion.setText("  \u25BA Câu " + a + ": " + itemsList.get(position).getQuestion());
        holder.ckbOption1.setText("" + itemsList.get(position).getOption1());
        holder.ckbOption2.setText("" + itemsList.get(position).getOption2());

        Items item = itemsList.get(position);
        int[] attrs = new int[]{(android.R.attr.listChoiceIndicatorMultiple)};
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs);
        if (!TextUtils.isEmpty(item.getMyAnswer())) {
            holder.ckbOption1.setChecked(false);
            holder.ckbOption1.setButtonDrawable(ta.getDrawable(0));
            holder.ckbOption4.setChecked(false);
            holder.ckbOption4.setButtonDrawable(ta.getDrawable(0));
            holder.ckbOption3.setChecked(false);
            holder.ckbOption3.setButtonDrawable(ta.getDrawable(0));
            holder.ckbOption2.setChecked(false);
            holder.ckbOption2.setButtonDrawable(ta.getDrawable(0));

            for (int i = 0; i < item.getMyAnswer().length(); i++) {
                switch (item.getMyAnswer().charAt(i)) {
                    case '1':
                        Log.e("-", 1 + "");
                        holder.ckbOption1.setChecked(true);
                        holder.ckbOption1.setButtonDrawable(ta.getDrawable(0));
                        break;
                    case '2':
                        Log.e("-", 2 + "");
                        holder.ckbOption2.setChecked(true);
                        holder.ckbOption2.setButtonDrawable(ta.getDrawable(0));
                        break;
                    case '3':
                        holder.ckbOption3.setChecked(true);
                        holder.ckbOption3.setButtonDrawable(ta.getDrawable(0));
                        break;

                    case '4':
                        holder.ckbOption4.setChecked(true);
                        holder.ckbOption4.setButtonDrawable(ta.getDrawable(0));
                        break;
                }
            }
        } else {
            holder.ckbOption4.setChecked(false);
            holder.ckbOption4.setButtonDrawable(ta.getDrawable(0));
            holder.ckbOption3.setChecked(false);
            holder.ckbOption3.setButtonDrawable(ta.getDrawable(0));
            holder.ckbOption2.setChecked(false);
            holder.ckbOption2.setButtonDrawable(ta.getDrawable(0));
            holder.ckbOption1.setChecked(false);
            holder.ckbOption1.setButtonDrawable(ta.getDrawable(0));
        }

        if (itemsList.get(position).getIllustrationId() == 0)
            holder.image.setVisibility(View.GONE);
        else {
            holder.image.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(Uri.parse("file:///android_asset/images/i" + itemsList.get(position).getIllustrationId() + ".jpg"))
                    .into(holder.image);
        }

        if (itemsList.get(position).getOption3() != null) {
            holder.ckbOption3.setVisibility(View.VISIBLE);
            holder.ckbOption3.setText("" + itemsList.get(position).getOption3());
        } else
            holder.ckbOption3.setVisibility(View.INVISIBLE);

        if (itemsList.get(position).getOption4() != null) {
            holder.ckbOption4.setVisibility(View.VISIBLE);
            holder.ckbOption4.setText("" + itemsList.get(position).getOption4());
        } else
            holder.ckbOption4.setVisibility(View.INVISIBLE);
    }
}