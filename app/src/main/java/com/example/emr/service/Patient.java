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

public interface Patient {

    @POST("api/screcord/")
    Call<Scheduling> newSchedule(@Body Scheduling scheduling);

    @GET("api/screcord/schedullingsByDate/{id}/{month}/{year}")
    Call<ArraySchedule> historicPatient(@Path("id") String id, @Path( "month" ) int month, @Path( "year" ) int year);

    @GET("api/doctors/showBySpecialty/{speciality}")
    Call<Result> getDoctors(@Path( "speciality") String speciality);

    @DELETE("api/screcord/{id}")
    Call<ArraySchedule> deleteSchedule(@Path("id") String id);

    @POST("auth/jwt/reset")
    Call<User> resetPassword(@Body User user);

    @GET("api/screcord/{id}")
    Call<Schedule> scheduleById(@Path( "id" ) String id);

    @GET("api/screcord/showSchedullingsByMedicName/{medic}")
    Call<Schedules> scheduleByName(@Path( "medic" ) String medic);
}
