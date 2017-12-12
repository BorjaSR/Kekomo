package com.bsalazar.kekomo.ui_dishes;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bsalazar.kekomo.BuildConfig;
import com.bsalazar.kekomo.R;
import com.bsalazar.kekomo.bbdd.controllers.DishesController;
import com.bsalazar.kekomo.bbdd.controllers.ProductController;
import com.bsalazar.kekomo.bbdd.entities.Dish;
import com.bsalazar.kekomo.bbdd.entities.Product;
import com.bsalazar.kekomo.general.Constants;
import com.bsalazar.kekomo.general.FileSystem;
import com.bsalazar.kekomo.general.GaleryActivity;
import com.bsalazar.kekomo.ui_dishes.adapters.IngredientsAdapter;
import com.bsalazar.kekomo.ui_dishes.adapters.ProductsAutoCompleteAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bsalazar on 22/06/2017.
 */

public class NewDishActivity extends AppCompatActivity implements View.OnClickListener {

    private Activity activity;
    private LinearLayout edit_image_options, data_container;
    private LinearLayout camera_button, gallery_button, kekomo_galley_button, delete_image;
    private AutoCompleteTextView ingredients_autoTextView;
    private ImageView dish_image, edit_image;
    private EditText dish_name, dish_description, dish_preparation;
    private RecyclerView ingredients_recycler;
    private IngredientsAdapter ingredientsAdapter;
    private ArrayList<Product> ingredients;

    private final int GALERY_INPUT = 1;
    private final int CAMERA_INPUT = 2;
    private final int OWN_GALERY = 3;

    private Bitmap new_image;
    private boolean isImageOprionShowed = false;

    private Dish dishToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dish);
        activity = this;

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        int dishID = -1;
        if (getIntent().getExtras() != null)
            dishID = (int) getIntent().getExtras().get("DISH_TO_EDIT");

        dishToEdit = null;
        if (dishID != -1)
            dishToEdit = new DishesController().getByID(dishID);


        dish_image = (ImageView) findViewById(R.id.dish_image);
        edit_image = (ImageView) findViewById(R.id.edit_image);
        edit_image_options = (LinearLayout) findViewById(R.id.edit_image_options);
        data_container = (LinearLayout) findViewById(R.id.data_container);
        camera_button = (LinearLayout) findViewById(R.id.camera_button);
        gallery_button = (LinearLayout) findViewById(R.id.gallery_button);
        kekomo_galley_button = (LinearLayout) findViewById(R.id.kekomo_galley_button);
        delete_image = (LinearLayout) findViewById(R.id.delete_image);
        dish_name = (EditText) findViewById(R.id.dish_name);
        dish_description = (EditText) findViewById(R.id.dish_description);
        dish_preparation = (EditText) findViewById(R.id.dish_preparation);
        ingredients_autoTextView = (AutoCompleteTextView) findViewById(R.id.ingredients_auto);

        ingredients_recycler = (RecyclerView) findViewById(R.id.ingredients_recycler);
//        ingredients_recycler.setHasFixedSize(false);
        ingredients_recycler.setLayoutManager(new LinearLayoutManager(this));

        ingredients = new ArrayList<>();
        ingredientsAdapter = new IngredientsAdapter(this, ingredients);
        ingredients_recycler.setAdapter(ingredientsAdapter);

        try {
            ProductsAutoCompleteAdapter adapter = new ProductsAutoCompleteAdapter(this, android.R.layout.simple_list_item_1, new ProductController().getAll());
            ingredients_autoTextView.setAdapter(adapter);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        edit_image.setOnClickListener(this);
        dish_image.setOnClickListener(this);
        camera_button.setOnClickListener(this);
        gallery_button.setOnClickListener(this);
        kekomo_galley_button.setOnClickListener(this);
        delete_image.setOnClickListener(this);

        if (dishToEdit != null) {

            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle("Editar plato");

            dish_name.setText(dishToEdit.getName());
            dish_description.setText(dishToEdit.getDescription());
            dish_preparation.setText(dishToEdit.getPreparation());

            try {
                new_image = BitmapFactory.decodeFile(FileSystem.getInstance(this).IMAGES_PATH + dishToEdit.getImage());
            } catch (Exception e) {
                e.printStackTrace();
            }

            dish_image.setImageBitmap(new_image);
        }

        ingredients_autoTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Product productSelected = (Product) ingredients_autoTextView.getAdapter().getItem(position);
                ingredients.add(productSelected);
                ingredientsAdapter.notifyItemInserted(ingredients.size() - 1);
                ingredients_autoTextView.setText("");
            }
        });

        ingredients_autoTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                Product product = new Product();
                product.setName(ingredients_autoTextView.getText().toString());
                product.setStock(1);
                product.setType(Product.NOT_DEFINED);
                product.setFrozen(false);

//                product = new ProductController().add(product, Constants.database);

                ingredients.add(product);
                ingredientsAdapter.notifyItemInserted(ingredients.size() - 1);
                ingredients_autoTextView.setText("");

                return true;
            }
        });

        ingredientsAdapter.setOnClickDelete(new IngredientsAdapter.OnClickDelete() {
            @Override
            public void onDeleteItem(Product product, int position) {
                TransitionManager.beginDelayedTransition(data_container);

                ingredients.remove(product);
                ingredientsAdapter.notifyItemRemoved(position);
            }
        });

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(dish_name.getWindowToken(), 0);
    }


    boolean edited = false;

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
                if (dishToEdit == null)
                    saveDish();
                else
                    updateDish();
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
//                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(takePictureIntent, CAMERA_INPUT);
                dispatchTakePictureIntent();
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
            dish.setPreparation(dish_preparation.getText().toString());
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

    private void updateDish() {
        if (dish_name.getText().length() > 0) {
            dishToEdit.setName(dish_name.getText().toString());
            dishToEdit.setDescription(dish_description.getText().toString());
            dishToEdit.setPreparation(dish_preparation.getText().toString());
            dishToEdit.setTags("x");
            saveImage(dishToEdit.getImage());
            new DishesController().update(dishToEdit, Constants.database);
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
                new_image = handleBigCameraPhoto();
                setImage(new_image);


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
                ViewAnimationUtils.createCircularReveal(myView, myView.getMeasuredWidth() - 90, cy + 50, 0, finalRadius);

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
                ViewAnimationUtils.createCircularReveal(myView, myView.getMeasuredWidth() - 90, cy + 50, initialRadius, 0);


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

    private void hashtagRecognizer() {
        if (!edited) {
            String text = dish_description.getText().toString();
            SpannableString hashText = new SpannableString(text);
            Matcher matcher = Pattern.compile("#([A-Za-z0-9_-]+)").matcher(hashText);

            while (matcher.find()) {
                hashText.setSpan(new StyleSpan(Typeface.BOLD), matcher.start(), matcher.end(), 0);
                String tag = matcher.group(0);
            }

            edited = true;
            dish_description.setText(hashText);
        } else {
            edited = false;
            dish_description.setSelection(dish_description.getText().length());
        }
    }


    private String mCurrentPhotoPath;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            Uri uri = getOutputMediaFileUri();
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        } catch (Exception e) {
            e.printStackTrace();
            mCurrentPhotoPath = null;
        }

        startActivityForResult(takePictureIntent, CAMERA_INPUT);
    }


    private Uri getOutputMediaFileUri() {
        //check for external storage
        if (isExternalStorageAvaiable()) {
            File mediaStorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File mediaFile;

            try {
                mediaFile = new File(mediaStorageDir.getPath() + "/temp.jpg");
                Log.i("st", "File: " + Uri.fromFile(mediaFile));
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("St", "Error creating file: " + mediaStorageDir.getAbsolutePath() + "/temp.jpg");
                return null;
            }

            Uri uri = FileProvider.getUriForFile(getApplicationContext(),
                    BuildConfig.APPLICATION_ID + ".provider",
                    mediaFile);

            mCurrentPhotoPath = mediaFile.getAbsolutePath();
            return uri;
        }
        //something went wrong
        return null;
    }

    private boolean isExternalStorageAvaiable() {

        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "temp.jpeg";
        File albumF = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageF = new File(albumF.getPath() + "/" + imageFileName);

        if (imageF.exists()) {
            imageF.delete();
        }
        Boolean res = imageF.createNewFile();
        return imageF;
    }


    private Bitmap handleBigCameraPhoto() {
        Bitmap bitmap = null;
        if (mCurrentPhotoPath != null) {
            bitmap = setPic();
            mCurrentPhotoPath = null;
        }
        return bitmap;
    }

    private Bitmap setPic() {

		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = 1;
        bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
        return BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
    }
}
