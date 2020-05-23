package com.example.emr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.emr.adapter.AdapterCountry;
import com.example.emr.adapter.CountryItem;
import com.example.emr.configuration.RetrofitConfig;
import com.example.emr.model.User;
import com.example.emr.service.Authentication;

import java.util.ArrayList;
import java.util.Locale;

import com.example.emr.user.doctor.menu.MenuDocActivity;
import com.example.emr.user.patient.menu.MenuUsrActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private Spinner language;
    private ArrayList<CountryItem> countryList;
    private AdapterCountry adapterCountry;
    private Retrofit retrofit;
    private String getToken, Profile;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mudarIdioma();
        setContentView(R.layout.act_main);

        getWindow().setStatusBarColor( Color.parseColor( "#00897b" ));

        initList();
        language = findViewById(R.id.spinIdioma);

        adapterCountry = new AdapterCountry(this, countryList);
        language.setAdapter(adapterCountry);

        language.setSelected(false);
        language.setSelection(getLanguage(), true);


        language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CountryItem clickedItem = (CountryItem) parent.getItemAtPosition(position);
                String clickedCountryName = clickedItem.getCountryName().toLowerCase();
                alteraridioma(clickedCountryName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initList() {
        countryList = new ArrayList<>();
        countryList.add(new CountryItem("pt-BR", R.drawable.brazil));
        countryList.add(new CountryItem("en-US", R.drawable.usa3));
    }

    public void abrirTelaLogin(View v) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void abrirTelaCadastro(View v) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void mudarIdioma()
    {
        SharedPreferences dados = getSharedPreferences("emr-language", MODE_PRIVATE);
        String lang = dados.getString("idioma", getResources().getConfiguration().locale.getLanguage());

        Locale idioma = new Locale(lang);
        Locale.setDefault(idioma);

        Context context = this;
        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());

        config.setLocale(idioma);
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

    public void alteraridioma(String idioma) {

        Locale locale;
        if(idioma.equals("pt-br"))
            locale = new Locale("pt");
        else if(idioma.equals("usa"))
            locale = new Locale("en");
        else
            locale = new Locale(idioma);

        Locale.setDefault(locale);

        Context context = this;
        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());

        config.setLocale(locale);
        res.updateConfiguration(config, res.getDisplayMetrics());

        SharedPreferences.Editor dados = getSharedPreferences("emr-language", MODE_PRIVATE).edit();
        dados.putString("idioma", getResources().getConfiguration().locale.getLanguage());
        dados.commit();

        finish();
        startActivity(new Intent(this,MainActivity.class));
    }


    public int getLanguage(){
        SharedPreferences dados = getSharedPreferences("emr-language",MODE_PRIVATE);
        String result = dados.getString("idioma","");
        switch(result){
            case "pt":
                return 0;
            case "en":
                return 1;
        }
        return 1;
    }

    public void menuPaciente() {
        Intent intent = new Intent(this, MenuUsrActivity.class);
        startActivity(intent);
    }

    public void menuMedico() {
        Intent intent = new Intent(this, MenuDocActivity.class);
        startActivity(intent);
    }

    public void authentication(){
        retrofit = RetrofitConfig.retrofitConfig();

        sharedPreferences = getSharedPreferences("salvarToken", MODE_PRIVATE);
        getToken = sharedPreferences.getString("token", null);
        editor = sharedPreferences.edit();

        Authentication authentication = retrofit.create(Authentication.class);
        Call<User> call = authentication.getToken(getToken);

        call.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {

                    Profile = response.body().getProfile();
                    String name = response.body().getName();
                    String email = response.body().getEmail();
                    String cpf = response.body().getCpf();
                    editor.putString( "user_name", name);
                    editor.putString( "user_email", email);
                    editor.putString( "document", cpf);
                    editor.commit();

                    if (Profile.equals("patient") && !getToken.equals("")) {
                        menuPaciente();
                    } else if (Profile.equals("medic") && !getToken.equals("")) {
                        menuMedico();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Error", "Ocorreu um erro: " + t);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        authentication();
    }
}
