package com.example.minoru.forms.telagerentel_layout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.minoru.R;
import com.example.minoru.forms.tela_login;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class tela_gerente extends AppCompatActivity {

    private CardView movimento, funcionarios, precos,financas, faq, logout, perfil;
    private ImageView perfilfoto;
    private TextView nome,txtEmail;
    private String usuarioID;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_tela_gerente);
        iniciar_component();


        DocumentReference MensalistaCad = db.collection("Mensal").document(month());
        MensalistaCad.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                }else{
                    Map<Object,Integer> tClientes = new HashMap<>();
                    tClientes.put("totalClientes",0);
                    tClientes.put("valorRendimento",0);
                    tClientes.put("salariosFunc",0);
                    tClientes.put("mensalistas",0);
                    MensalistaCad.set(tClientes);
                }
            }
        });

        DocumentReference valorCarro = db.collection("ValorEstacionamento")
                .document("Carro");
        valorCarro.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){}
                        else{
                            Map<Object, Double> tPreco = new HashMap<>();
                            tPreco.put("avulso",0.0);
                            tPreco.put("diario",0.0);
                            tPreco.put("mensal",0.0);
                            valorCarro.set(tPreco);

                        }
                    }
                });

        DocumentReference valorMoto = db.collection("ValorEstacionamento").document("Moto");

        valorMoto.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                }else{
                    Map<Object, Double> tPreco = new HashMap<>();
                    tPreco.put("avulso",0.0);
                    tPreco.put("diario",0.0);
                    tPreco.put("mensal",0.0);
                    valorMoto.set(tPreco);
                }
            }
        });


        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(tela_gerente.this, from_perfil_gerente.class);
                startActivity(intent);
            }
        });

        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(tela_gerente.this, form_ajudagerente.class);
                startActivity(intent);
            }
        });

        financas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(tela_gerente.this, form_financas.class);
                startActivity(intent);
            }
        });

        movimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(tela_gerente.this, form_movimento.class);
                startActivity(intent);
            }
        });

        precos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(tela_gerente.this, form_precos.class);
                startActivity(intent);
            }
        });

        funcionarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(tela_gerente.this, form_funcionarios.class);
                startActivity(intent);
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(tela_gerente.this,tela_login.class);
                startActivity(intent);
                finish();
            }
        });
    }



     String month(){
        Calendar calendar = Calendar.getInstance();
        int b = calendar.get(Calendar.MONTH)+1;
        int a = calendar.get(Calendar.YEAR);
        String date = (Integer.toString(b))+(Integer.toString(a));
        return date ;
    }

    @Override
    protected void onStart() {
        super.onStart();
        txtEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Gerente").document(usuarioID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if(documentSnapshot != null){
                    nome.setText(documentSnapshot.getString("nome"));

                    String Url = documentSnapshot.getString("profileURL");
                    Log.i("teste", Url);
                    Glide.with(tela_gerente.this).load(Url).into(perfilfoto);


                }
            }
        });

    }





    void iniciar_component(){
        financas = findViewById(R.id.ic_financas);
        movimento = findViewById(R.id.ic_movimento);
        precos = findViewById(R.id.ic_preços);
        funcionarios = findViewById(R.id.ic_Funcionarios);
        logout = findViewById(R.id.sair);
        faq = findViewById(R.id.Faq);
        perfil = findViewById(R.id.perfilCard);
        perfilfoto = findViewById(R.id.perfil);
        nome = findViewById(R.id.gerente_id);
        txtEmail = findViewById(R.id.txtemail);
    }
}