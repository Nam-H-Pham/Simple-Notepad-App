package com.example.simplenotepad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        NOTE: if you make a note of the same name of another, it replaces it
        */

        getSupportActionBar().hide(); //<< no top bar
        setContentView(R.layout.activity_main);

        Window window = getWindow();// black notifi bar
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#18c9b5"));

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        load();

        /*
        for (int i = 1; i <= 500; i++) {
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.NoteField);

            LinearLayout notearea = new LinearLayout(this);

            TextView text = new TextView(this);
            String count = String.valueOf(i);
            text.setText(count);

            Button btn = new Button(this);
            btn.setText("open");
            btn.setBackgroundColor(Color.TRANSPARENT);

            notearea.addView(text);
            notearea.addView(btn);

            linearLayout.setBackgroundColor(Color.TRANSPARENT);
            linearLayout.addView(notearea);
        }

         */
    }

    public void newnote(View v) {
        Intent i = new Intent(this, NoteTaking.class);
        startActivity(i);
    }
    public void reload(View v) {
        load();
    }

    public void load() {
        String path = String.valueOf(getFilesDir());/*Get files saved*/
        Log.d("Files", "Accessing: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: " + files.length);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.NoteField);
        if(((LinearLayout) linearLayout).getChildCount() > 0){ /*clear liner layout to reload*/
            ((LinearLayout) linearLayout).removeAllViews();}

        if (files.length == 0){
            TextView textifempty = new TextView(this);
            textifempty.setText("Press ADD NOTE to get started!");
            textifempty.setTextColor(Color.parseColor("#bababa"));

            linearLayout.addView(textifempty);
        }

        for (int i = 0; i < files.length; i++) {/*loop through all file names*/
            Log.d("Files", "FileName:" + files[i].getName());

            String filename = files[i].getName();

            FileInputStream fis = null;
            try {fis = openFileInput(filename);}
            catch (FileNotFoundException e){e.printStackTrace();}

            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();

            String text = null;
            while (true) {
                try {if (!((text = br.readLine()) != null)) break;}
                catch (IOException e) {e.printStackTrace();}
                sb.append(text).append("\n");
            }

            Log.d("Loading text: ", String.valueOf(sb)); /*log contents of files*/

            final String notetext = String.valueOf(sb);
            LinearLayout notearea = new LinearLayout(this);

            TextView txtview = new TextView(this);
            String count = String.valueOf(i+1);
            txtview.setText(count);

            Button btn = new Button(this);
            String btntitle = files[i].getName();
            btntitle = btntitle.replace(".txt","");

            btn.setText(btntitle);
            btn.setBackgroundColor(Color.TRANSPARENT);
            btn.setGravity(Gravity.LEFT);
            btn.setPadding(24,30,0,30);
            btn.setTextSize(18);
            btn.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
            btn.setTextColor(Color.parseColor("#525252"));
            btn.setId(i);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            btn.setLayoutParams(params);//set width to stretch across area


            final String finalBtntitle = btntitle;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button btn = (Button)v;
                    String notename = btn.getText().toString();
                    String filename = notename+".txt";
                    Log.d("loading",filename);

                    Intent i = new Intent(MainActivity.this, NoteTaking.class);
                    i.putExtra("filename",filename);
                    i.putExtra("title", finalBtntitle);
                    i.putExtra("contents", notetext);
                    startActivity(i);
                }
            });

            notearea.addView(txtview);
            notearea.addView(btn);

            linearLayout.setBackgroundColor(Color.TRANSPARENT);
            linearLayout.addView(notearea);

            TextView divider = new TextView(new ContextThemeWrapper(this, R.style.Divider));
            divider.setHeight(1);
            linearLayout.addView(divider);


        }
    }
}