package com.youtube.sorcjc.redemnorte.io

import com.youtube.sorcjc.redemnorte.io.response.*
import com.youtube.sorcjc.redemnorte.model.Item
import com.youtube.sorcjc.redemnorte.model.ResponsibleUser
import com.youtube.sorcjc.redemnorte.model.Sheet
import com.youtube.sorcjc.redemnorte.model.User
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface MyApiService {
    @POST("login")
    @Headers("Accept: application/json")
    fun postLogin(
            @Query("username") username: String?,
            @Query("password") password: String?
    ): Call<User>

    @GET("public-data")
    @Headers("Accept: application/json")
    fun getPublicData(): Call<PublicDataResponse>

    @GET("sheets")
    fun getSheets(@Query("user_id") userId: Int): Call<ArrayList<Sheet>>

    @GET("sheets/{sheetId}")
    fun getSheet(@Path("sheetId") sheetId: Int): Call<Sheet>

    @GET("sheets/{sheetId}/items")
    fun getItems(@Path("sheetId") sheetId: Int): Call<ArrayList<Item>>

    @GET("items/{itemId}")
    fun getItem(@Query("itemId") itemId: Int): Call<Item>

    @FormUrlEncoded
    @POST("items/{itemId}/photos")
    fun postPhoto(
            @Path("itemId") itemId: Int,
            @Field("image") base64: String?,
            @Field("extension") extension: String?
    ): Call<Item>

    @FormUrlEncoded
    @POST("sheets")
    @Headers("Accept: application/json")
    fun storeSheet(
            @Field("place") place: String?,
            @Field("location") location: String?,
            @Field("responsible_user") responsibleName: String?,
            @Field("position") position: String?,
            @Field("office") office: String?,
            @Field("ambient") ambient: String?,
            @Field("area") area: String?,
            @Field("pending") pending: Boolean,
            @Field("observation") observation: String?,
            @Field("author") userId: Int
    ): Call<Sheet>

    @FormUrlEncoded
    @PUT("sheets/{sheetId}")
    fun updateSheet(
            @Path("sheetId") sheetId: Int,
            @Field("place") place: String?,
            @Field("location") location: String?,
            @Field("responsible_user") responsibleName: String?,
            @Field("position") position: String?,
            @Field("office") office: String?,
            @Field("ambient") ambient: String?,
            @Field("area") area: String?,
            @Field("pending") pending: Boolean,
            @Field("observation") observation: String?
    ): Call<Sheet>

    @GET("check-qr")
    fun getCheckQr(@Query("qr_code") qrCode: String?): Call<SimpleResponse>

    @GET("take-by-patrimonial")
    fun getByPatrimonial(@Query("patrimonial") patrimonial: String?): Call<ByPatrimonialResponse?>

    @GET("take-by-old-code")
    fun getByOldCode(@Query("year") year: String?, @Query("code") code: String?): Call<ByOldCodeResponse?>

    @FormUrlEncoded
    @POST("items")
    fun storeItem(
            @Field("sheet_id") sheetId: Int,
            @Field("QR_code") QR_code: String?,
            @Field("patrimonial_code") patrimonial_code: String?,
            @Field("old_code") old_code: String?,
            @Field("old_year") old_year: String?,
            @Field("denomination") denomination: String?,
            @Field("brand") brand: String?,
            @Field("model") model: String?,
            @Field("series") series: String?,
            @Field("color") color: String?,
            @Field("length") length: String?,
            @Field("width") width: String?,
            @Field("height") height: String?,
            @Field("status") status: String?,
            @Field("labeled") labeled: Boolean,
            @Field("operative") operative: Boolean,
            @Field("observation") observation: String?
    ): Call<SimpleResponse>

    @FormUrlEncoded
    @PUT("items/{itemId}")
    fun updateItem(
            @Field("itemId") itemId: Int,
            @Field("QR_code") QR_code: String?,

            @Field("patrimonial_code") patrimonial_code: String?,
            @Field("old_code") old_code: String?,
            @Field("old_year") old_year: String?,
            @Field("denomination") denomination: String?,
            @Field("brand") brand: String?,
            @Field("model") model: String?,
            @Field("series") series: String?,
            @Field("color") color: String?,
            @Field("length") length: String?,
            @Field("width") width: String?,
            @Field("height") height: String?,
            @Field("status") status: String?,
            @Field("labeled") labeled: Boolean,
            @Field("operative") operative: Boolean,
            @Field("observation") observation: String?
    ): Call<SimpleResponse>
}