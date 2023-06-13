package com.example.minoru.forms.telacliente_layout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.minoru.R;
import com.example.minoru.models.validacoes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.santalu.maskara.widget.MaskEditText;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class form_contrato extends AppCompatActivity {
    private ImageView voltar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView txtnome,txttel,txtdataentrada,txtdatavencimento,txtvec,txtmodelovec,txtplaca;
    private String id;
    private Button renova;
    private int auxVec;
    private String VencimentoFormatado;
    private LocalDate localDate;
    private double valorfinal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_contrato);
        getSupportActionBar().hide();
        ini();

        id = getIntent().getStringExtra("id");
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(form_contrato.this,form_cliente.class);
                startActivity(intent);
                finish();
            }
        });

        DocumentReference dadosContrato = db.collection("Mensalistas").document(id);
        dadosContrato.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot d) {

                txtnome.setText(d.getString("nome"));
                txttel.setText(d.getString("tel"));
                txtdataentrada.setText(d.getString("InicioContrato"));
                txtdatavencimento.setText(d.getString("VencimentoContrato"));
                VencimentoFormatado = d.getString("VencimentoContrato");
                txtmodelovec.setText(d.getString("modelo"));
                txtplaca.setText(d.getString("placa"));
                if(d.getString("veiculo").equals("MOTO")){
                    txtvec.setText("Moto");
                    auxVec = 2;
                }else{
                    txtvec.setText("Carro");
                    auxVec = 1;
                }
            }
        });

        renova.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(form_contrato.this,R.style.CustomAlertDialog);
                dialog.setContentView(R.layout.dialog_pagamentocliente);
                dialog.show();
                frontPag(dialog);
            }
        });
    }

    public void frontPag(Dialog dialog) {
        MaskEditText numCard = dialog.findViewById(R.id.Edit_ncartao);
        MaskEditText numValid = dialog.findViewById(R.id.Edit_validade);
        MaskEditText numCvc = dialog.findViewById(R.id.Edit_cvv);
        EditText numNome = dialog.findViewById(R.id.Edit_nomecartao);
        MaskEditText numCPF = dialog.findViewById(R.id.Edit_CPF);
        Button pagar = dialog.findViewById(R.id.bt_inf_renova);
        Button cancel = dialog.findViewById(R.id.bt_inf_cancelar);
        TextView valorP = dialog.findViewById(R.id.vlpagar);
        ProgressBar a = dialog.findViewById(R.id.progressBar3);

        a.setVisibility(View.GONE);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        System.out.println(auxVec);
        if(auxVec == 2){
            System.out.println("aqui");
            db.collection("ValorEstacionamento").document("Moto").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    valorfinal =documentSnapshot.getDouble("mensal");
                    valorP.setText(formata_valores_br(documentSnapshot.getDouble("mensal")));
                }
            });
        }else{
            db.collection("ValorEstacionamento").document("Carro").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    valorfinal =documentSnapshot.getDouble("mensal");
                    valorP.setText(formata_valores_br(documentSnapshot.getDouble("mensal")));
                }
            });
        }

        pagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //0122
                //String auxValid = numValid.getUnMasked();

                validacoes valid = new validacoes();
                if(!numCard.isDone()){
                    Snackbar snackbar = Snackbar.make(view, "Numero do Cartao incompleto", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    numCard.requestFocus();
                }else if(!numValid.isDone()){
                    Snackbar snackbar = Snackbar.make(view, "Preencha a validade do cartao", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    numValid.requestFocus();
                }else if(!validCVV(numValid.getUnMasked())){
                    Snackbar snackbar = Snackbar.make(view, "Cartao Vencido", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    numValid.requestFocus();
                }else if(!numCvc.isDone()){
                    Snackbar snackbar = Snackbar.make(view, "Preencha o CVV do cartao", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    numCvc.requestFocus();
                }else if(!numCPF.isDone()){
                    Snackbar snackbar = Snackbar.make(view, "Preencha corretamente o CPF", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    numCPF.requestFocus();
                }else if(!valid.isCPF(numCPF.getUnMasked())){
                    Snackbar snackbar = Snackbar.make(view, "CPF INVALIDO!", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    numCPF.requestFocus();
                }else if(numNome.getText().toString().isEmpty()){
                    Snackbar snackbar = Snackbar.make(view, "Campo vazio Nome", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    numNome.requestFocus();
                }else if(!isValidName(numNome.getText().toString())){
                    Snackbar snackbar = Snackbar.make(view, "Digite um Nome Valido!", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    numNome.requestFocus();
                }else{
                    Log.i("Chegou","chegou");
                    pagar.setVisibility(View.INVISIBLE);
                    cancel.setVisibility(View.INVISIBLE);
                    a.setVisibility(View.VISIBLE);
                    Log.i("Chegou","chegou");


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        localDate = LocalDate.parse(VencimentoFormatado,DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    }
                    Log.i("Chegou","chegou");
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        Log.i("Chegou","chegouDentro");
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        Log.i("Chegou","chegouDentroFormat");
                        localDate = localDate.plusDays(30);
                        Log.i("Chegou","chegouDentroFormatADD");
                        VencimentoFormatado = localDate.format(formatter);
                    }
                    Log.i("Chegou","chegou");
                    db.collection("Mensalistas").document(id).update("VencimentoContrato",VencimentoFormatado);

                    DocumentReference valoresMonth =  db.collection("Mensal").document(month());

                    valoresMonth.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();
                                if(document.exists()){
                                    double auxR = document.getDouble("valorRendimento");
                                    auxR = auxR + valorfinal;
                                    valoresMonth.update("valorRendimento",auxR);

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(dialog.getContext(),form_pagsucesso.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    },1800);


                                }else{}
                            }else{
                                System.out.println("falha ao recuperar por que : "+ task.getException());
                            }
                        }
                    });
                }
        }});
    }

    public void ini(){
        voltar = findViewById(R.id.voltar);
        txtnome = findViewById(R.id.txtnome);
        txttel = findViewById(R.id.txttel);
        txtdataentrada = findViewById(R.id.txtdataentrada);
        txtdatavencimento = findViewById(R.id.txtdatavencimento);
        txtvec = findViewById(R.id.txtvec);
        txtmodelovec = findViewById(R.id.txtmodelovec);
        txtplaca = findViewById(R.id.txtplaca);
        renova = findViewById(R.id.bt_inf_renova);
    }

    String month(){
        Calendar calendar = Calendar.getInstance();
        int b = calendar.get(Calendar.MONTH)+1;
        int a = calendar.get(Calendar.YEAR);
        String date = (Integer.toString(b))+(Integer.toString(a));
        return date ;
    }

    public static boolean isValidName(String name){

        String regex = "^(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+(?:\\-(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+)*(?: (?:(?:e|y|de(?:(?: la| las| lo| los))?|do|dos|da|das|del|van|von|bin|le) )?(?:(?:(?:d'|D'|O'|Mc|Mac|al\\-))?(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+|(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+(?:\\-(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+)*))+(?: (?:Jr\\.|II|III|IV))?$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    String formata_valores_br (double a){

        DecimalFormatSymbols dfs= new DecimalFormatSymbols(new Locale("pt","Brazil"));
        dfs.setDecimalSeparator(',');
        dfs.setGroupingSeparator('.');

        String padrao = "###,###.##";
        DecimalFormat df = new DecimalFormat(padrao,dfs);
        return df.format(a);
    }

    public boolean validCVV(String a){

        int aux = Integer.parseInt(a.substring(0, 2));
        int auxano = Integer.parseInt(a.substring(2, 4));

        if ((aux > 0) && (aux <= 12) && (auxano >= 23)){
            if((aux >=6) || (auxano > 23)){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

}