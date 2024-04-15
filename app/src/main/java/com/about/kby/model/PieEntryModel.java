package com.about.kby.model;

public class PieEntryModel {
    String id,subcategory,duriation,category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    int color;

    public PieEntryModel(String subategory, String duriation, int color) {
        this.subcategory=subategory;
        this.duriation=duriation;
        this.color=color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getDuriation() {
        return duriation;
    }

    public void setDuriation(String duriation) {
        this.duriation = duriation;
    }
}
