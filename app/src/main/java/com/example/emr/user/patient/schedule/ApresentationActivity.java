package com.example.emr.user.patient.schedule;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.emr.R;
import com.example.emr.user.patient.menu.MenuUsrActivity;

public class ApresentationActivity extends AppCompatActivity {

    private FloatingActionButton iNext, iBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_slide01);

        getSupportActionBar().hide();

        iNext = findViewById(R.id.fabNext);
        iBack = findViewById(R.id.fabBack);

        iNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ApresentationActivity.this, CalendarActivity.class);
                startActivity(it);
            }
        });

        iBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(ApresentationActivity.this);
                dialog.setTitle(R.string.sair_titulo);
                dialog.setIcon(R.drawable.ic_remove_circle_black_24dp);

                dialog.setPositiveButton(R.string.sair_sim, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(ApresentationActivity.this, MenuUsrActivity.class));
                    }
                });

                dialog.setNegativeButton(R.string.sair_nao, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.create();
                dialog.show();
            }
        });
    }
}
