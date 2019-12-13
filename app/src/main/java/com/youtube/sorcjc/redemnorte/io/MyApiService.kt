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
    @Headers("Accept: application/json")
    fun getSheets(@Query("user_id") userId: Int): Call<ArrayList<Sheet>>

    @GET("sheets/{sheetId}")
    @Headers("Accept: application/json")
    fun getSheet(@Path("sheetId") sheetId: Int): Call<Sheet>

    @GET("sheets/{sheetId}/items")
    @Headers("Accept: application/json")
    fun getItems(@Path("sheetId") sheetId: Int): Call<ArrayList<Item>>

    @GET("items/{itemId}")
    @Headers("Accept: application/json")
    fun getItem(@Path("itemId") itemId: Int): Call<Item>

    @FormUrlEncoded
    @POST("items/{itemId}/photos")
    @Headers("Accept: application/json")
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
    @Headers("Accept: application/json")
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
    @Headers("Accept: application/json")
    fun getCheckQr(@Query("qr_code") qrCode: String?): Call<SimpleResponse>

    @GET("expected-data/search")
    @Headers("Accept: application/json")
    fun getByPatrimonial(@Query("code") code: String): Call<Item>

    @GET("old-data/search")
    @Headers("Accept: application/json")
    fun getByOldCode(@Query("year") year: String, @Query("code") code: String): Call<Item>

    @FormUrlEncoded
    @POST("items")
    @Headers("Accept: application/json")
    fun storeItem(
            @Field("sheet_id") sheetId: Int,
            @Field("inventory_code") qrCode: String?,
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
            @Field("status") status: Int,
            @Field("labeled") labeled: Boolean,
            @Field("operative") operative: Boolean,
            @Field("observation") observation: String?
    ): Call<Item>

    @FormUrlEncoded
    @PUT("items/{itemId}")
    @Headers("Accept: application/json")
    fun updateItem(
            @Field("itemId") itemId: Int,
            @Field("inventory_code") qrCode: String?,
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
            @Field("status") status: Int,
            @Field("labeled") labeled: Boolean,
            @Field("operative") operative: Boolean,
            @Field("observation") observation: String?
    ): Call<Item>
}