package com.example.emr.user.patient;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.emr.adapter.ScheduleAdapter;
import com.example.emr.configuration.RetrofitConfig;
import com.example.emr.model.Scheduling;
import com.example.emr.model.json.ArraySchedule;
import com.example.emr.R;
import com.example.emr.service.Patient;
import com.example.emr.user.patient.schedule.RecordUserActivity;
import com.example.emr.user.patient.schedule.Slide01Activity;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HistoryActivity extends AppCompatActivity {

    private MaterialCalendarView calendario;
    private FloatingActionButton fabAgendar;
    private Retrofit retrofit;
    private ArraySchedule arraySchedule;
    private String mesSelecionado, anoSelecionado, idPatient, status;
    private Patient service;
    private RecyclerView recyclerView;
    private List<Scheduling> fotodope = new ArrayList<>(  );
    private ScheduleAdapter scheduleAdapter;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_calendar);

        sharedPreferences = getSharedPreferences("salvarToken", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        recyclerView = findViewById( R.id.recycler );
        fabAgendar = findViewById(R.id.fabAgendar);
        idPatient = sharedPreferences.getString("idPatient", null);

        calendario = findViewById(R.id.calHistorico);
        calendario.state().edit()
                .setMaximumDate(CalendarDay.from(2020,1,1))
                .setMaximumDate(CalendarDay.from(2020,12,30))
                .commit();

        CalendarDay calendarDay = calendario.getCurrentDate();
        mesSelecionado = String.format( "%02d", (calendarDay.getMonth()+1) );
        anoSelecionado = Integer.toString( calendarDay.getYear());

        calendario.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

                mesSelecionado = String.format( "%02d", (date.getMonth()+1) );
                anoSelecionado =  Integer.toString( date.getYear() );
                getItems();

            }
        });

        fabAgendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( HistoryActivity.this, Slide01Activity.class));
            }
        });


        swipe();
        getItems();
    }

    public void getItems(){

        Toast.makeText(this, "data" + mesSelecionado + anoSelecionado, Toast.LENGTH_SHORT).show();

        retrofit = RetrofitConfig.retrofitConfig();
        service = retrofit.create( Patient.class);
        Call<ArraySchedule> call = service.historicPatient(Integer.parseInt(mesSelecionado),Integer.parseInt(anoSelecionado));

        call.enqueue( new Callback<ArraySchedule>() {
            @Override
            public void onResponse(Call<ArraySchedule> call, Response<ArraySchedule>response) {
                arraySchedule = response.body();
                fotodope = arraySchedule.schedules;

                recyclerView(fotodope);
            }

            @Override
            public void onFailure(Call<ArraySchedule> call, Throwable t) {

            }
        });
    }

    public void recyclerView(List<Scheduling> list) {

        scheduleAdapter = new ScheduleAdapter(fotodope, this);
        recyclerView.setHasFixedSize( true );
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter( scheduleAdapter );

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                scheduleAdapter.notifyDataSetChanged();

                                Scheduling scheduling = fotodope.get(position);
                                idPatient = scheduling.get_id();
                                status = scheduling.getStatus();
                                editor.putString( "idRecord", idPatient);
                                editor.commit();

                                if (status.equals( "Agendado" )){
                                    Toast.makeText( HistoryActivity.this, "Não é possível consultar. Status definido como agendado.", Toast.LENGTH_SHORT ).show();
                                } else {
                                    startActivity( new Intent( getApplicationContext(), RecordUserActivity.class ) );
                                }

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );
    }

    public void swipe(){

        ItemTouchHelper.Callback item = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags( dragFlags, swipeFlags );
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                removeSchedule( viewHolder );
            }
        };

        new ItemTouchHelper( item ).attachToRecyclerView( recyclerView );
    }

    public void removeSchedule(final RecyclerView.ViewHolder viewHolder){
        final AlertDialog.Builder alert = new AlertDialog.Builder( this );
        alert.setTitle( "Exlcuir movimentação" );
        alert.setMessage( "Tem certeza que deseja exlcuir a movimentação?" );
        alert.setCancelable( false );

        alert.setPositiveButton( "Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                int pos = viewHolder.getAdapterPosition();
                Scheduling schedule = fotodope.get(pos);

                status = schedule.getStatus();

                if (status.equals( "Medicado" )){

                    Toast.makeText( HistoryActivity.this, "Não é possível excluir. Prontuário já cadastrado pelo médico.", Toast.LENGTH_SHORT ).show();
                    scheduleAdapter.notifyDataSetChanged();
                } else {

                    idPatient = schedule.get_id();
                    scheduleAdapter.notifyItemRemoved(pos);

                    retrofit = RetrofitConfig.retrofitConfig();
                    Patient service =  retrofit.create( Patient.class );
                    Call<ArraySchedule> call =  service.deleteSchedule(idPatient);

                    call.enqueue( new Callback<ArraySchedule>() {
                        @Override
                        public void onResponse(Call<ArraySchedule> call, Response<ArraySchedule> response) {

                        }

                        @Override
                        public void onFailure(Call<ArraySchedule> call, Throwable t) {

                        }
                    } );

                    scheduleAdapter.notifyItemRemoved(pos);
                    getItems();

                }


            }
        } );

        alert.setNegativeButton( "Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText( getApplicationContext(), "Exclusão cancelada", Toast.LENGTH_SHORT ).show();
                scheduleAdapter.notifyDataSetChanged();
            }
        } );

        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }
}
