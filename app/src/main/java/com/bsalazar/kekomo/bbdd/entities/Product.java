package com.bsalazar.kekomo.bbdd.entities;

import java.util.Date;

/**
 * Created by bsalazar on 13/10/17.
 */

public class Product {

    public static final int MEAT = 0;
    public static final int FISH = 1;
    public static final int VEGETABLE = 2;
    public static final int DAIRY = 3;
    public static final int SAUCE = 4;
    public static final int FRUIT = 5;

    private int id;
    private String name;
    private int type;
    private int stock;
    private boolean frozen;


    private Date created;
    private Date updated;
    private boolean deleted;

    public Product() {
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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
