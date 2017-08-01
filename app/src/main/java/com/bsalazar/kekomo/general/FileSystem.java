package com.bsalazar.kekomo.general;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by bsalazar on 26/06/2017.
 */

public class FileSystem {

    private String PATH;
    public String IMAGES_PATH;

    private static FileSystem instance;

    public static FileSystem getInstance(Context context) {
        if (instance == null)
            instance = new FileSystem(context);
        return instance;
    }

    private FileSystem(Context context) {
//        PATH = context.getFilesDir().getAbsolutePath();
        PATH = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        IMAGES_PATH = PATH + "/images/";

        crearEstructuraCarpetas();
    }


    public void crearEstructuraCarpetas() {
        try {
            File f = new File(IMAGES_PATH);
            if (!f.exists())
                f.mkdir();

        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("crearEstructuraCarpetas", ex.toString());
        }
    }

    public boolean isSDCARDMounted() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }


    public Uri getUriNuevaImagen(String nombrefFichero) {
        File f = getFicheroFotoTemporalOriginal(nombrefFichero);
        if ( f != null)
            return Uri.fromFile(f);
        else
            return null;
    }

    private File getFicheroFotoTemporalOriginal(String nombreFichero) {
        File f = null;

        if (isSDCARDMounted()) {
            //f = new File(RUTA_CARPETA_APP_TEMPFOTOS, FOTO_TEMPORAL_ORIGINAL);
            f = new File(IMAGES_PATH, nombreFichero);

            try {
//                if (!f.exists()) {
//                    f.createNewFile();
//                }
                if (f.exists())
                    f.delete();

                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return f;
    }


}
