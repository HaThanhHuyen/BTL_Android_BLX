package com.dhtl.btl_ptud.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.dhtl.btl_ptud.R;
import com.dhtl.btl_ptud.adapter.AnswerAdapter;
import com.dhtl.btl_ptud.database.DatabaseHelper;
import com.dhtl.btl_ptud.model.Items;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


public class AnswerActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AnswerAdapter adapter;
    private LinearLayoutManager lLayout;
    private DatabaseHelper mDBHelper;
    public ArrayList<Items> listItem = null;
    Toolbar toolbar;
    Button btnSubmit;
    RelativeLayout relativeLayout;
    TextView second, txtTitle;
    ImageView imgNextPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_recycler_layout);
        addControl();
        addEvents();
    }

    private void addEvents() {
        final Intent intent = getIntent();
        int question = intent.getIntExtra("question", 0);
        mDBHelper = new DatabaseHelper(this);


        File database = getApplicationContext().getDatabasePath(DatabaseHelper.DBNAME);
        if (false == database.exists()) {
            mDBHelper.getReadableDatabase();
            if (copyDatabase(this)) {
                Toast.makeText(this, "Copied", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
        }

        Bundle bundle = getIntent().getExtras();
        listItem = bundle.getParcelableArrayList("list");
        adapter = new AnswerAdapter(listItem, this);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(question - 1);

        second.setVisibility(View.INVISIBLE);
        btnSubmit.setVisibility(View.GONE);

        imgNextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AnswerActivity.this);
                alertDialog.setTitle(getResources().getString(R.string.titletDialog));
                alertDialog.setMessage(getResources().getString(R.string.mesageDialog));

                final EditText input = new EditText(AnswerActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setHint(getResources().getString(R.string.mesageDialog));
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);
                alertDialog.setIcon(R.drawable.search);
                alertDialog.setPositiveButton(getString(R.string.agree),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (!input.getText().toString().isEmpty()) {
                                      int page = Integer.parseInt(input.getText().toString());
                                    if (page > listItem.size()) {
                                        Toast.makeText(AnswerActivity.this, getResources().getString(R.string.noResuilt), Toast.LENGTH_SHORT).show();
                                    } else
                                        recyclerView.scrollToPosition(page - 1);
                                }
                            }
                        });

                alertDialog.setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();

            }
        });

        //vuốt sang sẽ next 1 sang item tiếp theo
        LinearSnapHelper snapHelper = new LinearSnapHelper() {
            @Override
            public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
                int targetPos = super.findTargetSnapPosition(layoutManager, velocityX, velocityY);
                final View currentView = findSnapView(layoutManager);
                if (targetPos != RecyclerView.NO_POSITION && currentView != null) {
                    int currentPostion = layoutManager.getPosition(currentView);
                    int first = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                    int last = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    currentPostion = targetPos < currentPostion ? last : (targetPos > currentPostion ? first : currentPostion);
                    targetPos = targetPos < currentPostion ? currentPostion - 1 : (targetPos > currentPostion ? currentPostion + 1 : currentPostion);
                }
                return targetPos;
            }
        };

        snapHelper.attachToRecyclerView(recyclerView);

    }

    private void addControl() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //quay về activity trước
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        relativeLayout = (RelativeLayout) findViewById(R.id.rltLayout);
        relativeLayout.setVisibility(View.VISIBLE);
        second = (TextView) findViewById(R.id.second);
        imgNextPage = (ImageView) findViewById(R.id.imgNextPage);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);//mũi tên quay về
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        lLayout = new LinearLayoutManager(this);

        lLayout.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(lLayout);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setVisibility(View.VISIBLE);
        txtTitle.setText(getResources().getString(R.string.test));
    }

    private boolean copyDatabase(Context context) {
        try {
            InputStream inputStream = context.getAssets().open(DatabaseHelper.DBNAME);
            String outFileName = DatabaseHelper.DBLOCATION + DatabaseHelper.DBNAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buff = new byte[1024];
            int lenght = 0;
            while ((lenght = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, lenght);
            }
            outputStream.flush();
            outputStream.close();
            Log.v("Mai", "DB copied");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
