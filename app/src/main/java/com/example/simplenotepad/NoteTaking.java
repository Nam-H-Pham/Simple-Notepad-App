package com.example.simplenotepad;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class NoteTaking extends AppCompatActivity {

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_taking);

        getSupportActionBar().hide(); //<< no top bar
        setContentView(R.layout.activity_note_taking);

        Window window = getWindow();// black notifi bar
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#18c9b5"));



        Intent intent = getIntent();
        String filename = intent.getStringExtra("filename");
        String titletext = intent.getStringExtra("title");
        String contentstext = intent.getStringExtra("contents");
        if (filename != null) {
            Log.d("filename", filename);
            Log.d("title", titletext);
            Log.d("contents", contentstext);
            TextView title = findViewById(R.id.TitleInputArea);
            TextView notepad = findViewById(R.id.NoteInputArea);
            title.setText(titletext);
            notepad.setText(contentstext);
        }

    }
    public static String DIRECTORY_DOCUMENTS = "Documents";
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void savenote(View btn){
        TextView title = findViewById(R.id.TitleInputArea);
        String filename = title.getText().toString();

        if (filename.trim().isEmpty()){
            Toast.makeText(this, "Title field is empty", Toast.LENGTH_SHORT).show();
            return;
        }


        TextView note = findViewById(R.id.NoteInputArea);
        String notecontents = note.getText().toString();

        filename = filename+".txt";
        Toast.makeText(this, "Saving "+filename, Toast.LENGTH_SHORT).show();

        Intent intent = getIntent();// delete existing one (make sure title change doesn't matter)
        String intentfilename = intent.getStringExtra("filename");
        if (intentfilename != null) {
            File dir = getFilesDir();
            File file = new File(dir, intentfilename);
            if (file.exists()) {
                boolean deleted = file.delete();
            } else {
                return;
            }
        }


        FileOutputStream fos = null;// create save
        try {
            fos = openFileOutput(filename, MODE_PRIVATE);
            fos.write(notecontents.getBytes());
            //Toast.makeText(this, "Saved to " + getFilesDir() + "/" + filename,
            //        Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);


    }

    public void deletenote(View v){
        Intent intent = getIntent();
        String intentfilename = intent.getStringExtra("filename");

        if (intentfilename == null) {
            Toast.makeText(this, "Note does not yet exist", Toast.LENGTH_SHORT).show();
            return;
        }
        File dir = getFilesDir();
        File file = new File(dir, intentfilename);
        if(file.exists()) {
            boolean deleted = file.delete();
        }

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }


}
