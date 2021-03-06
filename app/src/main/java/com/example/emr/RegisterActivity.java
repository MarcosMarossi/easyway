package com.example.emr;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.emr.configuration.RetrofitConfig;
import com.example.emr.helper.MaskEditUtil;
import com.example.emr.model.User;
import com.example.emr.service.Authentication;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName,etCPF,edtEmail,edtPassword;
    private Button btnCadastrar, btSair;
    private String token, email, password, cpf, name;
    Retrofit retrofit;
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_register);

        getSupportActionBar().hide();
        getWindow().setStatusBarColor( Color.parseColor( "#304FFE" ));

        etName = findViewById(R.id.edtNome);
        etCPF = findViewById(R.id.edtCPF);
        etCPF.addTextChangedListener( MaskEditUtil.mask(etCPF, MaskEditUtil.FORMAT_CPF));
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtSenha);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        btSair = findViewById(R.id.btSair2);

        btSair.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(getApplicationContext(), MainActivity.class ) );
                finish();
            }
        } );

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                retrofit = RetrofitConfig.retrofitConfig();

                name = etName.getText().toString();
                cpf = etCPF.getText().toString();

                email = edtEmail.getText().toString();
                password = edtPassword.getText().toString();

                if(email.contains("@") && password.length() >= 8) {
                    User register = new User(name,cpf,email,password);

                    Authentication authentication = retrofit.create(Authentication.class);
                    Call<User> POST = authentication.registerNewUser(register);

                    POST.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            User register = response.body();
                            token = register.getToken();
                            Toast.makeText(RegisterActivity.this, R.string.validation_register, Toast.LENGTH_SHORT).show();
                            login();
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, R.string.validation_register_error, Toast.LENGTH_SHORT).show();
                }                
            }
        });
    }

    public void login(){
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }
}