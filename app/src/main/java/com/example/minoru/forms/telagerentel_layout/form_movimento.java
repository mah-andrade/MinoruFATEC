package com.example.minoru.forms.telagerentel_layout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.minoru.R;
import com.example.minoru.adapter.adapterVeiculoFragment2;
import com.example.minoru.models.veiculosF2;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;

public class form_movimento extends AppCompatActivity {
    private ImageView voltar;
    private TextView vagasocupadas, vagasdisponiveis;
    private adapterVeiculoFragment2 adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String id, bd;


    private Integer contMensal =0, contClientes =0, contFinal =0;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_form_movimento);
        IniciarComponentes();
        iniciarRecyclerView();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                contFinal = 0 ;
                contClientes = 0;
                contMensal = 0 ;


                db.collection("Mensalistas").whereEqualTo("contrato", true).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            contMensal++;Log.i("TAPORRA","mensal");
                        }
                    }
                });



                db.collection("Veiculo").document("DIAS").collection(dateCapter()).whereEqualTo("status",true).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            contClientes++;
                            Log.i("TAPORRA","AQUII");
                        }
                    }
                });

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        contFinal = contClientes + contMensal;
                        vagasdisponiveis.setText(String.valueOf(45 - contFinal));
                        vagasocupadas.setText(String.valueOf(contFinal));
                    }
                }, 2000);

                swipeRefreshLayout.setRefreshing(false);
            }
        });


        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(form_movimento.this, tela_gerente.class);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();



       db.collection("Mensalistas").whereEqualTo("contrato", true).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
           @Override
           public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
               for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                   contMensal++;Log.i("TAPORRA","mensal");
               }
           }
       });



        db.collection("Veiculo").document("DIAS").collection(dateCapter()).whereEqualTo("status",true).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    contClientes++;
                    Log.i("TAPORRA","AQUII");
                }
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                contFinal = contClientes + contMensal;
                vagasdisponiveis.setText(String.valueOf(45 - contFinal));
                vagasocupadas.setText(String.valueOf(contFinal));
            }
        }, 2000);

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void iniciarRecyclerView() {
        CollectionReference dadosFina = db.collection("Veiculo").document("DIAS")
                .collection(dateCapter());

        Query query = dadosFina.whereEqualTo("status", false).orderBy("nome", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<veiculosF2> options = new FirestoreRecyclerOptions.Builder<veiculosF2>()
                .setQuery(query, veiculosF2.class)
                .build();

        adapter = new adapterVeiculoFragment2(options);
        RecyclerView recyclerView = findViewById(R.id.recy_vec_mov);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    public String dateCapter() {

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(calendar.DAY_OF_MONTH);
        int month = calendar.get(calendar.MONTH) + 1;
        int year = calendar.get(calendar.YEAR);

        if (day >= 10) {
            if (month >= 10) {
                String date = day + "." + month + "." + year;
                return date;
            } else {
                String date = day + ".0" + month + "." + year;
                return date;
            }
        } else {
            if (month >= 10) {
                String date = "0" + day + "." + month + "." + year;
                return date;
            } else {
                String date = "0" + day + ".0" + month + "." + year;
                return date;
            }
        }
    }

    String month() {
        Calendar calendar = Calendar.getInstance();
        int b = calendar.get(Calendar.MONTH) + 1;
        int a = calendar.get(Calendar.YEAR);
        String date = (Integer.toString(b)) + (Integer.toString(a));
        return date;
    }

    void IniciarComponentes() {
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        voltar = findViewById(R.id.voltar);
        vagasdisponiveis = findViewById(R.id.vgsocupadas);
        vagasocupadas = findViewById(R.id.vgsdisponiveis);

        //  datamov = findViewById(R.id.datamov);
    }
}
