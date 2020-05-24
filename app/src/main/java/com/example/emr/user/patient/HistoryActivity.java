package com.example.emr.user.patient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.emr.helper.RecyclerItemClickListener;
import com.example.emr.adapter.ScheduleAdapter;
import com.example.emr.configuration.RetrofitConfig;
import com.example.emr.model.Scheduling;
import com.example.emr.model.json.ArraySchedule;
import com.example.emr.R;
import com.example.emr.service.Patient;
import com.example.emr.user.patient.schedule.Slide01Activity;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HistoryActivity extends AppCompatActivity {

    private MaterialCalendarView materialCalendarView;
    private FloatingActionButton fabSchedule;
    private Retrofit retrofit;
    private ArraySchedule arraySchedule;
    private String monthSelected, yearSelected, idPatient, status;
    private Patient service;
    private RecyclerView recyclerView;
    private List<Scheduling> list, listSchedules= new ArrayList<>(  );
    private ScheduleAdapter scheduleAdapter;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_historic);

        sharedPreferences = getSharedPreferences("salvarToken", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        recyclerView = findViewById( R.id.recycler );
        fabSchedule = findViewById(R.id.fabAgendar);
        idPatient = sharedPreferences.getString("idPatient", null);

        materialCalendarView = findViewById(R.id.calHistorico);
        materialCalendarView.state().edit()
                .setMaximumDate(CalendarDay.from(2020,1,1))
                .setMaximumDate(CalendarDay.from(2020,12,30))
                .commit();

        CalendarDay calendarDay = materialCalendarView.getCurrentDate();
        monthSelected = String.format( "%02d", (calendarDay.getMonth()+1) );
        yearSelected = Integer.toString( calendarDay.getYear());

        materialCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

                monthSelected = String.format( "%02d", (date.getMonth()+1) );
                yearSelected =  Integer.toString( date.getYear() );
                Toast.makeText(getApplicationContext(), monthSelected + "/" +yearSelected, Toast.LENGTH_SHORT).show();
                getItems();

            }
        });

        fabSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( HistoryActivity.this, Slide01Activity.class));
            }
        });

        getItems();
    }

    public void getItems(){
        retrofit = RetrofitConfig.retrofitConfig();
        service = retrofit.create( Patient.class);
        Call<ArraySchedule> call = service.historicPatient(idPatient, Integer.parseInt(monthSelected), Integer.parseInt(yearSelected));

        call.enqueue( new Callback<ArraySchedule>() {
            @Override
            public void onResponse(Call<ArraySchedule> call, Response<ArraySchedule>response) {
                arraySchedule = response.body();
                list = arraySchedule.schedules;
                for (int i = 0; i < list.size(); i++) {
                    if(list.get(i).getStatus().contains("Agendado")){
                        listSchedules.add(list.get(i));
                    }
                }
                recyclerView(listSchedules);
            }

            @Override
            public void onFailure(Call<ArraySchedule> call, Throwable t) {

            }
        });
    }

    public void recyclerView(List<Scheduling> list) {

        scheduleAdapter = new ScheduleAdapter(list, this);
        recyclerView.setHasFixedSize( true );
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter( scheduleAdapter );

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                scheduleAdapter.notifyDataSetChanged();

                                Scheduling scheduling = listSchedules.get(position);
                                idPatient = scheduling.get_id();
                                status = scheduling.getStatus();
                                editor.putString( "idRecord", idPatient);
                                editor.commit();

                                if (status.equals( "Agendado" )){
                                    Toast.makeText( HistoryActivity.this, "Não é possível consultar. Status definido como agendado.", Toast.LENGTH_SHORT ).show();
                                } else {
                                    startActivity( new Intent( getApplicationContext(), RecordUserActivity.class ) );
                                }

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {}

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {}
                        }
                )
        );
    }
}
