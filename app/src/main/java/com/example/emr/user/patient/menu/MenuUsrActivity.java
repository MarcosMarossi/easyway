package com.example.emr.user.patient.menu;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emr.adapter.MenuAdapter;
import com.example.emr.*;
import com.example.emr.R;
import com.example.emr.helper.RecyclerItemClickListener;
import com.example.emr.user.patient.DetailsAcount;
import com.example.emr.user.patient.HistoryActivity;
import com.example.emr.user.patient.RecordActivity;
import com.example.emr.user.patient.schedule.ApresentationActivity;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.example.emr.R.*;

public class MenuUsrActivity extends AppCompatActivity {

    private ListView listView;
    private RecyclerView recyclerView;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String name;
    private TextView txtNameUser;
    private MenuAdapter menuAdapter;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.act_menu_usr);

        sharedPreferences = getSharedPreferences("salvarToken", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        name = sharedPreferences.getString("user_name", null);

        getSupportActionBar().setElevation(0);

        txtNameUser = findViewById(id.txtNameUser);
        txtNameUser.setText(name);

        int[][] dados = {
                {string.tit_agendar, string.desc_agendar},
                {string.tit_historico, string.desc_historico},
                {string.titulo_prontuario, string.resumo_prontuario},
                {string.tit_account, string.desc_account}};


        int[] dadosImg = {drawable.snellenchart, drawable.healthcareandmedical, drawable.medicalreport, drawable.answer};

        recyclerView = findViewById(id.recyclerView);
        menuAdapter = new MenuAdapter(getApplication(), dados, dadosImg);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(menuAdapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                switch (position) {
                                    case 0:
                                        startActivity(new Intent(getApplicationContext(), ApresentationActivity.class));
                                        break;
                                    case 1:
                                        startActivity(new Intent(getApplicationContext(), HistoryActivity.class));

                                        break;
                                    case 2:
                                        startActivity(new Intent(getApplicationContext(), RecordActivity.class));
                                        break;
                                    case 3:
                                        startActivity(new Intent(getApplicationContext(), DetailsAcount.class));
                                        break;
                                    default:
                                        Toast.makeText(MenuUsrActivity.this, "Não foi possível", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case id.MenuSair:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle(string.sair_titulo);
                dialog.setIcon(drawable.ic_remove_circle_black_24dp);

                dialog.setPositiveButton(string.sair_sim, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor.putString("token", null);
                        editor.commit();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finishAffinity();
                    }
                });

                dialog.setNegativeButton(string.sair_nao, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                dialog.create();
                dialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}