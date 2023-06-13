package com.example.minoru.forms.telacliente_layout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.minoru.R;
import com.example.minoru.forms.telagerentel_layout.from_perfil_gerente;
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

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;

public class form_perfilCliente extends AppCompatActivity {
    private ImageView voltar,perfil;
    private Button editar;
    private String usuarioID;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView nome, cpf, telefone, nascimento, endereco, modelo, placa;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_perfil_cliente);
        getSupportActionBar().hide();
        IniciarComponentes();

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(form_perfilCliente.this, form_perfilCliente_edit.class);
                startActivity(intent);
                finish();
            }
        });





        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(form_perfilCliente.this, form_cliente.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();



        db.collection("Mensalistas").whereEqualTo("email",mAuth.getCurrentUser().getEmail()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                usuarioID = document.getId();
                            }
                            DocumentReference documentReference = db.collection("Mensalistas").document(usuarioID);
                            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                                    if(documentSnapshot != null){
                                        nome.setText(documentSnapshot.getString("nome"));
                                        cpf.setText(documentSnapshot.getString("cpf"));
                                        telefone.setText(documentSnapshot.getString("tel"));
                                        nascimento.setText(documentSnapshot.getString("dataNascimento"));
                                        endereco.setText(documentSnapshot.getString("endereco"));
                                        placa.setText(documentSnapshot.getString("placa"));
                                        modelo.setText(documentSnapshot.getString("modelo"));
                                        String Url = documentSnapshot.getString("profileURL");
                                        if(Url != ""){
                                            Glide.with(form_perfilCliente.this).load(Url).into(perfil);
                                        }

                                    }
                                }
                            });

                        }
                    }
                });

       /* DocumentReference documentReference = db.collection("Mensalistas").document(usuarioID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if(documentSnapshot != null){
                    nome.setText(documentSnapshot.getString("nome"));
                    cpf.setText(documentSnapshot.getString("cpf"));
                    telefone.setText(documentSnapshot.getString("telefone"));
                    nascimento.setText(documentSnapshot.getString("nascimento"));
                    endereco.setText(documentSnapshot.getString("endereco"));
                    placa.setText(documentSnapshot.getString("placa"));
                    modelo.setText(documentSnapshot.getString("modelo"));
                    String Url = documentSnapshot.getString("profileURL");
                    Glide.with(form_perfilCliente.this).load(Url).into(perfil);
                }
            }
        });*/

    }



    public void IniciarComponentes(){

        voltar = findViewById(R.id.voltar);
        editar = findViewById(R.id.editperfil);
        nome = findViewById(R.id.idcliente);
        cpf = findViewById(R.id.cpfcliente);
        telefone = findViewById(R.id.telcliente);
        nascimento = findViewById(R.id.datacliente);
        endereco = findViewById(R.id.enderecocliente);
        modelo = findViewById(R.id.modelocliente);
        placa = findViewById(R.id.placacliente);
        perfil = findViewById(R.id.perfil);

    }
}