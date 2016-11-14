package com.youtube.sorcjc.redemnorte.io;

import com.youtube.sorcjc.redemnorte.io.response.BienResponse;
import com.youtube.sorcjc.redemnorte.io.response.BienesResponse;
import com.youtube.sorcjc.redemnorte.io.response.ByOldCodeResponse;
import com.youtube.sorcjc.redemnorte.io.response.ByPatrimonialResponse;
import com.youtube.sorcjc.redemnorte.io.response.HojaResponse;
import com.youtube.sorcjc.redemnorte.io.response.HojasResponse;
import com.youtube.sorcjc.redemnorte.io.response.ResponsableResponse;
import com.youtube.sorcjc.redemnorte.io.response.SimpleResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RedemnorteApiService {

    @GET("responsables.php")
    Call<ResponsableResponse> getResponsables();

    @FormUrlEncoded
    @POST("registrar-foto.php")
    Call<SimpleResponse> postPhoto(
            @Field("image") String base64, @Field("extension") String extension,
            @Field("hoja_id") String hoja_id, @Field("QR_code") String QR_code);

    @FormUrlEncoded
    @POST("hojas.php")
    Call<HojasResponse> getHojas(@Field("dni") String dni);

    @GET("hoja.php")
    Call<HojaResponse> getHoja(@Query("hoja_id") String hoja_id);

    @FormUrlEncoded
    @POST("bienes.php")
    Call<BienesResponse> getBienes(@Field("hoja_id") String hoja_id);

    @GET("bien.php")
    Call<BienResponse> getBien(@Query("hoja_id") String hoja_id, @Query("QR_code") String QR_code);

    @GET("login.php")
    Call<SimpleResponse> getLogin(@Query("username") String username, @Query("password") String password);

    @FormUrlEncoded
    @POST("registrar-hoja.php")
    Call<SimpleResponse> postRegistrarHoja(
            @Field("id") String id,
            @Field("local") String local,
            @Field("ubicacion") String ubicacion,
            @Field("responsable") String responsable,
            @Field("cargo") String cargo,
            @Field("oficina") String oficina,
            @Field("ambiente") String ambiente,
            @Field("area") String area,
            @Field("activo") String activo,
            @Field("observacion") String observacion,
            @Field("inventariador") String inventariador
    );

    @FormUrlEncoded
    @POST("editar-hoja.php")
    Call<SimpleResponse> postEditarHoja(
            @Field("id") String id,
            @Field("local") String local,
            @Field("ubicacion") String ubicacion,
            @Field("responsable") String responsable,
            @Field("cargo") String cargo,
            @Field("oficina") String oficina,
            @Field("ambiente") String ambiente,
            @Field("area") String area,
            @Field("activo") String activo,
            @Field("observacion") String observacion
    );

    @GET("check-qr.php")
    Call<SimpleResponse> getCheckQr(@Query("qr_code") String QR_code);

    @GET("take-by-patrimonial.php")
    Call<ByPatrimonialResponse> getByPatrimonial(@Query("patrimonial") String patrimonial);

    @GET("take-by-old-code.php")
    Call<ByOldCodeResponse> getByOldCode(@Query("year") String year, @Query("code") String code);

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
            @Field("etiquetado") String etiquetado,
            @Field("operativo") String operativo,
            @Field("observacion") String observacion
    );

    @FormUrlEncoded
    @POST("editar-bien.php")
    Call<SimpleResponse> postEditarBien(
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
            @Field("etiquetado") String etiquetado,
            @Field("operativo") String operativo,
            @Field("observacion") String observacion
    );
}
