package com.bsalazar.kekomo.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by bsalazar on 13/10/17.
 */

@Entity(tableName = "Products")
public class Product {

    @Ignore
    public static final int NOT_DEFINED = -1;
    @Ignore
    public static final int MEAT = 0;
    @Ignore
    public static final int FISH = 1;
    @Ignore
    public static final int VEGETABLE = 2;
    @Ignore
    public static final int DAIRY = 3;
    @Ignore
    public static final int SAUCE = 4;
    @Ignore
    public static final int FRUIT = 5;

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private int type;
    private int stock;
    private boolean frozen;
    private boolean isSaved;

    private Long created;
    private Long updated;
    private boolean deleted;

    public Product() {
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Long getUpdated() {
        return updated;
    }

    public void setUpdated(Long updated) {
        this.updated = updated;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
