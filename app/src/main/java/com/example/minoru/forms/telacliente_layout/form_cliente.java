package com.example.minoru.forms.telacliente_layout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.minoru.R;
import com.example.minoru.adapter.adapterContrato;
import com.example.minoru.forms.tela_login;
import com.example.minoru.forms.telagerentel_layout.tela_gerente;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class form_cliente extends AppCompatActivity {
   // ImageView sair;
    CardView sair, faq, perfil;
    CardView contrato, sobre;
    private adapterContrato adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ImageView perfilfoto;
    private FirebaseAuth mAuth;
    private String id, emailUser;
    private TextView nomeCl,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cliente);
        mAuth = FirebaseAuth.getInstance();
        iniciar_componentes();
        getSupportActionBar().hide();

        db.collection("Mensalistas").whereEqualTo("email",mAuth.getCurrentUser().getEmail()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                nomeCl.setText(document.getString("nome"));
                                email.setText(document.getString("email"));
                                id = document.getId();
                            }
                        }
                    }
                });



        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(form_cliente.this,Form_ajuda.class);
                startActivity(intent);
            }
        });


        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(form_cliente.this,form_perfilCliente.class);
                startActivity(intent);
            }
        });

        sobre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(form_cliente.this,form_sobre.class);
                startActivity(intent);
            }
        });

        contrato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(form_cliente.this,form_contrato.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(form_cliente.this, tela_login.class);
                startActivity(intent);
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();

        emailUser= FirebaseAuth.getInstance().getCurrentUser().getEmail();

        db.collection("Mensalistas").whereEqualTo("email", emailUser).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        DocumentReference documentFulano = document.getReference();
                        documentFulano.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                                String url = value.getString("profileURL");
                                Log.i("urlDomain",url);
                                 if(url != ""){
                                    Glide.with(form_cliente.this).load(url).into(perfilfoto);
                                }
                            }
                        });
                    }
                }
            }
        });

    }

   /* @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }*/

   /* private void iniciarRecyclerView() {
        CollectionReference dadosF = db.collection("Mensalistas");
        Query query = dadosF.whereEqualTo("email",mAuth.getCurrentUser().getEmail());
        FirestoreRecyclerOptions<veiculos> options = new FirestoreRecyclerOptions.Builder<veiculos>()
                .setQuery(query,veiculos.class)
                .build();

        adapter = new adapterContrato(options);
        RecyclerView recyclerView = findViewById(R.id.recyC);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new adapterContrato.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                documentSnapshot.toObject(veiculos.class);
                id = documentSnapshot.getId();
                Intent intent = new Intent(form_cliente.this,form_contrato.class);
                intent.putExtra("Id",id);
                startActivity(intent);
                finish();
            }
        });
    }*/

    public void iniciar_componentes(){
        sair = findViewById(R.id.Cardsair);
        nomeCl = findViewById(R.id.cliente_id);
        perfil = findViewById(R.id.perfilCard);
        contrato = findViewById(R.id.contratoCard);
        sobre = findViewById(R.id.contatoCard);
        faq = findViewById(R.id.duvidaCard);
        perfilfoto = findViewById(R.id.perfil);
        email = findViewById(R.id.cliente_email);
    }
}