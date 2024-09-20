package com.example.camara_itajai;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnAgendar, btnConsultar, btnDocumentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAgendar = findViewById(R.id.btnAgendar);
        btnConsultar = findViewById(R.id.btnConsultar);
        btnDocumentos = findViewById(R.id.btnDocumentos);

        btnAgendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AgendamentoActivity.class);
                startActivity(intent);
            }
        });

        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ConsultarAgendamentoActivity.class);
                startActivity(intent);
            }
        });

        btnDocumentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DocumentosActivity.class);
                startActivity(intent);
            }
        });
    }
}
