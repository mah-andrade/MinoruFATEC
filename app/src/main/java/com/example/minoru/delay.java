package com.example.minoru;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.minoru.forms.tela_login;
import com.example.minoru.forms.telacliente_layout.form_cliente;
import com.example.minoru.forms.telafunc_tablayout.tela_func;
import com.example.minoru.forms.telagerentel_layout.tela_gerente;
import com.example.minoru.forms.telalavador_layout.form_lavador;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class delay extends AppCompatActivity {

    private final String gerente = "NaiD5lUTa8WVwXWQubfsfGUQYuf1";
    private final String lavador = "y8EwjJ1XM2a70BJxyiIpb6Wu2ri2";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delay);
        getSupportActionBar().hide();


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        FirebaseUser userAtual = FirebaseAuth.getInstance().getCurrentUser();



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (userAtual != null) {
                    String usuario = userAtual.getUid();
                    if (usuario.equals(gerente)) {
                        tela_principal_gerente();
                    } else if (usuario.equals(lavador)) {
                        tela_principal_func();
                    } else {
                        telamensalista_cliente();
                    }
                } else {
                    startActivity(new Intent(getBaseContext(), tela_login.class));
                    finish();
                }
            }
        }, 1000);
    }

    void tela_principal_func() {
        Intent intent = new Intent(this, form_lavador.class);
        startActivity(intent);
        finish();
    }

    void tela_principal_gerente() {
        Intent intent = new Intent(this, tela_gerente.class);
        startActivity(intent);
        finish();
    }

    void telamensalista_cliente() {
        Intent intent = new Intent(this, form_cliente.class);
        startActivity(intent);
        finish();
    }
}

