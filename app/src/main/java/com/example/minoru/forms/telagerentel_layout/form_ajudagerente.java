package com.example.minoru.forms.telagerentel_layout;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.example.minoru.R;
import com.example.minoru.forms.telacliente_layout.Form_ajuda;
import com.example.minoru.forms.telacliente_layout.expandablelistview;
import com.example.minoru.forms.telacliente_layout.form_cliente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class form_ajudagerente extends AppCompatActivity {
    private ImageView voltar;
    private List<String> listGroup;
    private HashMap<String, List<String>> listData;


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_ajudagerente);
        iniciarcomponents();
        getSupportActionBar().hide();


        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(form_ajudagerente.this, tela_gerente.class);
                startActivity(intent);
                finish();
            }
        });



        buildList();

        ExpandableListView expandableListView2 = (ExpandableListView) findViewById(R.id.expandableListView2);
        expandableListView2.setAdapter(new expandablelistview(form_ajudagerente.this, listGroup, listData));

        expandableListView2.setOnChildClickListener(new ExpandableListView.OnChildClickListener(){
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //  Toast.makeText(Form_ajuda.this, "Group: "+groupPosition+"| Item: "+childPosition, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        expandableListView2.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener(){
            @Override
            public void onGroupExpand(int groupPosition) {
                // Toast.makeText(Form_ajuda.this, "Group (Expand): "+groupPosition, Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView2.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener(){
            @Override
            public void onGroupCollapse(int groupPosition) {
                //   Toast.makeText(Form_ajuda.this, "Group (Collapse): "+groupPosition, Toast.LENGTH_SHORT).show();
            }
        });

        //    expandableListView.setGroupIndicator(getResources().getDrawable(R.drawable.ic_seta_baixo));
    }

    public void buildList(){
        listGroup = new ArrayList<String>();
        listData = new HashMap<String, List<String>>();

        // Pergunta
        listGroup.add("Como consulatar as finanças do estacionamento?");
        listGroup.add("Como gerar realtório em PDF?");
        listGroup.add("Posso alterar meus dados cadastrais?");
        listGroup.add("Como consultar os status atual do estacionamento?");
        listGroup.add("Como adicionar um novo funcionario? ");
        listGroup.add("O aplicativo protege meus dados ");
        listGroup.add("Como consultar as minhas informaçoes pessoais?");
        listGroup.add("Como editar minhas informações pessoais?");
        listGroup.add("Como alterar os preços do estacionamento?");


        // Resposta
        List<String> auxList = new ArrayList<String>();
        auxList.add("Para consultar as finanças, basta clicar no botão \"Finanças\" na tela inicial do aplicativo. Você será direcionado para a tela de finanças, onde poderá selecionar o mês desejado e obter as informações desejadas." );

        listData.put(listGroup.get(0), auxList);

        auxList.add("Para gerar um relatório, aperte no botão finançãs na tela incial do app, voce será direcionado para tela de finanças, selecione o mes desejado e depois clique no botão Salvar " );

        listData.put(listGroup.get(1), auxList);

        auxList = new ArrayList<String>();
        auxList.add("Sim, para alterar os dados cadastrais aperta no botão  Meu perfil na tela inicial, depois em Editar perfil e voce poderá alterar seus dados ");
        listData.put(listGroup.get(2), auxList);

        auxList = new ArrayList<String>();
        auxList.add("Para consultar o status, clique no botão \"Minoru\" na tela inicial do aplicativo. Você será direcionado para uma tela onde poderá verificar o número de vagas disponíveis no estacionamento e também visualizar a lista de clientes que estiveram no estabelecimento durante o dia em questão.");

        listData.put(listGroup.get(3), auxList);

        auxList = new ArrayList<String>();
        auxList.add("melhore essa resposta Para adicionar  um funcionário, aperte o botão \"Funcionários\" na tela inicial do app, voce derá direcionado para tela funcionários onde poderá consultar informações sobre o funcionario e poderá adicionar um novo funcionario apertando no botão +");

        listData.put(listGroup.get(4), auxList);

        auxList = new ArrayList<String>();
        auxList.add("Sim, todos os dados do usuário são criptografados e armazenados de forma segura no banco de dados Firebase da Google. ");

        listData.put(listGroup.get(5), auxList);

        auxList = new ArrayList<String>();
        auxList.add("Para consultar seus dados cadastrais, basta clicar no botão \"Meu Perfil\" na tela inicial do aplicativo.");

        listData.put(listGroup.get(6), auxList);

        auxList = new ArrayList<String>();
        auxList.add("Para editar seus dados cadastrais, basta clicar no botão \"Meu Perfil\" na tela inicial do aplicativo depois clicar no botão \"Editar Perfil\" e voce poderá alterar seus dados.");

        listData.put(listGroup.get(7), auxList);

        auxList = new ArrayList<String>();
        auxList.add("Para alterar os preços, basta clicar no botão \"Tabela de Preços\" na tela inicial, e voce poderá alterá os preços dos serviços oferecidos pelo estacionamento");

        listData.put(listGroup.get(8), auxList);




    }

    void iniciarcomponents(){
        voltar = findViewById(R.id.voltar);
    }


}

