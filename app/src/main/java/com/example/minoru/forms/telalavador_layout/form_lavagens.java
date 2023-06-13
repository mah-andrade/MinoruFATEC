package com.example.minoru.forms.telalavador_layout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.minoru.R;
import com.example.minoru.adapter.adapterLavagem;
import com.example.minoru.models.lavagens;
import com.example.minoru.models.veiculos;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Calendar;

public class form_lavagens extends AppCompatActivity {

    private ImageView voltar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private adapterLavagem adapter;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_form_lavagens);
        iniciar();
        iniciarRecyclerView();

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(form_lavagens.this, form_lavador.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void iniciarRecyclerView() {

        CollectionReference dadosFina = db.collection("Veiculo").document("DIAS")
                .collection(dateCapter());

        Query query = dadosFina.whereEqualTo("lavagem",false);            //orderBy("ordemLavagem", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<lavagens> options = new FirestoreRecyclerOptions.Builder<com.example.minoru.models.lavagens>()
                .setQuery(query, com.example.minoru.models.lavagens.class)
                .build();

        adapter = new adapterLavagem(options);
        RecyclerView recyclerView = findViewById(R.id.recyFun);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

       /* adapter.setOnItemClickListener(new adapterLavagem.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                documentSnapshot.toObject(veiculos.class);
                id = documentSnapshot.getId();
                //openDialog();
            }
        });*/
    }

    @Override
    protected void onStart() {
        super.onStart();
          adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    void iniciar() {
       voltar = findViewById(R.id.voltar);
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
}