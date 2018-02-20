package com.bsalazar.kekomo.bbdd_room.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by bsalazar on 26/06/2017.
 */

@Entity(tableName = "Events")
public class Event {

    @PrimaryKey
    @SerializedName("eventID")
    @Expose
    private int id;
    @ForeignKey(entity = Dish.class,
            parentColumns = "dishID",
            childColumns = "DishId")
    @SerializedName("DishId")
    @Expose
    private int DishId;
    @SerializedName("Date")
    @Expose
    private String Date;
    @SerializedName("type")
    @Expose
    private int type;

    @SerializedName("created")
    @Expose
    private Date created;
    @SerializedName("updated")
    @Expose
    private Date updated;
    @SerializedName("deleted")
    @Expose
    private boolean deleted;

    public Event() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDishId() {
        return DishId;
    }

    public void setDishId(int dishId) {
        DishId = dishId;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public java.util.Date getCreated() {
        return created;
    }

    public void setCreated(java.util.Date created) {
        this.created = created;
    }

    public java.util.Date getUpdated() {
        return updated;
    }

    public void setUpdated(java.util.Date updated) {
        this.updated = updated;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
