package com.bsalazar.kekomo.UI_dishes;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bsalazar.kekomo.R;
import com.bsalazar.kekomo.bbdd.controllers.DishesController;
import com.bsalazar.kekomo.bbdd.entities.Dish;
import com.bsalazar.kekomo.general.Constants;
import com.bsalazar.kekomo.general.FileSystem;
import com.bsalazar.kekomo.general.GaleryActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by bsalazar on 22/06/2017.
 */

public class NewDishActivity extends AppCompatActivity implements View.OnClickListener {

    private Activity activity;
    private LinearLayout edit_image_options;
    private LinearLayout camera_button, gallery_button, kekomo_galley_button, delete_image;
    private ImageView dish_image, edit_image;
    private EditText dish_name, dish_description;

    private final int GALERY_INPUT = 1;
    private final int CAMERA_INPUT = 2;
    private final int OWN_GALERY = 3;

    private Bitmap new_image;
    private boolean isImageOprionShowed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dish);
        activity = this;

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dish_image = (ImageView) findViewById(R.id.dish_image);
        edit_image = (ImageView) findViewById(R.id.edit_image);
        edit_image_options = (LinearLayout) findViewById(R.id.edit_image_options);
        camera_button = (LinearLayout) findViewById(R.id.camera_button);
        gallery_button = (LinearLayout) findViewById(R.id.gallery_button);
        kekomo_galley_button = (LinearLayout) findViewById(R.id.kekomo_galley_button);
        delete_image = (LinearLayout) findViewById(R.id.delete_image);
        dish_name = (EditText) findViewById(R.id.dish_name);
        dish_description = (EditText) findViewById(R.id.dish_description);


        edit_image.setOnClickListener(this);
        dish_image.setOnClickListener(this);
        camera_button.setOnClickListener(this);
        gallery_button.setOnClickListener(this);
        kekomo_galley_button.setOnClickListener(this);
        delete_image.setOnClickListener(this);

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
                return true;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dish_image:
                if (isImageOprionShowed)
                    hideImageOption();
                break;

            case R.id.edit_image:
                showImageOption();
                break;
            case R.id.camera_button:
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, CAMERA_INPUT);
                hideImageOption();
                break;
            case R.id.gallery_button:
                openGalery();
                hideImageOption();
                break;
            case R.id.kekomo_galley_button:
                startActivityForResult(new Intent(getApplicationContext(), GaleryActivity.class), OWN_GALERY);
                hideImageOption();
                break;
            case R.id.delete_image:
                new_image = null;
                setImage(null);
                hideImageOption();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isImageOprionShowed)
            hideImageOption();
        else
            super.onBackPressed();
    }

    private void showImageOption() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            enterReveal(edit_image_options);
        else
            edit_image_options.setVisibility(View.VISIBLE);

        isImageOprionShowed = true;
    }

    private void hideImageOption() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            exitReveal(edit_image_options);
        else
            edit_image_options.setVisibility(View.GONE);

        isImageOprionShowed = false;
    }

    private void saveDish() {
        if (dish_name.getText().length() > 0) {
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
            finish();
        } else
            Snackbar.make(dish_image, "El nombre es obligatorio", Snackbar.LENGTH_SHORT).show();
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
                .setNegativeButton(getString(R.string.gallery), new DialogInterface.OnClickListener() {
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

    private void saveImage(String image_name) {
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void enterReveal(View myView) {

        // get the center for the clipping circle
        int cx = myView.getMeasuredWidth() / 2;
        int cy = myView.getMeasuredHeight() / 2;

        // get the final radius for the clipping circle
        int finalRadius = Math.max(myView.getWidth(), myView.getHeight());

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(myView, myView.getMeasuredWidth() - 90, cy+50, 0, finalRadius);

        anim.setDuration(400);
        anim.setInterpolator(new AccelerateInterpolator());
        // make the view visible and start the animation
        myView.setVisibility(View.VISIBLE);
        anim.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void exitReveal(final View myView) {

        // get the center for the clipping circle
        int cx = myView.getMeasuredWidth() / 2;
        int cy = myView.getMeasuredHeight() / 2;

        // get the initial radius for the clipping circle
        int initialRadius = myView.getWidth();

        // create the animation (the final radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(myView, myView.getMeasuredWidth() - 90, cy+50, initialRadius, 0);


        anim.setDuration(400);
        anim.setInterpolator(new DecelerateInterpolator());
        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                myView.setVisibility(View.INVISIBLE);
            }
        });

        // start the animation
        anim.start();
    }
}
