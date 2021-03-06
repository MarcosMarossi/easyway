package com.example.emr.user.patient.schedule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CalendarActivity extends AppCompatActivity {

    private FloatingActionButton iValidate, iBack;
    private EditText etHour;
    private String hourSelected, id, diaSelecionado, mesSelecionado, anoSelecionado, dataCompleta;
    private MaterialCalendarView mcv;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_slide02);
        getSupportActionBar().hide();

        sharedPreferences = getSharedPreferences("salvarToken", MODE_PRIVATE);
        id = sharedPreferences.getString("id", null);
        editor = sharedPreferences.edit();

        iValidate = findViewById(R.id.fabConfirm);
        iBack = findViewById(R.id.fabBack);
        etHour = findViewById(R.id.etHour);
        mcv = findViewById(R.id.calendar);

        mcv.state()
                .edit()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        mcv.setOnDateChangedListener( new OnDateSelectedListener() {
            @Override
            public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {
                dataCompleta = DataCustom.dataCorreta( date.getDay(), date.getMonth() + 1 , date.getYear() );
            }
        } );

        mcv.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                dataCompleta = DataCustom.dataCorreta( date.getDay(), date.getMonth()  + 1 , date.getYear() );
            }
        });

        etHour.addTextChangedListener(MaskEditUtil.mask(etHour, MaskEditUtil.FORMAT_HOUR));

        iValidate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                try {
                    hourSelected = etHour.getText().toString();

                    if(DataCustom.dataValida(dataCompleta) && !hourSelected.isEmpty()){
                        editor.putString("hour", hourSelected);
                        editor.putString("date", dataCompleta);
                        editor.commit();
                        startActivity(new Intent(getApplicationContext(), SpecialityActivity.class));
                    } else {
                        Toast.makeText(CalendarActivity.this, "A hora ou data selecionada pode estar inválida.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(CalendarActivity.this, "Erro ao enviar as informações.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        iBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ApresentationActivity.class));
            }
        });
    }

}