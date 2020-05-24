package com.example.emr.user.patient;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PasswordActivity extends AppCompatActivity {

    private EditText edtNewPassword, edtConfirmPassword;
    private Button btChange;
    private Retrofit retrofit;
    private SharedPreferences sharedPreferences;
    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_password);

        sharedPreferences = getSharedPreferences("salvarToken", MODE_PRIVATE);

        edtNewPassword = findViewById(R.id.etOldPassword);
        edtConfirmPassword = findViewById(R.id.etNewPassword);
        btChange = findViewById(R.id.btnChange);

        btChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = sharedPreferences.getString("email", null);
                password = sharedPreferences.getString("pass", null);

                String OldPassword = edtNewPassword.getText().toString();
                String NewPassword = edtConfirmPassword.getText().toString();

                User change = new User(email, password, NewPassword, OldPassword, 0);

                retrofit = RetrofitConfig.retrofitConfig();
                Patient service = retrofit.create(Patient.class);
                Call<User> call = service.resetPassword(change);

                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(PasswordActivity.this, "Senha alterada.", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(PasswordActivity.this, "Senhas não correspondem!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                    }
                });
            }
        });
    }
}