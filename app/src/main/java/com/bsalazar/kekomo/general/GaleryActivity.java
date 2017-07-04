package com.bsalazar.kekomo.general;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.GridView;


import com.bsalazar.kekomo.R;

import java.util.ArrayList;

/**
 * Created by bsalazar on 27/06/2017.
 */

public class GaleryActivity extends AppCompatActivity {

    private Context mContext;
    public static String RESOURCE_SELECTED = "RESOURCE_SELECTED";

    private GridImagesAdapter gridImagesAdapter;
    private ArrayList<Integer> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galery);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = this;

        GridView grid_images = (GridView) findViewById(R.id.grid_images);
        images = new ArrayList<>();
        fillImages();
        gridImagesAdapter = new GridImagesAdapter(mContext, R.layout.grid_image_item, images);
        grid_images.setAdapter(gridImagesAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                return true;
        }
        return true;
    }

    public void setGridResult(int resource){
        Intent intent = getIntent();
        intent.putExtra(RESOURCE_SELECTED, resource);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void fillImages() {
        images.add(R.drawable.pasta);
        images.add(R.drawable.pescado);
        images.add(R.drawable.filete_patatas);
        images.add(R.drawable.verduras);
    }
}
