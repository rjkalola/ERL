
package com.app.erl.model.entity.info;

public class ItemInfo {
    private int id, lu_service_hour_type_id, lu_service_category_id, dry_clean_price, iron_dry_clean_price, iron_price;
    private String name, image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLu_service_hour_type_id() {
        return lu_service_hour_type_id;
    }

    public void setLu_service_hour_type_id(int lu_service_hour_type_id) {
        this.lu_service_hour_type_id = lu_service_hour_type_id;
    }

    public int getLu_service_category_id() {
        return lu_service_category_id;
    }

    public void setLu_service_category_id(int lu_service_category_id) {
        this.lu_service_category_id = lu_service_category_id;
    }

    public int getDry_clean_price() {
        return dry_clean_price;
    }

    public void setDry_clean_price(int dry_clean_price) {
        this.dry_clean_price = dry_clean_price;
    }

    public int getIron_dry_clean_price() {
        return iron_dry_clean_price;
    }

    public void setIron_dry_clean_price(int iron_dry_clean_price) {
        this.iron_dry_clean_price = iron_dry_clean_price;
    }

    public int getIron_price() {
        return iron_price;
    }

    public void setIron_price(int iron_price) {
        this.iron_price = iron_price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}



