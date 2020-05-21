package com.example.emr.service;

import com.example.emr.model.json.Result;
import com.example.emr.model.json.Schedule;
import com.example.emr.model.json.Schedules;
import com.example.emr.model.Scheduling;
import com.example.emr.model.json.ArraySchedule;
import com.example.emr.model.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface DataService {

    @POST("auth/jwt/login")
    Call<User> acessApp(@Body User token);


    @GET("auth/jwt/me")
    Call<User> getToken(@Header("x-access-token") String Token);

    @POST("auth/jwt/resetpassword")
    Call<User> resetEmail(@Body User email);


    @POST("auth/jwt/register")
    Call<User> registerNewUser(@Body User register);

    // Records

    @POST("api/screcord/")
    Call<Scheduling> newSchedule(@Body Scheduling scheduling);

    @GET("api/screcord/schedullingsByDates/{month}/{year}")
    Call<ArraySchedule> historicPatient(@Path( "month" ) String month, @Path( "year" ) String year);

    @GET("api/doctors/showBySpecialty/{speciality}")
    Call<Result> getDoctors(@Path( "speciality") String speciality);

    @DELETE("api/screcord/{id}")
    Call<ArraySchedule> deleteSchedule(@Path("id") String id);

    @GET("api/patients")
    Call<ArrayList<User>> getAllPatients();

    @POST("misc/heartbeat")
    Call<User> setBPMUser(@Body User user);

    @GET("api/heartbeatlog/{id}")
    Call<ArrayList<User>> getLog(@Path("id") String id);

    @POST("auth/jwt/reset")
    Call<User> resetPassword(@Body User user);


    @GET("api/screcord/{id}")
    Call<Schedule> scheduleById(@Path( "id" ) String id);

    @GET("api/patients/{id}")
    Call<Schedules> patientById(@Path( "id" ) String id);

    @GET("api/screcord/showSchedullingsByMedicName/{medic}")
    Call<Schedules> scheduleByName(@Path( "medic" ) String medic);

    @GET("api/patients/showByCPF/{cpf}")
    Call<ArrayList<User>> getPatientCPF(@Path("cpf") String cpf);
}
