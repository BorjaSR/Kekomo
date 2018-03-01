package com.bsalazar.kekomo.data.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.bsalazar.kekomo.data.entities.DishProducts;
import com.bsalazar.kekomo.data.entities.Product;

import java.util.List;

/**
 * Created by borja.salazar on 20/02/2018.
 */

@Dao
public interface DishProductDAO {

    @Query("SELECT * FROM Products INNER JOIN DishProducts ON Products.id = DishProducts.productId WHERE DishProducts.dishId = :dishId")
    List<Product> getProductsByDish(int dishId);

    @Insert
    void insertDishProduct(DishProducts product);

    @Delete
    void deleteDishProduct(DishProducts product);

    @Query("DELETE FROM DishProducts")
    void clear();
}