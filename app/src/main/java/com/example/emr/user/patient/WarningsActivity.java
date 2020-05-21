package com.example.emr.user.patient;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.emr.adapter.ScheduleAdapter;
import com.example.emr.model.Scheduling;
import com.example.emr.model.json.ArraySchedule;
import com.example.emr.R;
import com.example.emr.service.Patient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class WarningsActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private ArraySchedule arraySchedule;
    private Patient service;
    private RecyclerView recyclerView;
    private List<Scheduling> fotodope = new ArrayList<>(  );
    private ScheduleAdapter scheduleAdapter;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_warnings);
    }


}
