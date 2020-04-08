
package com.app.erl.model.entity.response;

public class BankInfoResponse extends BaseResponse {
    private String account_name;
    private String account_number;
    private String bank_name;
    private String sort_code;
    private String swift_code;
    private String iban;
    private String building_society_roll_no;

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getSort_code() {
        return sort_code;
    }

    public void setSort_code(String sort_code) {
        this.sort_code = sort_code;
    }

    public String getSwift_code() {
        return swift_code;
    }

    public void setSwift_code(String swift_code) {
        this.swift_code = swift_code;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getBuilding_society_roll_no() {
        return building_society_roll_no;
    }

    public void setBuilding_society_roll_no(String building_society_roll_no) {
        this.building_society_roll_no = building_society_roll_no;
    }
}

