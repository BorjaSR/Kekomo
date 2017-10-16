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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bsalazar.kekomo.R;
import com.bsalazar.kekomo.bbdd.entities.Product;

import java.util.ArrayList;

/**
 * Created by bsalazar on 13/10/17.
 */

public class PantryActivity  extends AppCompatActivity {

    private Activity activity;
    private GridView pantry_products_grid;
    private LinearLayout shadow;
    private RecyclerView rvProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        activity = this;

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        shadow = (LinearLayout) findViewById(R.id.shadow);

        ArrayList<Product> products = new ArrayList<>();

        for(int i = 0; i < 10; i++) {
            Product product = new Product();
            product.setName("pechugas de pollo");
            product.setType(Product.MEAT);
            product.setStock(100);
            product.setFrozen(true);

            Product product1 = new Product();
            product1.setName("Salmón");
            product1.setType(Product.FISH);
            product1.setStock(50);
            product1.setFrozen(true);

            Product product2 = new Product();
            product2.setName("Pimiento verde");
            product2.setType(Product.VEGETABLE);
            product2.setStock(10);
            product2.setFrozen(false);

            Product product3 = new Product();
            product3.setName("Naranjas");
            product3.setType(Product.FRUIT);
            product3.setStock(10);
            product3.setFrozen(false);

            Product product4 = new Product();
            product4.setName("Soja");
            product4.setType(Product.SAUCE);
            product4.setStock(10);
            product4.setFrozen(false);

            products.add(product);
            products.add(product1);
            products.add(product2);
            products.add(product3);
            products.add(product4);
        }

        rvProducts = (RecyclerView) findViewById(R.id.rvProducts);
        rvProducts.setLayoutManager(new GridLayoutManager(this, 3));
        rvProducts.setHasFixedSize(false);
        PantryAdapter adapter = new PantryAdapter(this, products);
        rvProducts.setAdapter(adapter);

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
                return true;
        }
        return true;
    }


    public void displayPopupWindow(View anchorView, Product product) {

        PopupWindow popup = new PopupWindow(this);
        View layout = getLayoutInflater().inflate(R.layout.popup_content, null);

        ((TextView) layout.findViewById(R.id.popup_product_name)).setText(product.getName());


        popup.setContentView(layout);
        // Set content width and height
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        // Closes the popup window when touch outside of it - when looses focus
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        // Show anchored to button
//        popup.showAtLocation(anchorView, Gravity.BOTTOM, 0, anchorView.getBottom() - 60);

        popup.showAsDropDown(anchorView, 0, 10, Gravity.CENTER_HORIZONTAL);
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                shadow.animate().alpha(0).setDuration(200).start();
            }
        });

        shadow.setVisibility(View.VISIBLE);
        shadow.animate().alpha(1).setDuration(200).start();
    }
}
