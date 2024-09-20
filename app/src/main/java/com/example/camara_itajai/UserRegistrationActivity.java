package com.example.camara_itajai;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.camara_itajai.api.ApiService;
import com.example.camara_itajai.api.RetrofitClient;
import com.example.camara_itajai.models.AgendamentoExistenteResponse;
import com.example.camara_itajai.models.AgendamentoResponse;
import com.example.camara_itajai.models.UserRegistrationRequest;
import com.example.camara_itajai.models.UserRegistrationResponse;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRegistrationActivity extends AppCompatActivity {
    private EditText editTextName, editTextEmail, editTextPhone;
    private TextInputLayout tilName, tilEmail, tilPhone;
    private Button btnRegister, btnBack;
    private TextView tvAppointmentInfo;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        initializeViews();
        apiService = RetrofitClient.getClient().create(ApiService.class);

        displayAppointmentInfo();
        setupButtons();
    }

    private void initializeViews() {
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        tilName = findViewById(R.id.tilName);
        tilEmail = findViewById(R.id.tilEmail);
        tilPhone = findViewById(R.id.tilPhone);
        btnRegister = findViewById(R.id.btnRegister);
        btnBack = findViewById(R.id.btnBack);
        tvAppointmentInfo = findViewById(R.id.tvAppointmentInfo);
    }

    private void displayAppointmentInfo() {
        String servicoNome = getIntent().getStringExtra("servico_nome");
        String dataFormatada = getIntent().getStringExtra("data_formatada");
        String horario = getIntent().getStringExtra("horario");

        String infoText = String.format("Agendamento: <b>%s</b><br>Data: <b>%s</b><br>Horário: <b>%s</b><br><br>Por favor, preencha seus dados para confirmar:",
                servicoNome, dataFormatada, horario);
        tvAppointmentInfo.setText(Html.fromHtml(infoText, Html.FROM_HTML_MODE_COMPACT));
    }

    private void setupButtons() {
        btnRegister.setOnClickListener(v -> {
            if (validateInputs()) {
                verificarAgendamentoExistente();
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private boolean validateInputs() {
        boolean isValid = true;

        // Validar nome
        String name = editTextName.getText().toString().trim();
        if (name.isEmpty()) {
            tilName.setError("Nome é obrigatório");
            isValid = false;
        } else if (name.length() > 60) {
            tilName.setError("Nome não pode ter mais de 60 caracteres");
            isValid = false;
        } else {
            tilName.setError(null);
        }

        // Validar email
        String email = editTextEmail.getText().toString().trim();
        if (email.isEmpty()) {
            tilEmail.setError("Email é obrigatório");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Email inválido");
            isValid = false;
        } else {
            tilEmail.setError(null);
        }

        // Validar telefone
        String phone = editTextPhone.getText().toString().trim();
        if (phone.isEmpty()) {
            tilPhone.setError("Telefone é obrigatório");
            isValid = false;
        } else {
            tilPhone.setError(null);
        }

        return isValid;
    }

    private void verificarAgendamentoExistente() {
        String email = editTextEmail.getText().toString().trim();
        int setorId = getIntent().getIntExtra("setor_id", 0);

        Log.d("DEBUG", "Verificando agendamento para email: " + email + " e setor_id: " + setorId);

        Call<AgendamentoExistenteResponse> call = apiService.verificarAgendamentoExistente(email, setorId);
        call.enqueue(new Callback<AgendamentoExistenteResponse>() {
            @Override
            public void onResponse(Call<AgendamentoExistenteResponse> call, Response<AgendamentoExistenteResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AgendamentoExistenteResponse existenteResponse = response.body();
                    if (existenteResponse.isExists()) {
                        showAlertDialog("Agendamento Existente", existenteResponse.getMessage());
                    } else {
                        registerUserAndConfirmAppointment();
                    }
                } else {
                    // Tratando erros HTTP específicos
                    handleApiError(response);
                }
            }

            @Override
            public void onFailure(Call<AgendamentoExistenteResponse> call, Throwable t) {
                Toast.makeText(UserRegistrationActivity.this, "Erro de conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerUserAndConfirmAppointment() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();

        UserRegistrationRequest request = new UserRegistrationRequest(name, email, phone);
        Call<UserRegistrationResponse> call = apiService.registerUser(request);
        call.enqueue(new Callback<UserRegistrationResponse>() {
            @Override
            public void onResponse(Call<UserRegistrationResponse> call, Response<UserRegistrationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserRegistrationResponse userResponse = response.body();
                    if (userResponse.isSuccess()) {
                        int userId = userResponse.getUserId();
                        confirmAppointment(userId);
                    } else {
                        Toast.makeText(UserRegistrationActivity.this, "Erro: " + userResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Tratando erros HTTP específicos
                    handleApiError(response);
                }
            }

            @Override
            public void onFailure(Call<UserRegistrationResponse> call, Throwable t) {
                Toast.makeText(UserRegistrationActivity.this, "Erro de conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmAppointment(int userId) {
        int servicoId = getIntent().getIntExtra("servico_id", 0);
        long data = getIntent().getLongExtra("data", 0);
        int horarioId = getIntent().getIntExtra("horario_id", 0);

        Call<AgendamentoResponse> call = apiService.realizarAgendamento(servicoId, data, horarioId, userId);
        call.enqueue(new Callback<AgendamentoResponse>() {
            @Override
            public void onResponse(Call<AgendamentoResponse> call, Response<AgendamentoResponse> response) {
                if (response.isSuccessful()) {
                    AgendamentoResponse agendamentoResponse = response.body();
                    if (agendamentoResponse != null && agendamentoResponse.isSuccess()) {
                        // Agendamento bem-sucedido
                        Toast.makeText(UserRegistrationActivity.this, "Agendamento realizado com sucesso!", Toast.LENGTH_LONG).show();
                        finish(); // Fecha a atividade atual
                    } else {
                        // Falha no agendamento
                        Toast.makeText(UserRegistrationActivity.this, "Falha ao agendar: " + agendamentoResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Tratar erros HTTP específicos
                    handleApiError(response);
                }
            }

            @Override
            public void onFailure(Call<AgendamentoResponse> call, Throwable t) {
                Toast.makeText(UserRegistrationActivity.this, "Erro de conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Agendamento Realizado")
                .setMessage("Seu agendamento foi realizado com sucesso!")
                .setPositiveButton("OK", (dialog, which) -> {
                    Intent intent = new Intent(UserRegistrationActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .setCancelable(false)
                .show();
    }

    private void showAlertDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    /**
     * Método para tratar erros HTTP específicos
     */
    private void handleApiError(Response<?> response) {
        try {
            // Assumindo que todas as respostas de erro seguem a estrutura { "success": false, "message": "Erro específico" }
            Gson gson = new Gson();
            String errorBody = response.errorBody().string();
            // Determinar o tipo de resposta de erro com base no endpoint ou código de status
            // Aqui, vamos supor que todas seguem AgendamentoResponse
            AgendamentoResponse errorResponse = gson.fromJson(errorBody, AgendamentoResponse.class);
            if (errorResponse != null && errorResponse.getMessage() != null) {
                Toast.makeText(UserRegistrationActivity.this, errorResponse.getMessage(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(UserRegistrationActivity.this, "Erro ao processar a solicitação.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(UserRegistrationActivity.this, "Erro desconhecido.", Toast.LENGTH_LONG).show();
        }
    }
}