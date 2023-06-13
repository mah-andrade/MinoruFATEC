package com.example.minoru.forms.telagerentel_layout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.minoru.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class form_financas extends AppCompatActivity {
    private ImageView voltar;
    private static final int CREATEPDF = 1;
    private Button edit, pdf;

    private TextView nredimentos;
    private TextView nclientes;
    private TextView imposto;
    private TextView nmensalistas;
    private TextView vsalarios;
    private TextView nlucro;
    private TextView nclientesM;
    private TextView nclientesC;
    private TextView aluguel;
    private TextView aguaLuz;
    private TextView Mlimpeza;
    private TextView Terceiros;
    private TextView Manutencao;
    private TextView ngastos;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference valores;
    private int contMensalAtivos = 0;
    private Map<String, Object> dadosCustos = new HashMap<>();
    private Map<String, Object> dadosCustosMensal = new HashMap<>();

    private Date dataAtual = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_form_financas);
        iniciarcompo();


        // Declaração do Spinner

        final List<String> meses = Arrays.asList("", "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro");
        final Spinner spinner = findViewById(R.id.spinner);

        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_itemlayout, meses);
        adapter.setDropDownViewResource(R.layout.spinner_itemlayout);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                db.collection("Mensalistas").whereEqualTo("contrato", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            contMensalAtivos = 0;
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String inicio[] = documentSnapshot.getString("InicioContrato").split("/");
                                if (Integer.parseInt(inicio[1]) == i) {
                                    contMensalAtivos++;
                                }
                            }
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    nmensalistas.setText(String.valueOf(contMensalAtivos));
                                }
                            }, 20);
                        }
                    }
                });

                db.collection("custosOperacionais").document(i + year()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException error) {
                        if (doc.exists() && doc != null) {

                            vsalarios.setText("R$ " + formata_valores_br(doc.getDouble("salarios")));
                            aluguel.setText("R$ " + formata_valores_br(doc.getDouble("aluguel")));
                            aguaLuz.setText("R$ " + formata_valores_br(doc.getDouble("agualuz")));
                            Mlimpeza.setText("R$ " + formata_valores_br(doc.getDouble("material")));
                            Terceiros.setText("R$ " + formata_valores_br(doc.getDouble("servicosTerceiros")));
                            Manutencao.setText("R$ " + formata_valores_br(doc.getDouble("manutencao")));

                            //setando valores custooperacional
                            String aux[] = nredimentos.getText().toString().split(" ");

                            String auxSalarios[] = vsalarios.getText().toString().split(" ");
                            String auxAluguel[] = aluguel.getText().toString().split(" ");
                            String auxLuz[] = aguaLuz.getText().toString().split(" ");
                            String auxlimpeza[] = Mlimpeza.getText().toString().split(" ");
                            String auxTerceiros[] = Terceiros.getText().toString().split(" ");
                            String auxManutencao[] = Manutencao.getText().toString().split(" ");


                            Double CustomOperation = calculaValor(auxAluguel[1].replace(".", "").replace(",", "."),
                                    auxLuz[1].replace(".", "").replace(",", "."),
                                    auxlimpeza[1].replace(".", "").replace(",", "."),
                                    auxTerceiros[1].replace(".", "").replace(",", "."),
                                    auxManutencao[1].replace(".", "").replace(",", "."),
                                    auxSalarios[1].replace(".", "").replace(",", "."));
                            Log.i("TAPORRA", String.valueOf(CustomOperation));

                            ngastos.setText("R$ " + formata_valores_br(CustomOperation));


                            calcularValorFinal(CustomOperation, Double.parseDouble(aux[1].replace(".", "").replace(",", ".")));

                        } else {
                            dadosCustosMensal.put("aluguel", 0);
                            dadosCustosMensal.put("agualuz", 0);
                            dadosCustosMensal.put("material", 0);
                            dadosCustosMensal.put("servicosTerceiros", 0);
                            dadosCustosMensal.put("manutencao", 0);
                            dadosCustosMensal.put("salarios", 0);
                            db.collection("custosOperacionais").document(i + year()).set(dadosCustosMensal);
                        }
                    }
                });

                valores = db.collection("Mensal").document(i + year());
                valores.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            System.out.println("erro");
                        }
                        if (value != null && value.exists()) {
                            nredimentos.setText("R$ " + formata_valores_br(value.getDouble("valorRendimento")));
                            nclientes.setText(String.valueOf(value.getDouble("totalClientes").intValue()));
                            nclientesM.setText(String.valueOf(value.getDouble("motoCli").intValue()));
                            nclientesC.setText(String.valueOf(value.getDouble("carCli").intValue()));


                            double rend = value.getDouble("valorRendimento");
                            String aux[] = ngastos.getText().toString().split(" ");
                            calcularValorFinal(Double.parseDouble(aux[1].replace(".", "").replace(",", ".")), rend);
                        } else {
                            nredimentos.setText("R$ 0");
                            nlucro.setText("R$ 0");
                            nclientes.setText("0");
                            nclientesM.setText("0");
                            nclientesC.setText("0");
                            //nmensalistas.setText("0");
                        }
                    }
                });


                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Dialog dialog = new Dialog(form_financas.this, R.style.CustomAlertDialog);
                        dialog.setContentView(R.layout.dialog_edit_financas);
                        dialog.show();


                        Button fechar = dialog.findViewById(R.id.bt_inf_cancelar);
                        Button salvar = dialog.findViewById(R.id.bt_salvar);
                        EditText aluguelET = dialog.findViewById(R.id.valoraluguel);
                        EditText energiaET = dialog.findViewById(R.id.valorenergia);
                        EditText limpezaET = dialog.findViewById(R.id.valorlimpeza);
                        EditText terceirosET = dialog.findViewById(R.id.valorterceiros);
                        EditText manutencaoET = dialog.findViewById(R.id.valormanutencao);

                        db.collection("custosOperacionais").document(i + year()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot doc = task.getResult();
                                    if (doc.exists()) {
                                        aluguelET.setText(formata_valores_br(doc.getDouble("aluguel")));
                                        energiaET.setText(formata_valores_br(doc.getDouble("agualuz")));
                                        limpezaET.setText(formata_valores_br(doc.getDouble("material")));
                                        terceirosET.setText(formata_valores_br(doc.getDouble("servicosTerceiros")));
                                        manutencaoET.setText(formata_valores_br(doc.getDouble("manutencao")));

                                    }
                                }
                            }
                        });


                        salvar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String aluguelDialog = aluguelET.getText().toString().trim();
                                String energiaDialog = energiaET.getText().toString().trim();
                                String limpezaDialog = limpezaET.getText().toString().trim();
                                String terceirosDialog = terceirosET.getText().toString().trim();
                                String manutencaoDialog = manutencaoET.getText().toString().trim();

                                if (aluguelDialog.isEmpty() || energiaDialog.isEmpty() || limpezaDialog.isEmpty() || terceirosDialog.isEmpty() || manutencaoDialog.isEmpty()) {
                                    Snackbar snackbar = Snackbar.make(view, "Algum ou Todos CAMPOS em branco!!", Snackbar.LENGTH_SHORT);
                                    snackbar.setBackgroundTint(Color.WHITE);
                                    snackbar.setTextColor(Color.BLACK);
                                    snackbar.show();
                                } else {

                                    double aluguel = Double.parseDouble(aluguelDialog.replace(".", "").replace(",", "."));
                                    double energia = Double.parseDouble(energiaDialog.replace(".", "").replace(",", "."));
                                    double limpeza = Double.parseDouble(limpezaDialog.replace(".", "").replace(",", "."));
                                    double terceiros = Double.parseDouble(terceirosDialog.replace(".", "").replace(",", "."));
                                    double manutencao = Double.parseDouble(manutencaoDialog.replace(".", "").replace(",", "."));

                                    dadosCustos.put("agualuz", energia);
                                    dadosCustos.put("aluguel", aluguel);
                                    dadosCustos.put("manutencao", manutencao);
                                    dadosCustos.put("material", limpeza);
                                    dadosCustos.put("servicosTerceiros", terceiros);
                                    db.collection("custosOperacionais").document(i + year()).update(dadosCustos).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isComplete()) {
                                                dialog.dismiss();
                                            }
                                        }
                                    });
                                }
                            }
                        });


                        fechar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(form_financas.this, tela_gerente.class);
                startActivity(intent);
                finish();
            }
        });


        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarPdf("Relatorio");
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(form_financas.this, R.style.CustomAlertDialog);
                dialog.setContentView(R.layout.dialog_edit_financas);
                dialog.show();


                Button fechar = dialog.findViewById(R.id.bt_inf_cancelar);
                Button salvar = dialog.findViewById(R.id.bt_salvar);
                EditText aluguelET = dialog.findViewById(R.id.valoraluguel);
                EditText energiaET = dialog.findViewById(R.id.valorenergia);
                EditText limpezaET = dialog.findViewById(R.id.valorlimpeza);
                EditText terceirosET = dialog.findViewById(R.id.valorterceiros);
                EditText manutencaoET = dialog.findViewById(R.id.valormanutencao);

                db.collection("custosOperacionais").document("Informacoes").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            if (doc.exists()) {
                                aluguelET.setText(formata_valores_br(doc.getDouble("aluguel")));
                                energiaET.setText(formata_valores_br(doc.getDouble("agualuz")));
                                limpezaET.setText(formata_valores_br(doc.getDouble("material")));
                                terceirosET.setText(formata_valores_br(doc.getDouble("servicosTerceiros")));
                                manutencaoET.setText(formata_valores_br(doc.getDouble("manutencao")));
                            }
                        }
                    }
                });


                salvar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String aluguelDialog = aluguelET.getText().toString().trim();
                        String energiaDialog = energiaET.getText().toString().trim();
                        String limpezaDialog = limpezaET.getText().toString().trim();
                        String terceirosDialog = terceirosET.getText().toString().trim();
                        String manutencaoDialog = manutencaoET.getText().toString().trim();

                        if (aluguelDialog.isEmpty() || energiaDialog.isEmpty() || limpezaDialog.isEmpty() || terceirosDialog.isEmpty() || manutencaoDialog.isEmpty()) {
                            Snackbar snackbar = Snackbar.make(view, "Algum ou Todos CAMPOS em branco!!", Snackbar.LENGTH_SHORT);
                            snackbar.setBackgroundTint(Color.WHITE);
                            snackbar.setTextColor(Color.BLACK);
                            snackbar.show();
                        } else {

                            double aluguel = Double.parseDouble(aluguelET.getText().toString().replace(",", "."));
                            double energia = Double.parseDouble(energiaET.getText().toString().replace(",", "."));
                            double limpeza = Double.parseDouble(limpezaET.getText().toString().replace(",", "."));
                            double terceiros = Double.parseDouble(terceirosET.getText().toString().replace(",", "."));
                            double manutencao = Double.parseDouble(manutencaoET.getText().toString().replace(",", "."));

                            dadosCustos.put("agualuz", energia);
                            dadosCustos.put("aluguel", aluguel);
                            dadosCustos.put("manutencao", manutencao);
                            dadosCustos.put("material", limpeza);
                            dadosCustos.put("servicosTerceiros", terceiros);
                            db.collection("custosOperacionais").document("Informacoes").update(dadosCustos);
                            dialog.dismiss();
                        }
                    }
                });


                fechar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });


    }


    public void criarPdf(String title) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, title);
        startActivityForResult(intent, CREATEPDF);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATEPDF) {
            if (data.getData() != null) {
                //receber o conteudo das strings
                String Nclientes = nclientes.getText().toString();
                String Motos = nclientesM.getText().toString();
                String Carros = nclientesC.getText().toString();
                String Mensalistas = nmensalistas.getText().toString();
                String Aluguel = aluguel.getText().toString();
                String agualuz = aguaLuz.getText().toString();
                String limpeza = Mlimpeza.getText().toString();
                String terceiro = Terceiros.getText().toString();
                String manutencaos = Manutencao.getText().toString();
                String salario = vsalarios.getText().toString();
                String resultado = nlucro.getText().toString();
                String rendimentos = nredimentos.getText().toString();
                String gastos = ngastos.getText().toString();
                String ISS = imposto.getText().toString();


                Uri caminhoDoArquivo = data.getData();
                PdfDocument pdfDocument = new PdfDocument();
                Paint paint = new Paint();
                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1240, 1754, 1).create();
                PdfDocument.Page page = pdfDocument.startPage(pageInfo);
                Canvas canvas = page.getCanvas();

                // Titulo PDF
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(40f);
                paint.setFakeBoldText(true);
                paint.setColor(Color.parseColor("#8B0000"));
                canvas.drawText("Relatorio Mensal Minoru Estacionamento", pageInfo.getPageWidth() / 2, 100, paint);

                // Sub titulos
                paint.setTextSize(38f);
                paint.setColor(Color.parseColor("#FF000000"));
                canvas.drawText("Informações  Clientes", pageInfo.getPageWidth() / 2, 200, paint);
                canvas.drawText("Custos Operacionais Mensal", pageInfo.getPageWidth() / 2, 550, paint);
                canvas.drawText("Resultado Final", pageInfo.getPageWidth() / 2, 1000, paint);

                // Linhas
                paint.setColor(Color.parseColor("#FF000000"));
                canvas.drawLine(48, 230, pageInfo.getPageWidth() - 100, 230, paint);
                canvas.drawLine(48, 580, pageInfo.getPageWidth() - 100, 580, paint);
                canvas.drawLine(48, 1030, pageInfo.getPageWidth() - 100, 1030, paint);

                //Textos
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(34f);
                paint.setColor(Color.parseColor("#FF000000"));
                paint.setFakeBoldText(false);

                //Informaçoes de clientes
                canvas.drawText("Numero de Clientes: ", 50, 300, paint);
                canvas.drawText(Nclientes, 900, 300, paint);
                canvas.drawText("Numero de Mensalistas: ", 50, 350, paint);
                canvas.drawText(Mensalistas, 900, 350, paint);
                canvas.drawText("Numero de Clientes (Moto):", 50, 400, paint);
                canvas.drawText(Motos, 900, 400, paint);
                canvas.drawText("Numero de Clientes (Carro):", 50, 450, paint);
                canvas.drawText(Carros, 900, 450, paint);

                // Custos  Operacionais
                canvas.drawText("Aluguel: ", 50, 650, paint);
                canvas.drawText(Aluguel, 900, 650, paint);
                canvas.drawText("Agua + Luz: ", 50, 700, paint);
                canvas.drawText(agualuz, 900, 700, paint);
                canvas.drawText("Material de Limpeza ", 50, 750, paint);
                canvas.drawText(limpeza, 900, 750, paint);
                canvas.drawText("Serviços de Terceiros:  ", 50, 800, paint);
                canvas.drawText(terceiro, 900, 800, paint);
                canvas.drawText("Sálarios dos Funcionarios: ", 50, 850, paint);
                canvas.drawText(salario, 900, 850, paint);
                canvas.drawText("Manutenção:  ", 50, 900, paint);
                canvas.drawText(manutencaos, 900, 900, paint);

                //Resultado final
                canvas.drawText("Rendimentos: ", 50, 1100, paint);
                canvas.drawText(rendimentos, 900, 1100, paint);
                canvas.drawText("Custos Operacionais", 50, 1150, paint);
                canvas.drawText(gastos, 900, 1150, paint);
                canvas.drawText("Imposto (ISS):  ", 50, 1300, paint);
                canvas.drawText(ISS, 900, 1300, paint);
                canvas.drawText("Saldo Final:  ", 50, 1450, paint);
                canvas.drawText(resultado, 900, 1450, paint);


                paint.setTextSize(24f);
                canvas.drawText("Obs: Este relatório não possui validade fiscal   ", 50, 1700, paint);

                pdfDocument.finishPage(page);
                gravarPdf(caminhoDoArquivo, pdfDocument);

            }
        }
    }

    private void gravarPdf(Uri caminhoDoArquivo, PdfDocument pdfDocument) {
        try {
            BufferedOutputStream stream = new BufferedOutputStream(getContentResolver().openOutputStream(caminhoDoArquivo));
            pdfDocument.writeTo(stream);
            pdfDocument.close();
            stream.flush();
            Toast.makeText(this, "PDF Salvo com Sucesso", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Erro de arquivo", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Erro de arquivo", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Erro de arquivo", Toast.LENGTH_LONG).show();
        }


    }

    void iniciarcompo() {
        nredimentos = findViewById(R.id.nredimentos);
        voltar = findViewById(R.id.voltar);
        nclientes = findViewById(R.id.nclientes);
        nmensalistas = findViewById(R.id.nmensalistas);
        nclientesM = findViewById(R.id.clientesM);
        nclientesC = findViewById(R.id.clientesC);
        aluguel = findViewById(R.id.valuguel);
        aguaLuz = findViewById(R.id.venergia);
        Mlimpeza = findViewById(R.id.vlimpeza);
        Terceiros = findViewById(R.id.vterceiros);
        Manutencao = findViewById(R.id.nmanutencao);
        ngastos = findViewById(R.id.ngastos);
        vsalarios = findViewById(R.id.vsalarios);
        nlucro = findViewById(R.id.nlucro);
        edit = findViewById(R.id.bt_edit);
        pdf = findViewById(R.id.bt_pdf);
        imposto = findViewById(R.id.nimposto);


    }

    Double calculaValor(String aluguel, String energia, String limpeza, String terceiros, String manutencao, String salarios) {

        return Double.parseDouble(aluguel) +
                Double.parseDouble(energia) +
                Double.parseDouble(limpeza) +
                Double.parseDouble(terceiros) +
                Double.parseDouble(manutencao) +
                Double.parseDouble(salarios);
    }


    String year() {
        Calendar calendar = Calendar.getInstance();

        int a = calendar.get(Calendar.YEAR);

        return Integer.toString(a);
    }


    String formata_valores_br(double a) {

        DecimalFormatSymbols dfs = new DecimalFormatSymbols(new Locale("pt", "Brazil"));
        dfs.setDecimalSeparator(',');
        dfs.setGroupingSeparator('.');

        String padrao = "###,###.##";
        DecimalFormat df = new DecimalFormat(padrao, dfs);
        return df.format(a);
    }


    void calcularValorFinal(double custoOpera, double Rendimento) {
        double lucro = Rendimento - custoOpera;
        double impostos = (Rendimento* 0.05);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imposto.setText("R$ " + formata_valores_br(impostos));
                nlucro.setText("R$ " + formata_valores_br(lucro - impostos));
            }
        }, 1000);

    }


    String formatNumber(Double amount) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "Brazi"));

        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        return numberFormat.format(amount);

    }

    public String dateCapter() {

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(calendar.DAY_OF_MONTH);
        int month = calendar.get(calendar.MONTH) + 1;
        int year = calendar.get(calendar.YEAR);

        if (day >= 10) {
            if (month >= 10) {
                String date = day + "." + month + "." + year;
                return date;
            } else {
                String date = day + ".0" + month + "." + year;
                return date;
            }
        } else {
            if (month >= 10) {
                String date = "0" + day + "." + month + "." + year;
                return date;
            } else {
                String date = "0" + day + ".0" + month + "." + year;
                return date;
            }
        }


    }
}