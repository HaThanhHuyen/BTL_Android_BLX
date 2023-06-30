package com.dhtl.btl_ptud.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.dhtl.btl_ptud.adapter.QuestionAdapter;
import com.dhtl.btl_ptud.database.DatabaseHelper;
import com.dhtl.btl_ptud.model.Items;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class BookActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private QuestionAdapter adapter;
    private LinearLayoutManager lLayout;
    private DatabaseHelper mDBHelper;
    public ArrayList<Items> listItem = null;
    private Toolbar toolbar;
    private Button btnSubmit;
    RelativeLayout relativeLayout;
    ImageView imgNextPage;
    TextView second, txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_recycler_layout);
        addControl();
        addEvents();
    }

    private void addEvents() {
        mDBHelper = new DatabaseHelper(this);

        File database = getApplicationContext().getDatabasePath(DatabaseHelper.DBNAME);
        if (false == database.exists()) {
            mDBHelper.getReadableDatabase();
            if (copyDatabase(this)) {
                Toast.makeText(this, "Copied", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
        }
        listItem = mDBHelper.getListItems();
        adapter = new QuestionAdapter(listItem, this);
        recyclerView.setAdapter(adapter);
        second.setVisibility(View.INVISIBLE);
        imgNextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(BookActivity.this);
                alertDialog.setTitle(getResources().getString(R.string.titletDialog));
                // alertDialog.setMessage();

                final EditText input = new EditText(BookActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setSingleLine();
                input.setHint(getResources().getString(R.string.mesageDialog));
                FrameLayout container = new FrameLayout(BookActivity.this);
                FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin=getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                params.rightMargin=getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                input.setLayoutParams(params);
                container.addView(input);
                alertDialog.setView(container);
                alertDialog.setIcon(R.drawable.green_search);


                alertDialog.setPositiveButton(getString(R.string.agree),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (!input.getText().toString().isEmpty()) {
                                    int page = Integer.parseInt(input.getText().toString());
                                    if (page > listItem.size() || page <= 0) {
                                        Toast.makeText(BookActivity.this, getResources().getString(R.string.noResuilt), Toast.LENGTH_SHORT).show();
                                    } else
                                        recyclerView.scrollToPosition(page - 1);
                                }
                            }
                        });

                alertDialog.setNegativeButton(Html.fromHtml(getString(R.string.cancel_html)),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = alertDialog.create();
                alert.show();

                //màu nút hủy
                Button nbutton1 = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                nbutton1.setTextColor(getResources().getColor(R.color.colorGray));

                //màu nút đồng ý
                Button nbutton2 = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                nbutton2.setTextColor(getResources().getColor(R.color.colorGreen));

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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);//mũi tên quay về

        relativeLayout = (RelativeLayout) findViewById(R.id.rltLayout);
        relativeLayout.setVisibility(View.VISIBLE);

        imgNextPage = (ImageView) findViewById(R.id.imgNextPage);
        second = (TextView) findViewById(R.id.second);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setVisibility(View.INVISIBLE);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        lLayout = new LinearLayoutManager(this);
        lLayout.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(lLayout);

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setVisibility(View.VISIBLE);
        txtTitle.setText(getResources().getString(R.string.book));
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
