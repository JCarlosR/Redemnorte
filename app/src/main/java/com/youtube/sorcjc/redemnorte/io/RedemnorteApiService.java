package com.youtube.sorcjc.redemnorte.io;

import com.youtube.sorcjc.redemnorte.io.response.HojasResponse;
import com.youtube.sorcjc.redemnorte.io.response.SimpleResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RedemnorteApiService {

    @FormUrlEncoded
    @POST("hojas.php")
    Call<HojasResponse> getHojas(@Field("dni") String dni);

    @FormUrlEncoded
    @POST("login.php")
    Call<SimpleResponse> getLogin(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("registrar-hoja.php")
    Call<SimpleResponse> postRegistrarHoja(
            @Field("local") String local,
            @Field("ubicacion") String ubicacion,
            @Field("responsable") String responsable,
            @Field("cargo") String cargo,
            @Field("dependencia") String dependencia,
            @Field("ambiente") String ambiente,
            @Field("area") String area,
            @Field("inventariador") String inventariador
    );
}
