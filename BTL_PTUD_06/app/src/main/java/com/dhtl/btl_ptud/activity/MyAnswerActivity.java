package com.dhtl.btl_ptud.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dhtl.btl_ptud.R;
import com.dhtl.btl_ptud.adapter.MyAnswerAdapter;
import com.dhtl.btl_ptud.model.Items;

import java.util.ArrayList;


public class MyAnswerActivity extends AppCompatActivity {
    Toolbar toolbar;
    private RecyclerView recyclerView;
    private MyAnswerAdapter adapter;
    private GridLayoutManager lLayout;
    private Button btnSubmit;

    private ImageView imgNextPage;
    ArrayList<Items> listItem;
    TextView txtCore, txtResuit;
    LinearLayout lnResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_recycler_layout);
        addControl();
        addEvent();
    }
    private void addEvent() {
        int i = 0;
        for (Items items : listItem) {
            if (items.getAnswer().replace(",", "").equals(items.getMyAnswer()))
                i++;
        }

        txtCore.setText(i + "/" + listItem.size());
        txtCore.setVisibility(View.VISIBLE);

        if (i >= 16)
            txtResuit.setText(getResources().getString(R.string.pass));
        else
            txtResuit.setText(getResources().getString(R.string.fail));
    }

    private void addControl() {
        Bundle bundle = getIntent().getExtras();
        listItem = bundle.getParcelableArrayList("list");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);//mũi tên quay về

        txtCore = (TextView) findViewById(R.id.txtCore);

        imgNextPage = (ImageView) findViewById(R.id.imgNextPage);
        imgNextPage.setVisibility(View.INVISIBLE);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setVisibility(View.INVISIBLE);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        lLayout = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(lLayout);
        adapter = new MyAnswerAdapter(listItem, this);
        recyclerView.setAdapter(adapter);

        lnResult = (LinearLayout) findViewById(R.id.lnResult);
        txtResuit = (TextView) findViewById(R.id.txtResuit);
        lnResult.setVisibility(View.VISIBLE);
    }
}