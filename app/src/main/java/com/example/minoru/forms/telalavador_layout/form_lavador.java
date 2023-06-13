package com.example.minoru.forms.telalavador_layout;

import static java.security.AccessController.getContext;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.minoru.R;
import com.example.minoru.adapter.adapterLavagem;
import com.example.minoru.adapter.adapterVeiculoFragment2;
import com.example.minoru.forms.tela_login;
import com.example.minoru.models.lavagens;
import com.example.minoru.models.veiculos;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class form_lavador extends AppCompatActivity {

    private CardView lavagens, sair;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private adapterLavagem adapter;
    private RecyclerView recy;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_form_lavador);


        iniciar();
        iniciarRecyclerView();


        lavagens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(form_lavador.this, form_lavagens.class);
                startActivity(intent);
                finish();
            }
        });

        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(form_lavador.this, tela_login.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.startListening();
    }

    private void iniciarRecyclerView() {

        CollectionReference dadosFina = db.collection("Veiculo").document("DIAS")
                .collection(dateCapter());

        Query query = dadosFina.whereEqualTo("lavagem",true);       //orderBy("ordemLavagem", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<lavagens> options = new FirestoreRecyclerOptions.Builder<com.example.minoru.models.lavagens>()
                .setQuery(query, com.example.minoru.models.lavagens.class)
                .build();

        adapter = new adapterLavagem(options);
        RecyclerView recyclerView = findViewById(R.id.recyFun);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new adapterLavagem.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                documentSnapshot.toObject(veiculos.class);
                id = documentSnapshot.getId();
                openDialog();
            }
        });
    }

    private void openDialog() {
        Dialog dialog = new Dialog(this,R.style.CustomAlertDialog);
        dialog.setContentView(R.layout.dialog_confirmacao);
        dialog.show();
        frontDialog(dialog);

    }

    private void frontDialog(Dialog dialog) {
        TextView statusT = dialog.findViewById(R.id.textView14);
        TextView ModeloT = dialog.findViewById(R.id.textView11);
        TextView placaT = dialog.findViewById(R.id.textView12);
        Button finalizado = dialog.findViewById(R.id.bt_sim);
        Button cancel = dialog.findViewById(R.id.bt_nao);


        db.collection("Veiculo").document("DIAS")
                .collection(dateCapter()).document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot doc = task.getResult();
                            if(doc.exists() && doc != null){
                                ModeloT.setText(doc.getString("modelo"));
                                placaT.setText(doc.getString("placa"));
                                statusT.setText(doc.getString("statusLavagem"));
                            }
                        }
                    }
                });


        finalizado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object>Dados =new HashMap<>();
                Dados.put("statusLavagem","FINALIZADO");
                Dados.put("lavagem",false);

                db.collection("Veiculo").document("DIAS")
                        .collection(dateCapter()).document(id).update(Dados).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    dialog.dismiss();
                                }
                            }
                        });

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
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

    public void iniciar() {
        lavagens = findViewById(R.id.ic_lavagens);
        sair = findViewById(R.id.ic_sair);
        recy = findViewById(R.id.recyFun);
    }
}