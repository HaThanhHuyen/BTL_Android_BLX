package com.dhtl.btl_ptud.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TableRow;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

import com.dhtl.btl_ptud.R;


public class MainActivity extends AppCompatActivity {
    TableRow trBook, trTest, trGuide, trSuggest;
    ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControl();
        addEvent();
    }

    private void addEvent() {
        try {
            trBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, BookActivity.class);
                    startActivity(intent);
                }
            });
            trTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, ExamActivity.class);
                    startActivity(intent);
                }
            });
            trGuide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String youtubeLink = "https://www.youtube.com/watch?v=PXwVbtxh8P4"; // Link YouTube cần mở
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink));
                    startActivity(intent);
                }
            });
            trSuggest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, SuggestActivity.class);
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addControl() {
        try{
            viewFlipper = (ViewFlipper) findViewById(R.id.vwFlipper);
            trBook = (TableRow) findViewById(R.id.trBook);
            trTest = (TableRow) findViewById(R.id.trTest);
            trGuide = (TableRow) findViewById(R.id.trGuide);
            trSuggest = (TableRow) findViewById(R.id.trSuggest);
            viewFlipper.setAutoStart(true);
            viewFlipper.setFlipInterval(4000);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}