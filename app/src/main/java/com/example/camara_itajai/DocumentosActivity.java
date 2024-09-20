package com.example.camara_itajai;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

public class DocumentosActivity extends AppCompatActivity {

    public Button btnCertidoes, btnCurriculo, btnBalcaoEmpregos, btnRG, btnDocumentosPerdidos, btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documentos);

        // Habilitar o botão Up na Action Bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Documentos");
        }

        // Inicializando os botões
        btnCertidoes = findViewById(R.id.btnCertidoes);
        btnCurriculo = findViewById(R.id.btnCurriculo);
        btnBalcaoEmpregos = findViewById(R.id.btnBalcaoEmpregos);
        btnRG = findViewById(R.id.btnRG);
        btnDocumentosPerdidos = findViewById(R.id.btnDocumentosPerdidos);
        btnVoltar = findViewById(R.id.btnVoltar);

        // Configurando listeners com verificações null
        if (btnCertidoes != null) {
            btnCertidoes.setOnClickListener(view -> {
                Intent intent = new Intent(DocumentosActivity.this, CertidoesActivity.class);
                startActivity(intent);
            });
        }

        if (btnCurriculo != null) {
            btnCurriculo.setOnClickListener(view -> {
                Intent intent = new Intent(DocumentosActivity.this, CurriculoActivity.class);
                startActivity(intent);
            });
        }

        if (btnBalcaoEmpregos != null) {
            btnBalcaoEmpregos.setOnClickListener(view ->
                    openWebPage("https://intranet2.itajai.sc.gov.br/balcao-empregos/externo/vagas"));
        }

        if (btnRG != null) {
            btnRG.setOnClickListener(view ->
                    openWebPage("https://www.policiacientifica.sc.gov.br/carteira-de-identidade/passo-a-passo/"));
        }

        if (btnDocumentosPerdidos != null) {
            btnDocumentosPerdidos.setOnClickListener(view ->
                    openWebPage("https://www.cvi.sc.gov.br/documentos-perdidos/encontrados"));
        }

        if (btnVoltar != null) {
            btnVoltar.setOnClickListener(view -> finish());
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Fecha a atividade atual e retorna para a anterior
        return true;
    }

    /**
     * Abre uma página web no navegador padrão.
     *
     * @param url A URL para abrir.
     */
    private void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Nenhum navegador encontrado para abrir o link.", Toast.LENGTH_LONG).show();
        }
    }
}
