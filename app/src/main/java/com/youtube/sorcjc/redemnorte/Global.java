package com.youtube.sorcjc.redemnorte;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.widget.Spinner;

import java.io.ByteArrayOutputStream;

public class Global {

    public static int getSpinnerIndex(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++)
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }

        return index;
    }

    public static String getBase64FromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // Get the base 64 string
        return Base64.encodeToString(byteFormat, Base64.NO_WRAP);
    }

    public static String getProductPhotoUrl(String hoja_id, String qr_code, String extension) {
        return "https://redemnorte.com/images/2016/"+hoja_id+"-"+qr_code+"."+extension;
    }

}
