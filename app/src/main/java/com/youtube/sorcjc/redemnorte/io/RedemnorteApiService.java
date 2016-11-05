package com.youtube.sorcjc.redemnorte.io;

import com.youtube.sorcjc.redemnorte.io.response.BienesResponse;
import com.youtube.sorcjc.redemnorte.io.response.HojasResponse;
import com.youtube.sorcjc.redemnorte.io.response.SimpleResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RedemnorteApiService {

    @FormUrlEncoded
    @POST("hojas.php")
    Call<HojasResponse> getHojas(@Field("dni") String dni);

    @FormUrlEncoded
    @POST("bienes.php")
    Call<BienesResponse> getBienes(@Field("hoja_id") String hoja_id);

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

    @FormUrlEncoded
    @POST("registrar-bien.php")
    Call<SimpleResponse> postRegistrarBien(
            @Field("hoja_id") String hoja_id,
            @Field("QR_code") String QR_code,
            @Field("patrimonial_code") String patrimonial_code,
            @Field("old_code") String old_code,
            @Field("old_year") String old_year,

            @Field("denominacion") String denominacion,
            @Field("marca") String marca,
            @Field("modelo") String modelo,
            @Field("serie") String serie,
            @Field("color") String color,

            @Field("largo") String largo,
            @Field("ancho") String ancho,
            @Field("alto") String alto,

            @Field("condicion") String condicion,
            @Field("operativo") String operativo,
            @Field("observacion") String observacion
    );
}
