package com.example.minoru.forms.telacliente_layout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.minoru.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Form_ajuda extends AppCompatActivity {
    private ImageView voltar;
    private List<String> listGroup;
    private HashMap<String, List<String>> listData;


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_ajuda);
        iniciarcomponents();
        getSupportActionBar().hide();


        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Form_ajuda.this, form_cliente.class);
                startActivity(intent);
                finish();
            }
        });



        buildList();

        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListView.setAdapter(new expandablelistview(Form_ajuda.this, listGroup, listData));

        expandableListView.setOnChildClickListener(new OnChildClickListener(){
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
              //  Toast.makeText(Form_ajuda.this, "Group: "+groupPosition+"| Item: "+childPosition, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        expandableListView.setOnGroupExpandListener(new OnGroupExpandListener(){
            @Override
            public void onGroupExpand(int groupPosition) {
               // Toast.makeText(Form_ajuda.this, "Group (Expand): "+groupPosition, Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new OnGroupCollapseListener(){
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
        listGroup.add("Como posso renovar o meu contrato?");
        listGroup.add("Qual o horário de funcionamento do estacionamento?");
        listGroup.add("Quais são a formas de pagamento disponiveis?");
        listGroup.add("Posso estacionar outro veiculo na minha vaga? ");
        listGroup.add("O estacionamento possui algum sistema de segurança?");
        listGroup.add("O estacionamento oferece outros serviços?");
        listGroup.add("O estacionamento tem seguro?");
        listGroup.add("Como consultar as minhas informaçoes pessoais?");
        listGroup.add("Como editar minhas informações pessoais?");
        listGroup.add("Onde encontrar informações realacionadas ao estacionamento ?");


        // Resposta
        List<String> auxList = new ArrayList<String>();
        auxList.add("Para renovar seu contrato basta ir até a tela incial, clicar no botão Meu Contrato e depois ir em renovar contrato no canto inferior da tela");

        listData.put(listGroup.get(0), auxList);

        auxList = new ArrayList<String>();
        auxList.add("O estacionamento Minoru funciona de segunda à sábado das 05:30 até às 22:00 e aos domingos das 07:00 às 22:00 ");
        listData.put(listGroup.get(1), auxList);

        auxList = new ArrayList<String>();
        auxList.add("Através do aplicativo, você tem a opção de realizar o pagamento utilizando cartão de crédito. No entanto, se preferir, também é possível efetuar o pagamento diretamente no estacionamento, seja em dinheiro ou cartão de crédito.");

        listData.put(listGroup.get(2), auxList);

        auxList = new ArrayList<String>();
        auxList.add("Não, somente o veículo vinculado ao cliente no cadastro terá permissão para utilizar a vaga.");

        listData.put(listGroup.get(3), auxList);

        auxList = new ArrayList<String>();
        auxList.add("Sim, o estacionamento é equipado com um sistema de monitoramento por câmeras de vigilância que funciona em tempo integral e é administrado pela empresa Porto Seguro");

        listData.put(listGroup.get(4), auxList);

        auxList = new ArrayList<String>();
        auxList.add("No momento, o estacionamento oferece uma variedade de serviços, incluindo estacionamento avulso, diário e mensal. Além disso, também disponibiliza o serviço de lavagem de veículos.");

        listData.put(listGroup.get(5), auxList);

        auxList = new ArrayList<String>();
        auxList.add("O estacionamento oferce seguro para roubos, furtos qualificados e danos aos veículos causados por um dos funcionários.");

        listData.put(listGroup.get(6), auxList);

        auxList = new ArrayList<String>();
        auxList.add("Para consultar seus dados cadastrais, basta clicar no botão \"Meu Perfil\" na tela inicial do aplicativo.");

        listData.put(listGroup.get(7), auxList);

        auxList = new ArrayList<String>();
        auxList.add("Para editar seus dados cadastrais, basta clicar no botão \"Meu Perfil\" na tela inicial do aplicativo depois clicar no botão \"Editar Perfil\" e voce poderá alterar seus dados.");

        listData.put(listGroup.get(8), auxList);

        auxList = new ArrayList<String>();
        auxList.add("Para informações sobre o estacionamento, basta clicar no botão \"Sobre\" na tela inicial do aplicativo ");

        listData.put(listGroup.get(9), auxList);
    }

    void iniciarcomponents(){
        voltar = findViewById(R.id.voltar);
    }


}