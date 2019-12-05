package com.bsalazar.kekomo.ui_pantry;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.bsalazar.kekomo.R;
import com.bsalazar.kekomo.data.LocalDataSource;
import com.bsalazar.kekomo.data.entities.Product;
import com.bsalazar.kekomo.ui_dishes.adapters.ProductsAutoCompleteAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bsalazar on 10/05/2017.
 */

public class AddProductDialogFragment extends DialogFragment {

    SnapHelper snapHelper;
    LinearLayoutManager linearLayoutManager;
    private ArrayList<Product> notSavedProducts = new ArrayList<>();
    private Product productToSave = new Product();

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

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.add_product_dialog_fragment, container, false);


        final RecyclerView type_recycler = rootView.findViewById(R.id.type_recycler);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        type_recycler.setLayoutManager(linearLayoutManager);
        type_recycler.setHasFixedSize(false);
        type_recycler.setAdapter(new ProductTypeAdapter(getActivity()));

        snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(type_recycler);
        snapHelper.findSnapView(linearLayoutManager);

        notSavedProducts = (ArrayList<Product>) LocalDataSource.getInstance(getContext()).getProductsNotSaved();
        ProductsAutoCompleteAdapter adapter = new ProductsAutoCompleteAdapter(getContext(), android.R.layout.simple_list_item_1, notSavedProducts);
        AutoCompleteTextView productsAuto = rootView.findViewById(R.id.product_name_auto);
        productsAuto.setAdapter(adapter);

        productsAuto.setOnItemClickListener((parent, view, position, id) -> productToSave = notSavedProducts.get(position));

        rootView.findViewById(R.id.add_product_button).setOnClickListener(v -> {
            productToSave.setName(productsAuto.getText().toString());
            productToSave.setStock(1);
            productToSave.setType((int) snapHelper.findSnapView(linearLayoutManager).getTag());
            productToSave.setFrozen(((Switch) rootView.findViewById(R.id.switch_product_frozen)).isChecked());
            productToSave.setSaved(true);

            int productID = productToSave.getId() != 0 ?
                    (int) LocalDataSource.getInstance(getContext()).updateProduct(productToSave) :
                    (int) LocalDataSource.getInstance(getContext()).saveProduct(productToSave);

            productToSave = LocalDataSource.getInstance(getContext()).getProductByID(productID);
            if (productToSave != null)
                ((PantryActivity) getActivity()).insertProduct(productToSave);
            else
                Toast.makeText(getContext(), "Error añadiendo producto", Toast.LENGTH_SHORT).show();

            dismiss();
        });

        rootView.findViewById(R.id.background_add_product).setOnClickListener(v -> dismiss());

        return rootView;
    }
}
