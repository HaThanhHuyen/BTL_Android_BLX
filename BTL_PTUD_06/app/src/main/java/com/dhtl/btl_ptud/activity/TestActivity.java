package com.dhtl.btl_ptud.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.dhtl.btl_ptud.adapter.TestAdapter;
import com.dhtl.btl_ptud.database.DatabaseHelper;
import com.dhtl.btl_ptud.model.Items;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class TestActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TestAdapter adapter;
    private LinearLayoutManager lLayout;
    private DatabaseHelper mDBHelper;
    public ArrayList<Items> listItem = null;
    Toolbar toolbar;
    Button btnSubmit;
    RelativeLayout relativeLayout;
    TextView minute, second, txtTitle;
    ImageView imgNextPage;
    private static final String FORMAT = "%02d:%02d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_recycler_layout);
        addControl();
        //tạo dữ liệu
        initData();
        //Tạo đếm ngược
        createCountDownTime();
        //Sự kiện
        addEvent();
    }

    private void initData() {
        try {
            final Intent intent = getIntent();
            int id = intent.getIntExtra("exam", 10);

            mDBHelper = new DatabaseHelper(this);
            File database = getApplicationContext().getDatabasePath(DatabaseHelper.DBNAME);
            if (false == database.exists()) {
                mDBHelper.getReadableDatabase();
                if (copyDatabase(this)) {
                    Toast.makeText(this, "Copied", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
            }
            listItem = mDBHelper.getList20Items(id);
            adapter = new TestAdapter(listItem, this);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addEvent() {
        try {
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder b = new AlertDialog.Builder(TestActivity.this);
                    b.setTitle(getString(R.string.notification));
                    b.setMessage("Bạn có chắc chắn muốn nộp bài?")
                            .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    int a = 0;
                                    for (Items items : listItem) {
                                        if (items.getAnswer().replace(",", "").equals(items.getMyAnswer()))
                                            a++;
                                    }
                                    Toast.makeText(TestActivity.this, "Bạn đúng " + a + " câu", Toast.LENGTH_SHORT).show();
                                    Intent intent1 = new Intent(TestActivity.this, MyAnswerActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putParcelableArrayList("list", listItem);
                                    intent1.putExtras(bundle);
                                    startActivity(intent1);
                                    finish();
                                }
                            })
                            .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });
            imgNextPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(TestActivity.this);
                    alertDialog.setTitle(getResources().getString(R.string.titletDialog));

                    final EditText input = new EditText(TestActivity.this);
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
                                            Toast.makeText(TestActivity.this, getResources().getString(R.string.noResuilt), Toast.LENGTH_SHORT).show();
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void createCountDownTime() {
        new CountDownTimer(1200000, 1000) {
            public void onTick(long millisUntilFinished) {
                second.setText("" + String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                Intent intent1 = new Intent(TestActivity.this, MyAnswerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("list", listItem);
                intent1.putExtras(bundle);
                startActivity(intent1);
            }
        }.start();
    }

    private void addControl() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
//        minute = (TextView) findViewById(R.id.minute);
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
