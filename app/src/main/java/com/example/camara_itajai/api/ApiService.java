package com.example.camara_itajai.api;

import com.example.camara_itajai.models.AgendamentoExistenteResponse;
import com.example.camara_itajai.models.AgendamentoResponse;
import com.example.camara_itajai.models.CancelarAgendamentoResponce;
import com.example.camara_itajai.models.HorariosResponse;
import com.example.camara_itajai.models.Servico;
import com.example.camara_itajai.models.UserRegistrationRequest;
import com.example.camara_itajai.models.UserRegistrationResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @GET("read_sectors.php")
    Call<List<String>> getSectors();

    @GET("get_horarios_disponiveis.php")
    Call<HorariosResponse> getHorariosDisponiveis(@Query("servico_id") int servicoId, @Query("data") long data);

    @POST("create_user.php")
    Call<UserRegistrationResponse> registerUser(@Body UserRegistrationRequest request);

    @GET("consultar_agendamentos.php")
    Call<AgendamentoResponse> consultarAgendamentos(@Query("email") String email);

    @FormUrlEncoded
    @POST("cancelar_agendamento.php")
    Call<CancelarAgendamentoResponce> cancelarAgendamento(@Field("agendamento_id") int agendamentoId);

    @FormUrlEncoded
    @POST("realizar_agendamento.php")
    Call<AgendamentoResponse> realizarAgendamento(
            @Field("servico_id") int servicoId,
            @Field("data") long data,
            @Field("horario_id") int horarioId,
            @Field("usuario_id") int usuarioId);

    @GET("get_servicos.php")
    Call<List<Servico>> getServicos();

    @GET("verificar_agendamento_existente.php")
    Call<AgendamentoExistenteResponse> verificarAgendamentoExistente(@Query("email") String email, @Query("setor_id") int setorId);

    @GET("get_available_times.php")
    Call<List<String>> getAvailableTimes(
            @Query("date") String selectedDate,
            @Query("sector") String sector,
            @Query("service") String service
    );

    // Remova a classe RetrofitClient interna daqui
}