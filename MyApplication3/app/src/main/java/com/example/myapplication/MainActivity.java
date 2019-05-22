package com.example.myapplication;

import android.app.AlertDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button reset;
    Button about;
    Draw draw;
    AlertDialog.Builder dlgAlert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        draw = (Draw)findViewById(R.id.Draw1);
        reset = (Button)findViewById(R.id.Reset);
        about = (Button)findViewById(R.id.About);

        dlgAlert  = new AlertDialog.Builder(this);
        dlgAlert.setMessage("Ragdoll\nZhonghao Ji\n20646959");

        dlgAlert.setCancelable(true);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                draw = (Draw)findViewById(R.id.Draw1);
                draw.reset();
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                dlgAlert.create().show();
            }
        });
    }


}
