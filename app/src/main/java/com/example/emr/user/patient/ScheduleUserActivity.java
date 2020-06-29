package com.example.emr.user.patient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emr.R;
import com.example.emr.configuration.RetrofitConfig;
import com.example.emr.model.json.ArraySchedule;
import com.example.emr.model.json.Schedule;
import com.example.emr.service.Patient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ScheduleUserActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private SharedPreferences sharedPreferences;
    private TextView txtSpeciality, txtDoctor, txtData, txtStatus;
    private String idSchedule;
    private Button deleteSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_historic_details_user);

        txtDoctor = findViewById( R.id.txtMedico );
        txtSpeciality = findViewById( R.id.txtEspecialidade );
        txtData = findViewById( R.id.txtData );
        txtStatus = findViewById( R.id.txtStatus );
        deleteSchedule = findViewById( R.id.deleteSchedule);

        sharedPreferences = getSharedPreferences("salvarToken", MODE_PRIVATE);
        idSchedule = sharedPreferences.getString("idRecord", null);

        retrofit = RetrofitConfig.retrofitConfig();

        Patient service = retrofit.create( Patient.class );
        Call<Schedule> call = service.scheduleById( idSchedule );

        call.enqueue( new Callback<Schedule>() {
            @Override
            public void onResponse(Call<Schedule> call, Response<Schedule> response) {

                Schedule s = response.body();
                txtDoctor.setText( s.schedules.getMedic() );
                txtSpeciality.setText( s.schedules.getSpecialty() );
                txtData.setText( s.schedules.getDate() );
                txtStatus.setText( s.schedules.getStatus() );

            }
            @Override
            public void onFailure(Call<Schedule> call, Throwable t) {

            }
        } );

        deleteSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrofit = RetrofitConfig.retrofitConfig();
                Patient service =  retrofit.create( Patient.class );
                Call<ArraySchedule> call =  service.deleteSchedule( idSchedule );

                call.enqueue( new Callback<ArraySchedule>() {
                    @Override
                    public void onResponse(Call<ArraySchedule> call, Response<ArraySchedule> response) {
                        Toast.makeText(ScheduleUserActivity.this, "Você excluiu o agendamento", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
                    }

                    @Override
                    public void onFailure(Call<ArraySchedule> call, Throwable t) {}
                } );
            }
        });
    }
}
