package com.example.camara_itajai;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.camara_itajai.api.ApiService;
import com.example.camara_itajai.api.RetrofitClient;
import com.example.camara_itajai.models.HorarioDisponivel;
import com.example.camara_itajai.models.HorariosResponse;
import com.example.camara_itajai.models.Servico;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgendamentoActivity extends AppCompatActivity {

    private Spinner spinnerServico;
    private CalendarView calendarView;
    private Spinner spinnerHorarios;
    private Button btnConfirm;
    private Button btnVoltar;
    private long selectedDate;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agendamento);

        initializeViews();
        setupCalendarView();
        loadServicos();
        setupServiceSpinner();
        setupConfirmButton();
        setupVoltarButton();
    }

    private void initializeViews() {
        spinnerServico = findViewById(R.id.spinnerServico);
        calendarView = findViewById(R.id.calendarView);
        spinnerHorarios = findViewById(R.id.spinnerHorarios);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnVoltar = findViewById(R.id.btnVoltar);
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    private void setupCalendarView() {
        Locale locale = new Locale("pt", "BR");
        Locale.setDefault(locale);
        calendarView.setMinDate(System.currentTimeMillis() - 1000);
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);

            if (isWeekend(calendar)) {
                Toast.makeText(this, "Finais de semana não são permitidos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isHoliday(calendar)) {
                Toast.makeText(this, "Feriados não são permitidos", Toast.LENGTH_SHORT).show();
                return;
            }

            selectedDate = calendar.getTimeInMillis();
            updateHorarios();
        });
    }

    private void setupServiceSpinner() {
        spinnerServico.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Servico selectedServico = (Servico) spinnerServico.getSelectedItem();
                if (selectedServico != null && selectedServico.getId() == -1) {
                    showIdentityServiceDialog();
                } else {
                    updateHorarios(); // Atualiza horários quando um serviço válido é selecionado
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Não é necessário fazer nada aqui
            }
        });
    }

    private void setupConfirmButton() {
        btnConfirm.setOnClickListener(v -> confirmAppointment());
    }

    private void setupVoltarButton() {
        btnVoltar.setOnClickListener(v -> finish());
    }

    private void loadServicos() {
        Call<List<Servico>> call = apiService.getServicos();
        call.enqueue(new Callback<List<Servico>>() {
            @Override
            public void onResponse(Call<List<Servico>> call, Response<List<Servico>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Servico> servicos = new ArrayList<>(response.body());

                    ArrayAdapter<Servico> adapter = new ArrayAdapter<>(AgendamentoActivity.this,
                            android.R.layout.simple_spinner_item, servicos);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerServico.setAdapter(adapter);
                } else {
                    handleApiError(response, "Erro ao carregar serviços");
                }
            }

            @Override
            public void onFailure(Call<List<Servico>> call, Throwable t) {
                Toast.makeText(AgendamentoActivity.this, "Erro de conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateHorarios() {
        Servico selectedServico = (Servico) spinnerServico.getSelectedItem();
        if (selectedServico == null || selectedDate == 0) return;

        Call<HorariosResponse> call = apiService.getHorariosDisponiveis(selectedServico.getId(), selectedDate);
        call.enqueue(new Callback<HorariosResponse>() {
            @Override
            public void onResponse(Call<HorariosResponse> call, Response<HorariosResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<HorarioDisponivel> horarios = response.body().getHorarios();
                    if (horarios != null && !horarios.isEmpty()) {
                        ArrayAdapter<HorarioDisponivel> adapter = new ArrayAdapter<>(AgendamentoActivity.this,
                                android.R.layout.simple_spinner_item, horarios);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerHorarios.setAdapter(adapter);
                    } else {
                        Toast.makeText(AgendamentoActivity.this, "Nenhum horário disponível", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    handleApiError(response, "Erro ao carregar horários");
                }
            }

            @Override
            public void onFailure(Call<HorariosResponse> call, Throwable t) {
                Log.e("API Error", "Falha na chamada da API", t);
                Toast.makeText(AgendamentoActivity.this, "Erro de conexão: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void confirmAppointment() {
        Servico selectedServico = (Servico) spinnerServico.getSelectedItem();
        HorarioDisponivel selectedHorario = (HorarioDisponivel) spinnerHorarios.getSelectedItem();
        if (selectedServico == null || selectedDate == 0 || selectedHorario == null) {
            Toast.makeText(this, "Por favor, selecione serviço, data e horário", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedHorario.getVagasDisponiveis() > 0) {
            Intent intent = new Intent(AgendamentoActivity.this, UserRegistrationActivity.class);
            intent.putExtra("servico_id", selectedServico.getId());
            intent.putExtra("servico_nome", selectedServico.getNome());
            intent.putExtra("data", selectedDate);
            intent.putExtra("data_formatada", new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date(selectedDate)));
            intent.putExtra("horario_id", selectedHorario.getId());
            intent.putExtra("horario", selectedHorario.getHora());
            intent.putExtra("setor_id", selectedServico.getSetorId()); // Adicione o setor_id
            startActivity(intent);
        } else {
            Toast.makeText(this, "Não há mais vagas disponíveis para este horário", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isWeekend(Calendar calendar) {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
    }

    private boolean isHoliday(Calendar calendar) {
        List<Calendar> holidays = new ArrayList<>();
        Calendar holiday1 = Calendar.getInstance();
        holiday1.set(2024, Calendar.JANUARY, 1); // Ano Novo
        holidays.add(holiday1);

        // Adicione outros feriados à lista conforme necessário

        for (Calendar holiday : holidays) {
            if (calendar.get(Calendar.DAY_OF_YEAR) == holiday.get(Calendar.DAY_OF_YEAR) &&
                    calendar.get(Calendar.YEAR) == holiday.get(Calendar.YEAR)) {
                return true;
            }
        }
        return false;
    }

    private void showIdentityServiceDialog() {
        new AlertDialog.Builder(AgendamentoActivity.this)
                .setMessage("Você deseja continuar com o serviço de identidade?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://agendamento.igp.sc.gov.br/protocoloavisopreemissaorg.aspx"));
                    startActivity(browserIntent);
                })
                .setNegativeButton("Não", null)
                .show();
    }

    /**
     * Método para tratar erros HTTP específicos
     */
    private void handleApiError(Response<?> response, String defaultMessage) {
        try {
            // Assumindo que todas as respostas de erro seguem a estrutura { "success": false, "message": "Erro específico" }
            Gson gson = new Gson();
            String errorBody = response.errorBody().string();
            // Dependendo do endpoint, você pode mapear para modelos de erro específicos
            AgendamentoResponse errorResponse = gson.fromJson(errorBody, AgendamentoResponse.class);
            if (errorResponse != null && errorResponse.getMessage() != null) {
                Toast.makeText(AgendamentoActivity.this, errorResponse.getMessage(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(AgendamentoActivity.this, defaultMessage, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(AgendamentoActivity.this, "Erro desconhecido.", Toast.LENGTH_LONG).show();
        }
    }
}