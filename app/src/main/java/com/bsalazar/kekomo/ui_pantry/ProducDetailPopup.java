package com.bsalazar.kekomo.ui_pantry;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import com.bsalazar.kekomo.R;
import com.bsalazar.kekomo.bbdd_room.entities.Product;

/**
 * Created by bsalazar on 20/10/17.
 */

public class ProducDetailPopup extends PopupWindow {

    private OnClickDelete onClickDelete;

    public ProducDetailPopup(Context context, View anchorView, final int index, final Product product) {
        super(context);

        int relativePosition = index % 3;

        View layout = ((PantryActivity) context).getLayoutInflater().inflate(R.layout.popup_content, null);

        ImageView indicator;
        if (relativePosition == 0)
            indicator = (ImageView) layout.findViewById(R.id.indicator1);
        else if (relativePosition == 1)
            indicator = (ImageView) layout.findViewById(R.id.indicator2);
        else
            indicator = (ImageView) layout.findViewById(R.id.indicator3);
        indicator.setVisibility(View.VISIBLE);

        ((TextView) layout.findViewById(R.id.popup_product_name)).setText(product.getName());
        ((ImageView) layout.findViewById(R.id.image_popup_product)).setImageResource(getProductIcon(product.getType()));
        Switch frozen_switch = (Switch) layout.findViewById(R.id.switch_frozen);
        frozen_switch.setChecked(product.isFrozen());
        frozen_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                product.setFrozen(b);
            }
        });

        layout.findViewById(R.id.delete_product).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickDelete != null)
                    onClickDelete.onDeleteItem(product);
            }
        });

        setContentView(layout);
        // Set content width and height
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        // Closes the popup window when touch outside of it - when looses focus
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());
//        popup.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        // Show anchored to button
//        popup.showAtLocation(anchorView, Gravity.TOP, (int) anchorView.getX(), (int) anchorView.getY());
        showAsDropDown(anchorView, 0, -40, Gravity.TOP);
    }

    public void setOnClickDelete(OnClickDelete onClickDelete) {
        this.onClickDelete = onClickDelete;
    }

    private int getProductIcon(int type) {
        switch (type) {
            case Product.MEAT:
                return R.drawable.steak;
            case Product.FISH:
                return R.drawable.fish;
            case Product.VEGETABLE:
                return R.drawable.vegetables;
            case Product.DAIRY:
                return R.drawable.dairy;
            case Product.SAUCE:
                return R.drawable.sauces;
            case Product.FRUIT:
                return R.drawable.fruit;
            default:
                return R.drawable.steak;
        }

    }

    public interface OnClickDelete {
        void onDeleteItem(Product product);
    }
}
