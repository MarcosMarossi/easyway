package com.example.emr.user.patient.schedule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.emr.configuration.RetrofitConfig;
import com.example.emr.helper.DataCustom;
import com.example.emr.helper.MaskEditUtil;
import com.example.emr.model.json.Result;
import com.example.emr.model.Scheduling;
import com.example.emr.model.User;
import com.example.emr.R;
import com.example.emr.service.Patient;
import com.example.emr.user.patient.HistoryActivity;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Slide02Activity extends AppCompatActivity {

    private FloatingActionButton iValidate, iBack;
    private EditText etHour, etDate;
    private String nameDoctor, nameCategory, hourSelected, id, dateSelected, diaSelecionado, mesSelecionado, anoSelecionado, dataCompleta;
    private Spinner spCategory, spDoctor;
    private Retrofit retrofit;
    private ArrayList<String> names = new ArrayList<String>();
    private SharedPreferences sharedPreferences;

    private MaterialCalendarView mcv;
    private int day, month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_slide02);
        getSupportActionBar().hide();

        sharedPreferences = getSharedPreferences("salvarToken", MODE_PRIVATE);
        id = sharedPreferences.getString("id", null);
        spCategory = findViewById(R.id.spCategory);
        iValidate = findViewById(R.id.fabConfirm);
        iBack = findViewById(R.id.fabBack);
        spDoctor = findViewById(R.id.spDoctor);
        etHour = findViewById(R.id.etHour);
        mcv = findViewById(R.id.calendar);


        /**
         * Configure Hour/Date
         */

        mcv.state().edit()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setMinimumDate(CalendarDay.from(2020, 1, 1))
                .setMaximumDate(CalendarDay.from(2020, 12, 30))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        mcv.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                mesSelecionado = Integer.toString(date.getMonth() + 1);
                diaSelecionado = Integer.toString(date.getDay());
                anoSelecionado = Integer.toString(date.getYear());
                dataCompleta = DataCustom.dataCorreta(diaSelecionado, mesSelecionado, anoSelecionado);
                Toast.makeText(Slide02Activity.this, "" + dataCompleta, Toast.LENGTH_SHORT).show();

            }
        });

        mcv.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay calendarDay) {

                mesSelecionado = Integer.toString(calendarDay.getMonth() + 1);
                diaSelecionado = Integer.toString(calendarDay.getDay());
                anoSelecionado = Integer.toString(calendarDay.getYear());

                dataCompleta = DataCustom.dataCorreta(diaSelecionado, mesSelecionado, anoSelecionado);
            }
        });

        etHour.addTextChangedListener(MaskEditUtil.mask(etHour, MaskEditUtil.FORMAT_HOUR));

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

        iValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hourSelected = etHour.getText().toString();
                String dataFormat = hourSelected + " " + dataCompleta;
                Scheduling schedule = new Scheduling(id, nameCategory, nameDoctor, dataFormat, "Agendado");

                retrofit = RetrofitConfig.retrofitConfig();
                Patient service1 = retrofit.create(Patient.class);
                Call<Scheduling> chm = service1.newSchedule(schedule);
                chm.enqueue(new Callback<Scheduling>() {
                    @Override
                    public void onResponse(Call<Scheduling> call, Response<Scheduling> response) {
                        finish();
                        startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
                    }

                    @Override
                    public void onFailure(Call<Scheduling> call, Throwable t) {

                    }
                });

            }
        });

        iBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Slide01Activity.class));
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
                    Result test = response.body();
                    List<User> shedulings = test.result;
                    names.clear();

                    for (int i = 0; i < shedulings.size(); i++) {
                        User s = shedulings.get(i);
                        names.add(s.getName());
                    }
                }

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(Slide02Activity.this, android.R.layout.simple_spinner_item, names);
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