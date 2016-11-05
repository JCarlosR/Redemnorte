package com.youtube.sorcjc.redemnorte;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Spinner;

public class Global {

    public static void saveInSharedPreferences(Activity activity, String key, String value) {
        SharedPreferences sharedPref = activity.getSharedPreferences("global_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getFromSharedPreferences(Activity activity, String key) {
        SharedPreferences sharedPref = activity.getSharedPreferences("global_preferences", Context.MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }

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

}
