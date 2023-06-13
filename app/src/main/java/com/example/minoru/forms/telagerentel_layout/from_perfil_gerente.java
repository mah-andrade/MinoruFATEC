package com.example.minoru.forms.telagerentel_layout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.minoru.R;
import com.example.minoru.forms.telacliente_layout.form_perfilCliente;
import com.example.minoru.forms.telacliente_layout.form_perfilCliente_edit;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.IOException;
import java.net.URI;
import java.net.URL;


public class from_perfil_gerente extends AppCompatActivity {
    private ImageView voltar, perfil;
    private Button editar;
    private  URI uri;
    private TextView nome, cpf, nascimento, endereco, telefone ,txtEmail;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_from_perfil_gerente);
        IniciarComponets();

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(from_perfil_gerente.this, tela_gerente.class);
                startActivity(intent);
                finish();
            }
        });

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(from_perfil_gerente.this, form_perfil_gerente_edit.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();


        Log.i("TAPORRA",FirebaseAuth.getInstance().getCurrentUser().getEmail());
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Gerente").document(usuarioID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if(documentSnapshot != null){
                    nome.setText(documentSnapshot.getString("nome"));
                    cpf.setText(documentSnapshot.getString("cpf"));
                    telefone.setText(documentSnapshot.getString("telefone"));
                    nascimento.setText(documentSnapshot.getString("nascimeto"));
                    endereco.setText(documentSnapshot.getString("endereco"));
                    String Url = documentSnapshot.getString("profileURL");
                    Log.i("teste", Url);
                    Glide.with(from_perfil_gerente.this).load(Url).into(perfil);

                }
            }
        });

    }



    void IniciarComponets() {
        voltar = findViewById(R.id.voltar);
        editar = findViewById(R.id.editperfil2);
        nome = findViewById(R.id.idcliente);
        cpf = findViewById(R.id.cpfcliente);
        telefone = findViewById(R.id.telcliente);
        nascimento = findViewById(R.id.datacliente);
        endereco = findViewById(R.id.enderecocliente);
        perfil = findViewById(R.id.perfil);



    }
}