
package com.app.erl.model.entity.info;

public class ModuleInfo {
    private int id, city_id;
    private String name, zip_code;
    private boolean check;

    public void copyData(ModuleInfo info){
        this.id = info.getId();
        this.city_id = info.getCity_id();
        this.name = info.getName();
        this.zip_code = info.getZip_code();
        this.check = info.isCheck();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}



