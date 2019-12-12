package com.youtube.sorcjc.redemnorte.io

import com.youtube.sorcjc.redemnorte.io.response.*
import com.youtube.sorcjc.redemnorte.model.ResponsibleUser
import com.youtube.sorcjc.redemnorte.model.Sheet
import com.youtube.sorcjc.redemnorte.model.User
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface MyApiService {
    @POST("login")
    fun postLogin(@Query("username") username: String?, @Query("password") password: String?): Call<User>

    @GET("responsible-users")
    fun getResponsibleUsers(): Call<ArrayList<ResponsibleUser>>

    @FormUrlEncoded
    @POST("registrar-foto")
    fun postPhoto(
            @Field("image") base64: String?, @Field("extension") extension: String?,
            @Field("hoja_id") hoja_id: String?, @Field("QR_code") QR_code: String?): Call<SimpleResponse?>

    @GET("sheets")
    fun getSheets(@Query("dni") dni: String?): Call<ArrayList<Sheet>>

    @GET("/sheets/1")
    fun getSheet(@Query("sheet_id") sheet_id: String?): Call<HojaResponse?>

    @GET("items")
    fun getItems(@Query("sheet_id") sheet_id: String?): Call<BienesResponse?>

    @GET("bien")
    fun getItem(@Query("hoja_id") hoja_id: String?, @Query("QR_code") QR_code: String?): Call<BienResponse>

    @FormUrlEncoded
    @POST("sheets")
    fun storeSheet(
            @Field("id") id: String?,
            @Field("place") place: String?,
            @Field("ubicacion") ubicacion: String?,
            @Field("responsable") responsable: String?,
            @Field("cargo") cargo: String?,
            @Field("office") office: String?,
            @Field("ambient") ambient: String?,
            @Field("area") area: String?,
            @Field("pending") pending: Boolean?,
            @Field("observation") observation: String?,
            @Field("author") userId: Int
    ): Call<SimpleResponse?>

    @FormUrlEncoded
    @POST("editar-hoja")
    fun updateSheet(
            @Field("id") id: String?,
            @Field("place") place: String?,
            @Field("ubicacion") ubicacion: String?,
            @Field("responsable") responsable: String?,
            @Field("cargo") cargo: String?,
            @Field("office") office: String?,
            @Field("ambient") ambient: String?,
            @Field("area") area: String?,
            @Field("pending") pending: Boolean,
            @Field("observation") observation: String?
    ): Call<SimpleResponse?>

    @GET("check-qr")
    fun getCheckQr(@Query("qr_code") QR_code: String?): Call<SimpleResponse>

    @GET("take-by-patrimonial")
    fun getByPatrimonial(@Query("patrimonial") patrimonial: String?): Call<ByPatrimonialResponse?>

    @GET("take-by-old-code")
    fun getByOldCode(@Query("year") year: String?, @Query("code") code: String?): Call<ByOldCodeResponse?>

    @FormUrlEncoded
    @POST("items")
    fun storeItem(
            @Field("hoja_id") hoja_id: String?,
            @Field("QR_code") QR_code: String?,
            @Field("patrimonial_code") patrimonial_code: String?,
            @Field("old_code") old_code: String?,
            @Field("old_year") old_year: String?,
            @Field("denominacion") denominacion: String?,
            @Field("marca") marca: String?,
            @Field("modelo") modelo: String?,
            @Field("serie") serie: String?,
            @Field("color") color: String?,
            @Field("largo") largo: String?,
            @Field("ancho") ancho: String?,
            @Field("alto") alto: String?,
            @Field("condicion") condicion: String?,
            @Field("etiquetado") etiquetado: String?,
            @Field("operativo") operativo: String?,
            @Field("observation") observation: String?
    ): Call<SimpleResponse>

    @FormUrlEncoded
    @POST("items/update")
    fun updateItem(
            @Field("hoja_id") hoja_id: String?,
            @Field("QR_code") QR_code: String?,
            @Field("patrimonial_code") patrimonial_code: String?,
            @Field("old_code") old_code: String?,
            @Field("old_year") old_year: String?,
            @Field("denominacion") denominacion: String?,
            @Field("marca") marca: String?,
            @Field("modelo") modelo: String?,
            @Field("serie") serie: String?,
            @Field("color") color: String?,
            @Field("largo") largo: String?,
            @Field("ancho") ancho: String?,
            @Field("alto") alto: String?,
            @Field("condicion") condicion: String?,
            @Field("etiquetado") etiquetado: String?,
            @Field("operativo") operativo: String?,
            @Field("observation") observation: String?
    ): Call<SimpleResponse>
}