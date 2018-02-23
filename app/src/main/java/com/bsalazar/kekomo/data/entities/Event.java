package com.bsalazar.kekomo.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by bsalazar on 26/06/2017.
 */

@Entity(tableName = "Events")
public class Event {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @Expose
    private int id;
    @ForeignKey(entity = Dish.class,
            parentColumns = "id",
            childColumns = "dishID")
    @SerializedName("dishID")
    @Expose
    private int dishID;
    @SerializedName("Date")
    @Expose
    private String Date;
    @SerializedName("type")
    @Expose
    private int type;

    @SerializedName("created")
    @Expose
    private Long created;
    @SerializedName("updated")
    @Expose
    private Long updated;
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

    public int getDishID() {
        return dishID;
    }

    public void setDishID(int dishID) {
        this.dishID = dishID;
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
