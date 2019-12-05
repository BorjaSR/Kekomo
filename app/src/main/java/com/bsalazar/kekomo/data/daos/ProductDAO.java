package com.bsalazar.kekomo.data.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.bsalazar.kekomo.data.entities.Product;

import java.util.List;

/**
 * Created by borja.salazar on 20/02/2018.
 */

@Dao
public interface ProductDAO {

    @Query("SELECT * FROM Products")
    List<Product> getAll();

    @Query("SELECT * FROM Products WHERE isSaved = 1")
    List<Product> getAllSaved();

    @Query("SELECT * FROM Products WHERE isSaved = 0")
    List<Product> getAllNotSaved();

    @Query("SELECT * FROM Products WHERE id = :productID")
    Product getByID(int productID);

    @Update
    void updateProduct(Product product);

    @Insert
    long insertProduct(Product product);

    @Delete
    void deleteProduct(Product product);

    @Query("DELETE FROM Products")
    void clear();

}