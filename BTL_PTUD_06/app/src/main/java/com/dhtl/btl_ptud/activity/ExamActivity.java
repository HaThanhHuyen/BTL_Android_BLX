package com.dhtl.btl_ptud.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.dhtl.btl_ptud.R;
import com.dhtl.btl_ptud.adapter.ExamAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class ExamActivity extends AppCompatActivity {

    Toolbar toolbar;
    private RecyclerView recyclerView;
    private ExamAdapter adapter;
    private GridLayoutManager lLayout;
    private Button btnSubmit;

    private ImageView imgNextPage;

    public ArrayList<Integer> list = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_recycler_layout);
        addControl();
        addEvents();
    }

    private void addEvents() {
    }

    private void addControl() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);//mũi tên quay về

        imgNextPage = (ImageView) findViewById(R.id.imgNextPage);
        imgNextPage.setVisibility(View.INVISIBLE);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setVisibility(View.INVISIBLE);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        lLayout = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(lLayout);
        adapter = new ExamAdapter(list, this);
        recyclerView.setAdapter(adapter);
    }
}