package com.example.camara_itajai;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CertidoesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certidoes);

        Button buttonVoltar = findViewById(R.id.buttonVoltar);
        buttonVoltar.setOnClickListener(v -> finish());
    }
}