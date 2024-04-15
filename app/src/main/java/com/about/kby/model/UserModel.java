package com.about.kby.model;


import org.json.JSONException;
import org.json.JSONObject;

public class UserModel {
    private String customer_email;
    private String customer_id;
    private String customer_mobile;
    private String customer_name;

    public UserModel() {
    }

    public UserModel(JSONObject jsonObject) {
        try {
            if (jsonObject.has("customer_id")) {
                this.customer_id = jsonObject.getString("customer_id");
            }
            if (jsonObject.has("customer_name")) {
                this.customer_name = jsonObject.getString("customer_name");
            }
            if (jsonObject.has("customer_mobile")) {
                this.customer_mobile = jsonObject.getString("customer_mobile");
            }
            if (jsonObject.has("customer_email")) {
                this.customer_email = jsonObject.getString("customer_email");
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_mobile() {
        return customer_mobile;
    }

    public void setCustomer_mobile(String customer_mobile) {
        this.customer_mobile = customer_mobile;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

}
