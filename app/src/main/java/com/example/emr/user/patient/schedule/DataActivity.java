package com.example.emr.user.patient.schedule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.emr.R;
import com.example.emr.configuration.RetrofitConfig;
import com.example.emr.model.User;
import com.example.emr.service.Patient;
import com.example.emr.user.patient.HistoryActivity;
import com.example.emr.user.patient.PasswordActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DataActivity extends AppCompatActivity {

    private EditText etOldPassword, etNewPassword;
    private Button btAlterar, btnSair;
    private Retrofit retrofit;
    private SharedPreferences sharedPreferences;
    private String email, senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_password);
        sharedPreferences = getSharedPreferences("salvarToken", MODE_PRIVATE);

        etOldPassword = findViewById( R.id.etOldPassword );
        etNewPassword = findViewById( R.id.etNewPassword );
        btAlterar = findViewById( R.id.btnChange);

        email = sharedPreferences.getString("email", null);
        senha = sharedPreferences.getString("pass", null);

//        btAlterar.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity( new Intent( getApplicationContext(), DataActivity.class ) );
//            }
//        } );

//        btAlterar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String OldPassword = etOldPassword.getText().toString();
//                String NewPassword = etNewPassword.getText().toString();
//
//                User change = new User( email, senha, NewPassword, OldPassword,0);
//
//                retrofit = RetrofitConfig.retrofitConfig();
//                Patient service = retrofit.create( Patient.class );
//                Call<User> call = service.resetPassword( change );
//
//                call.enqueue( new Callback<User>() {
//                    @Override
//                    public void onResponse(Call<User> call, Response<User> response) {
//                        if(response.isSuccessful()){
//                            Toast.makeText( getApplicationContext() , "Senha alterada.", Toast.LENGTH_SHORT ).show();
//
//                        } else{
//                            Toast.makeText( getApplicationContext(), "Senhas não correspondem!", Toast.LENGTH_SHORT ).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<User> call, Throwable t) { }
//                } );
//            }
//        });

    }
}
