package com.bsalazar.kekomo.ui_pantry;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import com.bsalazar.kekomo.R;
import com.bsalazar.kekomo.bbdd.controllers.ProductController;
import com.bsalazar.kekomo.bbdd.entities.Product;
import com.bsalazar.kekomo.general.Constants;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by bsalazar on 13/10/17.
 */

public class PantryActivity  extends AppCompatActivity {

    private final int NUM_GRID_COLUMS = 3;

    private Activity activity;
    ArrayList<Product> products = new ArrayList<>();
    private LinearLayout shadow;
    private RecyclerView rvProducts;
    private PantryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        activity = this;

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        shadow = (LinearLayout) findViewById(R.id.shadow);

        try {
            products = new ProductController().getAll();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        rvProducts = (RecyclerView) findViewById(R.id.rvProducts);
        rvProducts.setLayoutManager(new GridLayoutManager(this, NUM_GRID_COLUMS));
        rvProducts.setHasFixedSize(false);
        adapter = new PantryAdapter(this, products);
        rvProducts.setAdapter(adapter);
    }

    public void insertProduct(Product product){
        products.add(product);
        adapter.notifyItemInserted(products.size()-1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pantry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_add:
                AddProductDialogFragment dialog = new AddProductDialogFragment();
                dialog.show(this.getFragmentManager(), "ADD_PRODUCT");
                return true;
        }
        return true;
    }


    boolean isDelete = false;
    public void displayPopupWindow(View anchorView, final int index) {

        final ProducDetailPopup popupWindow = new ProducDetailPopup(this, anchorView, index, products.get(index));
        popupWindow.setOnClickDelete(new ProducDetailPopup.OnClickDelete() {
            @Override
            public void onDeleteItem(Product product) {
                new ProductController().deleteByID(product.getId());
                products.remove(index);
                adapter.notifyItemRemoved(index);
                isDelete = true;
                popupWindow.dismiss();
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if(!isDelete){
                    new ProductController().update(products.get(index), Constants.database);
                    adapter.notifyItemChanged(index);
                    isDelete = false;
                }
                shadow.animate().alpha(0).setDuration(200).start();
            }
        });

        shadow.setVisibility(View.VISIBLE);
        shadow.animate().alpha(1).setDuration(200).start();
    }
}
