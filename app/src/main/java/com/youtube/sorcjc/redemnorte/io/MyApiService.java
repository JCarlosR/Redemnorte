package com.youtube.sorcjc.redemnorte.io;

import com.youtube.sorcjc.redemnorte.io.response.BienResponse;
import com.youtube.sorcjc.redemnorte.io.response.BienesResponse;
import com.youtube.sorcjc.redemnorte.io.response.ByOldCodeResponse;
import com.youtube.sorcjc.redemnorte.io.response.ByPatrimonialResponse;
import com.youtube.sorcjc.redemnorte.io.response.HojaResponse;
import com.youtube.sorcjc.redemnorte.io.response.ResponsableResponse;
import com.youtube.sorcjc.redemnorte.io.response.SimpleResponse;
import com.youtube.sorcjc.redemnorte.model.Sheet;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MyApiService {

    @GET("responsables")
    Call<ResponsableResponse> getResponsables();

    @FormUrlEncoded
    @POST("registrar-foto")
    Call<SimpleResponse> postPhoto(
            @Field("image") String base64, @Field("extension") String extension,
            @Field("hoja_id") String hoja_id, @Field("QR_code") String QR_code);

    @GET("sheets")
    Call<ArrayList<Sheet>> getSheets(@Query("dni") String dni);

    @GET("/sheets/1")
    Call<HojaResponse> getSheet(@Query("sheet_id") String sheet_id);

    @GET("items")
    Call<BienesResponse> getItems(@Query("sheet_id") String sheet_id);

    @GET("bien")
    Call<BienResponse> getItem(@Query("hoja_id") String hoja_id, @Query("QR_code") String QR_code);

    @GET("login")
    Call<SimpleResponse> getLogin(@Query("username") String username, @Query("password") String password);

    @FormUrlEncoded
    @POST("sheets")
    Call<SimpleResponse> storeSheet(
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
    @POST("editar-hoja")
    Call<SimpleResponse> updateSheet(
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

    @GET("check-qr")
    Call<SimpleResponse> getCheckQr(@Query("qr_code") String QR_code);

    @GET("take-by-patrimonial")
    Call<ByPatrimonialResponse> getByPatrimonial(@Query("patrimonial") String patrimonial);

    @GET("take-by-old-code")
    Call<ByOldCodeResponse> getByOldCode(@Query("year") String year, @Query("code") String code);

    @FormUrlEncoded
    @POST("items")
    Call<SimpleResponse> storeItem(
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
    @POST("items/update")
    Call<SimpleResponse> updateItem(
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
