package com.example.waqas.hisaabkitaab;

public class Milk_Items{

    private int ID;
    private String DateNTime;
    private String Milk_Quantity;
    private int Total_Milk;

    public Milk_Items(String dateNTime, String milk_Quantity, int total_Milk) {
        DateNTime = dateNTime;
        Milk_Quantity = milk_Quantity;
        Total_Milk = total_Milk;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDateNTime() {
        return DateNTime;
    }

    public void setDateNTime(String dateNTime) {
        DateNTime = dateNTime;
    }

    public String getMilk_Quantity() {
        return Milk_Quantity;
    }

    public void setMilk_Quantity(String milk_Quantity) {
        Milk_Quantity = milk_Quantity;
    }

    public int getTotal_Milk() {
        return Total_Milk;
    }

    public void setTotal_Milk(int total_Milk) {
        Total_Milk = total_Milk;
    }
}
