package com.about.kby.model;

import java.util.List;

public class OpenModel {
    String id,category,user_name,time,date,task,active,inactive,description;
    private List<SubCategoryModel> subcategory;

    public List<SubCategoryModel> checktask ;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInactive() {
        return inactive;
    }

    public void setInactive(String inactive) {
        this.inactive = inactive;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }


    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    // getters and setters
    public List<SubCategoryModel> getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(List<SubCategoryModel> subcategory) {
        this.subcategory = subcategory;
    }

    public List<SubCategoryModel> getChecktask() {
        return checktask;
    }

    public void setChecktask(List<SubCategoryModel> checktask) {
        this.checktask = checktask;
    }

    // other methods

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

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }





    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
