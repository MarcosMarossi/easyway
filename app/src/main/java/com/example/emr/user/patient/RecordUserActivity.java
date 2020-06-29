package com.example.emr.user.patient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.emr.configuration.RetrofitConfig;
import com.example.emr.model.json.Schedule;
import com.example.emr.R;
import com.example.emr.service.Patient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RecordUserActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private SharedPreferences sharedPreferences;
    private TextView txtSpeciality, txtDoctor, txtData, txtSintomas, txtStatus, txtNotas, txtDiagnostico, txtCid;
    private String idSchedule;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.act_record_details_user);

        txtDoctor = findViewById( R.id.txtMedico );
        txtSpeciality = findViewById( R.id.txtEspecialidade );
        txtData = findViewById( R.id.txtData );
        txtSintomas = findViewById( R.id.txtSintomas );
        txtStatus = findViewById( R.id.txtStatus );
        txtNotas = findViewById( R.id.txtNotas );
        txtDiagnostico = findViewById( R.id.txtDiagnosticos );
        txtCid = findViewById( R.id.txtCid );

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
                txtSintomas.setText( s.schedules.symptoms.get(0) );
                txtCid.setText( s.schedules.cids.get(0) );
                txtStatus.setText( s.schedules.getStatus() );
                txtNotas.setText( s.schedules.getMedicNotes() );
                txtDiagnostico.setText( s.schedules.getDiagnosis() );
            }
            @Override
            public void onFailure(Call<Schedule> call, Throwable t) {

            }
        } );
    }
}
