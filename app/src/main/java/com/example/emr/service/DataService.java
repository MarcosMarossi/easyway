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

    @POST("api/screcord/")
    Call<Scheduling> newSchedule(@Body Scheduling scheduling);

    @GET("api/screcord/schedullingsByDate/5ec5c8ed66d2340007af6fae/{month}/{year}")
    Call<ArraySchedule> historicPatient(@Path( "month" ) int month, @Path( "year" ) int year);

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
