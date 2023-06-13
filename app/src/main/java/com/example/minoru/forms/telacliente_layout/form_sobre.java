package com.example.minoru.forms.telacliente_layout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.minoru.R;

import java.util.ArrayList;
import java.util.List;

public class form_sobre extends AppCompatActivity {
    private ImageView voltar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_sobre);
        getSupportActionBar().hide();
        voltar = findViewById(R.id.voltar);

        // Carrosel de imagens

        ImageSlider imageSlider = findViewById(R.id.slider);


        List<SlideModel> slideModels=new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.minoru1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.minoru2, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.minoru3, ScaleTypes.FIT));

        imageSlider.setImageList(slideModels, ScaleTypes.FIT);



        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(form_sobre.this, form_cliente.class);
                startActivity(intent);
                finish();
            }
        });

    }


}