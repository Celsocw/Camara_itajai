package com.example.camara_itajai;
import com.example.camara_itajai.models.AgendamentoResponse;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.camara_itajai.api.ApiService;
import com.example.camara_itajai.api.RetrofitClient;
import com.example.camara_itajai.models.Agendamento;
import com.example.camara_itajai.models.CancelarAgendamentoResponce;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsultarAgendamentoActivity extends AppCompatActivity {
    private Spinner spinnerAgendamentos;
    private EditText editTextEmail;
    private TextView textViewResultado;
    private Button buttonConsultar, buttonCancelar, buttonVoltar;
    private ListView listViewAgendamentos;
    private ApiService apiService;
    private List<Agendamento> agendamentos;

    private int selectedAgendamentoId = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_agendamento);

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        editTextEmail = findViewById(R.id.editTextEmail);
        textViewResultado = findViewById(R.id.textViewResultado);
        buttonConsultar = findViewById(R.id.buttonConsultar);
        buttonCancelar = findViewById(R.id.buttonCancelar);
        buttonVoltar = findViewById(R.id.buttonVoltar);
        spinnerAgendamentos = findViewById(R.id.spinnerAgendamentos);

        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    private void setupListeners() {
        buttonConsultar.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            if (email.isEmpty() || !isValidEmail(email)) {
                Toast.makeText(ConsultarAgendamentoActivity.this, "Por favor, digite um e-mail válido.", Toast.LENGTH_SHORT).show();
                return;
            }
            consultarAgendamentos(email);
        });

        buttonCancelar.setOnClickListener(v -> {
            if (selectedAgendamentoId != -1) {
                mostrarDialogoConfirmacao(selectedAgendamentoId);
            } else {
                Toast.makeText(ConsultarAgendamentoActivity.this, "Selecione um agendamento para cancelar.", Toast.LENGTH_SHORT).show();
            }
        });

        buttonVoltar.setOnClickListener(v -> finish());

        spinnerAgendamentos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Agendamento agendamento = (Agendamento) parent.getItemAtPosition(position);
                selectedAgendamentoId = agendamento.getId();
                Log.d("CancelarAgendamento", "Agendamento selecionado: " + selectedAgendamentoId);
                Toast.makeText(ConsultarAgendamentoActivity.this, "Agendamento selecionado: " + agendamento.getServicoNome(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedAgendamentoId = -1;
            }
        });

        editTextEmail.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                String email = editTextEmail.getText().toString().trim();
                if (email.isEmpty() || !isValidEmail(email)) {
                    Toast.makeText(ConsultarAgendamentoActivity.this, "Por favor, digite um e-mail válido.", Toast.LENGTH_SHORT).show();
                    return true;
                }
                consultarAgendamentos(email);
                return true;
            }
            return false;
        });
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches(); // Valida o formato do e-mail
    }

    private void mostrarDialogoConfirmacao(int agendamentoId) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar Cancelamento")
                .setMessage("Tem certeza que deseja cancelar este agendamento?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    cancelarAgendamento(agendamentoId);
                })
                .setNegativeButton("Não", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void consultarAgendamentos(String email) {
        Call<AgendamentoResponse> call = apiService.consultarAgendamentos(email);
        call.enqueue(new Callback<AgendamentoResponse>() {
            @Override
            public void onResponse(Call<AgendamentoResponse> call, Response<AgendamentoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AgendamentoResponse agendamentoResponse = response.body();
                    agendamentos = agendamentoResponse.getAgendamentos();

                    // Verifica se a lista de agendamentos está vazia
                    if (agendamentos == null || agendamentos.isEmpty()) {
                        Toast.makeText(ConsultarAgendamentoActivity.this, "Nenhum agendamento encontrado.", Toast.LENGTH_SHORT).show();
                        return; // Sai do método se não houver agendamentos
                    }

                    ArrayAdapter<Agendamento> adapter = new ArrayAdapter<>(ConsultarAgendamentoActivity.this, android.R.layout.simple_spinner_item, agendamentos);
                    spinnerAgendamentos.setAdapter(adapter);
                } else {
                    Toast.makeText(ConsultarAgendamentoActivity.this, "Erro ao consultar agendamentos.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AgendamentoResponse> call, Throwable t) {
                Toast.makeText(ConsultarAgendamentoActivity.this, "Erro de conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void cancelarAgendamento(int agendamentoId) {
        Call<CancelarAgendamentoResponce> call = apiService.cancelarAgendamento(agendamentoId);
        Log.d("CancelarAgendamento", "ID do agendamento a ser cancelado: " + agendamentoId);

        call.enqueue(new Callback<CancelarAgendamentoResponce>() {
            @Override
            public void onResponse(Call<CancelarAgendamentoResponce> call, Response<CancelarAgendamentoResponce> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CancelarAgendamentoResponce cancelarResponse = response.body();
                    if (cancelarResponse.isSuccess()) {
                        Toast.makeText(ConsultarAgendamentoActivity.this, "Agendamento cancelado com sucesso.", Toast.LENGTH_SHORT).show();
                        consultarAgendamentos(editTextEmail.getText().toString());
                    } else {
                        Toast.makeText(ConsultarAgendamentoActivity.this, "Erro: " + cancelarResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ConsultarAgendamentoActivity.this, "Erro ao cancelar agendamento.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CancelarAgendamentoResponce> call, Throwable t) {
                Toast.makeText(ConsultarAgendamentoActivity.this, "Erro de conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}