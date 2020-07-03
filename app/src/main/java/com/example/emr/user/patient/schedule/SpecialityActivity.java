package com.example.emr.user.patient.schedule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.emr.R;
import com.example.emr.configuration.RetrofitConfig;
import com.example.emr.model.Scheduling;
import com.example.emr.model.User;
import com.example.emr.model.json.Result;
import com.example.emr.service.Patient;
import com.example.emr.user.patient.HistoryActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SpecialityActivity extends AppCompatActivity {

    private FloatingActionButton fabConfirm, fabBack;
    private Spinner spCategory, spDoctor;
    private String nameDoctor, nameCategory, hourSelected, dateSelected, id;
    private Retrofit retrofit;
    private List<String> names = new ArrayList<String>();
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_slide03);

        fabConfirm = findViewById(R.id.fabConfirm);
        fabBack = findViewById(R.id.fabBack);
        spCategory = findViewById(R.id.spCategory);
        spDoctor = findViewById(R.id.spDoctor);

        getSupportActionBar().hide();

        sharedPreferences = getSharedPreferences("salvarToken", MODE_PRIVATE);
        id = sharedPreferences.getString("id", null);
        hourSelected = sharedPreferences.getString("hour", null);
        dateSelected = sharedPreferences.getString("date", null);

        /**
         * Configure Doctor Spinner
         */
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categorias, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter);

        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                nameCategory = spCategory.getSelectedItem().toString();
                callRetrofit();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spDoctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                nameDoctor = spDoctor.getSelectedItem().toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        fabConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dataFormat = hourSelected + " " + dateSelected;
                Scheduling schedule = new Scheduling(id, nameCategory, nameDoctor, dataFormat, "Agendado");

                retrofit = RetrofitConfig.retrofitConfig();
                Patient service1 = retrofit.create(Patient.class);
                Call<Scheduling> chm = service1.newSchedule(schedule);
                chm.enqueue(new Callback<Scheduling>() {
                    @Override
                    public void onResponse(Call<Scheduling> call, Response<Scheduling> response) {
                        Toast.makeText(SpecialityActivity.this, R.string.sucesso_agendamento, Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
                    }

                    @Override
                    public void onFailure(Call<Scheduling> call, Throwable t) {
                        Toast.makeText(SpecialityActivity.this, R.string.erro_agendamento , Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), CalendarActivity.class));
            }
        });
    }

    public void callRetrofit() {
        retrofit = RetrofitConfig.retrofitConfig();
        Patient service = retrofit.create(Patient.class);

        Call<Result> call = service.getDoctors(nameCategory);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.isSuccessful()) {
                    Result result = response.body();
                    List<User> shedulings = result.result;
                    names.clear();

                    for (int i = 0; i < shedulings.size(); i++) {
                        User s = shedulings.get(i);
                        names.add(s.getName());
                    }
                }

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(SpecialityActivity.this, android.R.layout.simple_spinner_item, names);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                spDoctor.setAdapter(spinnerArrayAdapter);
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.d("404", "Ocorreu um erro: " + t);
            }
        });
    }
}
