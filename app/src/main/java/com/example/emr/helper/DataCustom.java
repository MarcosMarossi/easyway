package com.example.emr.helper;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DataCustom {

    public static String dataCorreta(int dia, int mes, int ano){
        String diaCorreto = dia > 9 ? "" + dia : "0" + dia;
        String mesCorreto = mes > 9 ? "" + mes : "0" + mes;
        System.out.println("sdfgbdsadffdsfvgbdsgfbvd" + diaCorreto +  mesCorreto);
        return diaCorreto + "/" + mesCorreto + "/" + ano;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean dataValida (String data) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(data, dtf).compareTo(LocalDate.now()) >= 0;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean horaValida(String hour) {
        SimpleDateFormat formato = new SimpleDateFormat("HH:mm");
        LocalTime localTime = LocalTime.parse(formato.format(new Date((hour))));
        System.out.println(localTime.toString());
        return localTime.isAfter(LocalTime.parse(formato.format(new Date("08:00"))));
    }
}
