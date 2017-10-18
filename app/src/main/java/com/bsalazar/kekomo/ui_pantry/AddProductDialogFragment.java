package com.bsalazar.kekomo.ui_pantry;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.bsalazar.kekomo.R;
import com.bsalazar.kekomo.bbdd.controllers.ProductController;
import com.bsalazar.kekomo.bbdd.entities.Product;
import com.bsalazar.kekomo.general.Constants;

/**
 *
 * Created by bsalazar on 10/05/2017.
 */

public class AddProductDialogFragment extends DialogFragment {

    private TextView add_product_button;

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.CommentsDialogAnimation;
        getDialog().setCancelable(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            d.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.add_product_dialog_fragment, container,
                false);

        rootView.findViewById(R.id.add_product_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Product product = new Product();
                product.setName(((EditText) rootView.findViewById(R.id.product_name)).getText().toString());
                product.setStock(1);
                product.setType(Product.FISH);
                product.setFrozen(((Switch) rootView.findViewById(R.id.switch_product_frozen)).isChecked());

                ((PantryActivity) getActivity()).insertProduct(new ProductController().add(product, Constants.database));
                dismiss();
            }
        });

        rootView.findViewById(R.id.background_add_product).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return rootView;
    }
}
