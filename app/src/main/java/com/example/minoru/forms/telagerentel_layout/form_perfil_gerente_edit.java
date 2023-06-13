package com.example.minoru.forms.telagerentel_layout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.minoru.R;
import com.example.minoru.forms.telacliente_layout.form_cliente;
import com.example.minoru.forms.telacliente_layout.form_perfilCliente_edit;
import com.example.minoru.models.validacoes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.santalu.maskara.widget.MaskEditText;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class form_perfil_gerente_edit extends AppCompatActivity {
    private ImageView voltar;
    private TextView editimg,nomeText,dataText,enderecoText;
    private Button salvar;

    private MaskEditText cpfMask,telMask;
    private String usuarioID = FirebaseAuth.getInstance().getUid();
    private Uri  imguri;
    private Map<String, Object> dadosGerente = new HashMap<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ImageView imguser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_form_perfil_gerente_edit);
        IniciarComponets();





        editimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectphoto();
            }
        });


        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(form_perfil_gerente_edit.this, from_perfil_gerente.class);
                startActivity(intent);
                finish();
            }
        });

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imguri != null){
                    salvarUsuario();
                    Intent intent = new Intent(form_perfil_gerente_edit.this, tela_gerente.class);
                    startActivity(intent);
                    finish();
                }

                String nome = nomeText.getText().toString().trim();
                String date = dataText.getText().toString().trim();
                String end = enderecoText.getText().toString().trim();
                String cpf = cpfMask.getUnMasked().trim();
                String tel = telMask.getUnMasked().trim();
                char[] myChars = date.toCharArray();
                int resultadofinal = 0;
                validacoes valid = new validacoes();
                if (nome.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(v, "Campo nome vazio!", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    nomeText.requestFocus();
                } else if (date.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(v, "Campo Data vazio!", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    dataText.requestFocus();
                } else if (date.length() != 10) {
                    Snackbar snackbar = Snackbar.make(v, "Complete o campo data!", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    dataText.requestFocus();
                }else if(end.isEmpty()){
                    Snackbar snackbar = Snackbar.make(v, "Complete o campo endereco!", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    enderecoText.requestFocus();
                }else if(!cpfMask.isDone()){
                    Snackbar snackbar = Snackbar.make(v, "Complete o campo CPF", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    cpfMask.requestFocus();
                }else if(!valid.isCPF(cpf)){
                    Snackbar snackbar = Snackbar.make(v, "CPF INVALIDO!", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    cpfMask.requestFocus();
                }else if(tel.isEmpty()){
                    Snackbar snackbar = Snackbar.make(v, "Campo telefone vazio!", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    telMask.requestFocus();
                }else if(!telMask.isDone()){
                    Snackbar snackbar = Snackbar.make(v, "Preencha todo o campo telefone!", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    telMask.requestFocus();
                }
                else if (date.length() == 10) {
                    int x1 = myChars[6] - '0';
                    x1 = x1 * 1000;
                    int x2 = myChars[7] - '0';
                    x2 = x2 * 100;
                    int x3 = myChars[8] - '0';
                    x3 = x3 * 10;
                    int x4 = myChars[9] - '0';
                    x4 = x4 * 1;
                    int rsd = x1 + x2 + x3 + x4;
                    resultadofinal = rsd;

                    if (resultadofinal > Calendar.getInstance().get(Calendar.YEAR)) {
                        Snackbar snackbar = Snackbar.make(v, "Ano Superior ", Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                    } else if ((calcularIdade(date)) < 18) {
                        Snackbar snackbar = Snackbar.make(v, "Menor de idade", Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                    } else {
                        dadosGerente.put("nome",nome);
                        dadosGerente.put("cpf",cpfMask.getMasked());
                        dadosGerente.put("dataNascimentoi",date);
                        dadosGerente.put("tel",telMask.getMasked());
                        dadosGerente.put("endereco",end);

                        db.collection("Gerente").document(usuarioID).update(dadosGerente).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Intent intent = new Intent(form_perfil_gerente_edit.this, tela_gerente.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                    }
            }
        }});

        dataText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(form_perfil_gerente_edit.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month++;
                        if (day >= 10) {
                            if (month >= 10) {
                                String date = day + "/" + month + "/" + year;
                                dataText.setText(date);
                            } else {
                                String date = day + "/0" + month + "/" + year;
                                dataText.setText(date);
                            }
                        } else {
                            if (month >= 10) {
                                String date = "0" + day + "/" + month + "/" + year;
                                dataText.setText(date);
                            } else {
                                String date = "0" + day + "/0" + month + "/" + year;
                                dataText.setText(date);
                            }
                        }
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        db.collection("Gerente").document(usuarioID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        nomeText.setText(doc.getString("nome"));
                        cpfMask.setText(doc.getString("cpf"));
                        telMask.setText(doc.getString("telefone"));
                        dataText.setText(doc.getString("nascimeto"));
                        enderecoText.setText(doc.getString("endereco"));
                    }
                }
            }
        });
    }

    private  void selectphoto(){
        Intent  intent = new Intent (Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 ) {
            imguri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imguri);
                imguser.setImageDrawable(new BitmapDrawable(bitmap));
            } catch (IOException e) {

            }
        }
    }

    private  void salvarUsuario() {
        String Filemane = UUID.randomUUID().toString();
        final StorageReference ref = FirebaseStorage.getInstance().getReference("/imagens/"+Filemane);
        ref.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.i("teste", uri.toString());
                                String profilUrl = uri.toString();
                                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                db.collection("Gerente").document(userID).update("profileURL", profilUrl);
                            }

                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("teste", e.getMessage(), e);
                    }
                });



    }



    public int calcularIdade(String date1){

        // Pegando a data de hoje
        GregorianCalendar calendario = new GregorianCalendar();
        int dia = calendario.get(calendario.DAY_OF_MONTH);
        int mes = calendario.get(calendario.MONTH)+1;
        int ano = calendario.get(calendario.YEAR);

        //formatando a data recebida
        int diaU = Integer.valueOf(date1.substring(0,2));
        int mesU = Integer.valueOf(date1.substring(3,5));
        int anoU = Integer.valueOf(date1.substring(6,10));

        int idade;

        if(mesU < mes || (mesU==mes && diaU <=dia)){
            idade = ano-anoU;
        }
        else{
            idade = (ano- anoU)-1;
        }
        return idade;
    }
    void IniciarComponets() {

        imguser = findViewById(R.id.perfilgerente);
        editimg = findViewById(R.id.editarfoto);
        voltar = findViewById(R.id.voltar);
        salvar = findViewById(R.id.editperfil);
        nomeText = findViewById(R.id.dialog_nome);
        cpfMask = findViewById(R.id.dialog_cpf);
        telMask = findViewById(R.id.edit_fone);
        dataText= findViewById(R.id.edit_data_nasc);
        enderecoText= findViewById(R.id.edit_end);






    }
}