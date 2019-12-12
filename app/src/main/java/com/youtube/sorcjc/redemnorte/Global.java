package com.youtube.sorcjc.redemnorte;

public class Global {

    public static String getProductPhotoUrl(String hoja_id, String qr_code, String extension) {
        return "https://redemnorte.com/images/2016/"+hoja_id+"-"+qr_code+"."+extension;
    }

}
