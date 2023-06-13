package com.example.minoru.forms.telagerentel_layout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.minoru.R;
import com.example.minoru.adapter.adapterFunc;
import com.example.minoru.adapter.adapterToken;
import com.example.minoru.adapter.adapterVeiculo;
import com.example.minoru.forms.telafunc_tablayout.tela_func;
import com.example.minoru.models.funcbd;
import com.example.minoru.models.validacoes;
import com.example.minoru.models.veiculos;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.santalu.maskara.widget.MaskEditText;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class form_funcionarios extends AppCompatActivity{
    private ImageView voltar,ajuda;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private String userEmail,id;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button addfunc;
    private final validacoes valid = new validacoes();
    private adapterFunc adapter;
    private double salario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_form_funcionarios);
        IniciarComponentes();
        iniciar_recycler();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userEmail = user.getEmail();




        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(form_funcionarios.this, tela_gerente.class);
                startActivity(intent);
                finish();
            }
        });
        addfunc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openmenu();
            }
        });
    }

    private void iniciar_recycler(){

        CollectionReference func = db.collection("Funcionarios");


        Query query = func.orderBy("nome",Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<funcbd> options = new FirestoreRecyclerOptions.Builder<funcbd>()
                .setQuery(query,funcbd.class)
                .build();


        adapter = new adapterFunc(options);

        RecyclerView recyclerView = findViewById(R.id.recyFun);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new adapterFunc.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                documentSnapshot.toObject(funcbd.class);
                id = documentSnapshot.getId();
                opendialoginfo();
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                                    adapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);




    }
    public void opendialoginfo(){
        Dialog dialog = new Dialog(this,R.style.CustomAlertDialog);
        dialog.setContentView(R.layout.dialog_infofunc);
        dialog.show();
        frontDialogFuncMenu(dialog);
    }

    public void frontDialogFuncMenu(Dialog dialog){
        TextView tvNome =dialog.findViewById(R.id.txtnome);
        TextView tvCpf = dialog.findViewById(R.id.txtcpf);
        TextView tvTel = dialog.findViewById(R.id.txtcliente);
        TextView tvSalario = dialog.findViewById(R.id.txtinfsalario);
        TextView tvEmail = dialog.findViewById(R.id.txtdatavencimento);
        Button fechar = dialog.findViewById(R.id.bt_inf_cancelar);

        fechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        db.collection("Funcionarios").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                tvNome.setText(documentSnapshot.getString("nome"));
                tvCpf.setText(documentSnapshot.getString("cpf"));
                tvTel.setText(documentSnapshot.getString("tel"));
                tvSalario.setText(documentSnapshot.getString("salario"));
                tvEmail.setText(documentSnapshot.getString("email"));
            }
        });
    }

    public void openmenu(){
        Dialog dialog = new Dialog(this,R.style.CustomAlertDialog);
        dialog.setContentView(R.layout.dialog_addfunc);
        dialog.show();
        frontDialogFunc(dialog);
    }
    private void frontDialogFunc(Dialog dialog) {
        Button add = dialog.findViewById(R.id.bt_add);
        Button cancel = dialog.findViewById(R.id.bt_add_cancel);
        EditText editNome = dialog.findViewById(R.id.dialog_nome);
        MaskEditText editCpf = dialog.findViewById(R.id.dialog_cpf);
        MaskEditText editTelefone = dialog.findViewById(R.id.dialog_telefone);
        EditText editSalario = dialog.findViewById(R.id.dialog_salario);
        EditText CargoTipo = dialog.findViewById(R.id.dialog_emailfunc);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = editNome.getText().toString().trim();
                String cpf = editCpf.getUnMasked().trim();
                String tel = editTelefone.getUnMasked().trim();
                String sal = editSalario.getText().toString().trim();
                String cargo = CargoTipo.getText().toString().trim();
                // email senha nome cpf tel sal
                if(nome.isEmpty()){
                    Snackbar snackbar = Snackbar.make(view,"Campo nome vazio!", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    editNome.requestFocus();
                }else if(!isValidName(nome)){
                    Snackbar snackbar = Snackbar.make(view,"Digite um nome valido", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    editNome.requestFocus();
                }else if(!editCpf.isDone()){
                    Snackbar snackbar = Snackbar.make(view,"Preencha todo o campo do cpf", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    editCpf.requestFocus();
                }else if(!valid.isCPF(cpf)){
                    Snackbar snackbar = Snackbar.make(view,"CPF INVALIDO", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    editCpf.requestFocus();
                }else if(tel.isEmpty()){
                    Snackbar snackbar = Snackbar.make(view,"Campo telefone vazio", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    editTelefone.requestFocus();
                }else if(!editTelefone.isDone()){
                    Snackbar snackbar = Snackbar.make(view,"Preencha todo o campo do telefone", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    editTelefone.requestFocus();
                }else if(sal.isEmpty()){
                    Snackbar snackbar = Snackbar.make(view,"Preencha o campo do Salario do Funcionario", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    editSalario.requestFocus();
                }else if(cargo.isEmpty()){
                    Snackbar snackbar = Snackbar.make(view,"Preencha o campo do Salario do Funcionario", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    CargoTipo.requestFocus();
                }else{
                    createUser(cargo,nome,editCpf.getMasked().trim(),editTelefone.getMasked().trim(),sal,dialog,view);

                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
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
        adapter.stopListening();
    }

    public void createUser( String cargo,String nome , String cpf , String tel, String sal, Dialog dialog,View view){

                    DocumentReference Func = db.collection("Funcionarios").document();
                    Map<Object, String> dadosFunc = new HashMap<>();

                    dadosFunc.put("nome",nome);
                    dadosFunc.put("cpf",cpf);
                    dadosFunc.put("tel",tel);
                    dadosFunc.put("salario",sal);
                    dadosFunc.put("email",cargo);

                    Func.set(dadosFunc);

                    DocumentReference valoresMonth =  db.collection("custosOperacionais").document(month());

                    valoresMonth.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();
                                if(document.exists()){
                                    double auxR = document.getDouble("salarios");
                                    auxR = auxR + Double.parseDouble(sal);
                                    valoresMonth.update("salarios",auxR);
                                    dialog.dismiss();
                                }else{}
                            }else{
                                System.out.println("falha ao recuperar por que : "+ task.getException());
                            }
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
    void IniciarComponentes(){
        voltar = findViewById(R.id.voltar);
        addfunc =findViewById(R.id.button_add);
    }
    public static boolean isValidName(String name){
        String regex = "^(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+(?:\\-(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+)*(?: (?:(?:e|y|de(?:(?: la| las| lo| los))?|do|dos|da|das|del|van|von|bin|le) )?(?:(?:(?:d'|D'|O'|Mc|Mac|al\\-))?(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+|(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+(?:\\-(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+)*))+(?: (?:Jr\\.|II|III|IV))?$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

}