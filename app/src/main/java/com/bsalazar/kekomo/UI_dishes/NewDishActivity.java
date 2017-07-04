package com.bsalazar.kekomo.UI_dishes;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bsalazar.kekomo.R;
import com.bsalazar.kekomo.bbdd.controllers.DishesController;
import com.bsalazar.kekomo.bbdd.entities.Dish;
import com.bsalazar.kekomo.general.Constants;
import com.bsalazar.kekomo.general.FileSystem;
import com.bsalazar.kekomo.general.GaleryActivity;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by bsalazar on 22/06/2017.
 */

public class NewDishActivity extends AppCompatActivity implements View.OnClickListener {

    private Activity activity;
    private ImageView dish_image;
    private EditText dish_name, dish_description;

    private final int GALERY_INPUT = 1;
    private final int CAMERA_INPUT = 2;
    private final int OWN_GALERY = 3;
    String mCurrentPhotoPath;

    private Bitmap new_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dish);
        activity = this;

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dish_image = (ImageView) findViewById(R.id.dish_image);
        dish_name = (EditText) findViewById(R.id.dish_name);
        dish_description = (EditText) findViewById(R.id.dish_description);

        dish_image.setOnClickListener(this);
        dish_image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), GaleryActivity.class), OWN_GALERY);
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_dish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_save:
                saveDish();
                finish();
                return true;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dish_image:
                showImageDialog();
                break;
        }
    }



    private void saveDish() {
        Dish dish = new Dish();
        dish.setName(dish_name.getText().toString());
        dish.setDescription(dish_description.getText().toString());
        dish.setTags("x");
        dish.setImage("x");

        Dish save_dish = new DishesController().add(dish, Constants.database);

        String image_name = "pic_" + save_dish.getId() + ".jpg";
        saveImage(image_name);
        save_dish.setImage(image_name);
        new DishesController().update(save_dish, Constants.database);
    }


    private void showImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.ask_image))
                .setPositiveButton(getString(R.string.camera), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePictureIntent, CAMERA_INPUT);

                    }
                })
                .setNegativeButton(getString(R.string.galery), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        openGalery();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void openGalery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALERY_INPUT);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALERY_INPUT) {
                try {
                    new_image = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    setImage(new_image);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == CAMERA_INPUT) {

                if (data != null && data.getExtras() != null) {
                    new_image = Bitmap.createBitmap((Bitmap) data.getExtras().get("data"));
                    setImage(new_image);
                }
            } else if (requestCode == OWN_GALERY) {
                if (data != null && data.getExtras() != null) {
                    new_image = BitmapFactory.decodeResource(getResources(), (Integer) data.getExtras().get(GaleryActivity.RESOURCE_SELECTED));
                    setImage(new_image);
                }
            }
        }
    }


    private void setImage(Bitmap bitmap) {
        dish_image.setImageBitmap(bitmap);


    }

    private void saveImage(String image_name){
        //Creamos los ficheros de imagenes
        String path = FileSystem.getInstance(this).getUriNuevaImagen(image_name).getPath();
        File fTempOriginal = new File(path);

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(fTempOriginal);
            new_image.compress(Bitmap.CompressFormat.JPEG, 70, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
