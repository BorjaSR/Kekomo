package com.bsalazar.kekomo.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by borja.salazar on 23/02/2018.
 */


@Entity(tableName = "DishProducts", primaryKeys = {"dishId", "productId"})
public class RelaionDishProducts {

    @ForeignKey(entity = Dish.class,
            parentColumns = "id",
            childColumns = "dishId")
    private int dishId;

    @ForeignKey(entity = Product.class,
            parentColumns = "id",
            childColumns = "productId")
    private int productId;

    public RelaionDishProducts() {
    }

    public RelaionDishProducts(int dishId, int productId) {
        this.dishId = dishId;
        this.productId = productId;
    }

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
