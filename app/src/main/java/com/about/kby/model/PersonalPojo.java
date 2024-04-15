package com.about.kby.model;

public class PersonalPojo {
    String id ,category,subategory,duriation,singleLineSubCategoryDuration;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSingleLineSubCategoryDuration() {
        return singleLineSubCategoryDuration;
    }

    public void setSingleLineSubCategoryDuration(String singleLineSubCategoryDuration) {
        this.singleLineSubCategoryDuration = singleLineSubCategoryDuration;
    }

    public String getSubategory() {
        return subategory;
    }

    public PersonalPojo setSubategory(String subategory) {
        this.subategory = subategory;
        return null;
    }

    public String getDuriation() {
        return duriation;
    }

    public PersonalPojo setDuriation(String duriation) {
        this.duriation = duriation;
        return null;
    }
}
