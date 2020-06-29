package com.example.emr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emr.configuration.RetrofitConfig;
import com.example.emr.model.User;
import com.example.emr.service.Authentication;
import com.example.emr.user.patient.menu.MenuUsrActivity;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private EditText edtName, edtPassword;
    private Button btnSend, btnClose;
    private TextView txtRecovery;
    private String email, password;
    private String getToken, profile;
    private Retrofit retrofit;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);

        getWindow().setStatusBarColor( Color.parseColor( "#304FFE" ));
        getSupportActionBar().hide();

        edtName = findViewById(R.id.edtNome);
        edtPassword = findViewById(R.id.edtSenha);
        btnSend = findViewById(R.id.btnEntrar);
        btnClose = findViewById(R.id.btSair);
        sharedPreferences = getSharedPreferences("salvarToken", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(getApplicationContext(), MainActivity.class ) );
                finish();
            }
        } );

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = edtName.getText().toString();
                password = edtPassword.getText().toString();

                retrofit = RetrofitConfig.retrofitConfig();

                User token = new User( email, password);

                Authentication authentication = retrofit.create( Authentication.class );
                Call<User> POST = authentication.acessApp( token );

                POST.enqueue( new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {

                        if(response.isSuccessful()){

                        User user = response.body();
                        getToken = response.body().getToken();

                        editor.putString( "email", email );
                        editor.putString( "pass", password);
                        editor.putString( "id", user.getId() );
                        editor.putString( "token", getToken );
                        editor.commit();

                        if (user.getToken() != null) {

                            Authentication authentication = retrofit.create( Authentication.class );
                            Call<User> GET = authentication.getToken( getToken );

                            GET.enqueue( new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    if (response.isSuccessful()) {

                                        profile = response.body().getProfile();
                                        String name = response.body().getName();
                                        String id = response.body().getId();
                                        String cpf = response.body().getCpf();

                                        editor.putString( "user_name", name);
                                        editor.putString( "user_email", email);
                                        editor.putString( "document", cpf);
                                        editor.putString("idPatient",id);
                                        editor.putString("id",id);
                                        editor.commit();

                                        if (profile.equals( "patient" )) {
                                            menuPaciente();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {
                                    Log.e( "ERROR", "Seu erro ocorreu aqui: " + t + " " + call );
                                }
                            } );
                        } else {
                            Toast.makeText( LoginActivity.this, "Login ou senha incorretos!", Toast.LENGTH_SHORT ).show();

                            }
                        } else {
                            Toast.makeText( LoginActivity.this, "Senha ou email inválidos! Verifique suas informações", Toast.LENGTH_SHORT ).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.e( "ERROR", "Seu erro ocorreu aqui: " + t + " " + call );
                        Toast.makeText( LoginActivity.this, "Desculpe. Não conseguimos conectar te. E-mail ou senha inválidos.", Toast.LENGTH_SHORT ).show();
                    }
                } );
            }
        } );

    getToken = sharedPreferences.getString("token", null);

    if (getToken != null) {
        edtName.setText(sharedPreferences.getString("email", null));
        edtPassword.setText(sharedPreferences.getString("pass", null));
    }
}

    public void menuPaciente() {
        startActivity(new Intent(this, MenuUsrActivity.class));
    }
}