package com.example.ergasia2;

public class CartModel {

    // a model class for the cart recycler view to display the books on the cart and their information

    private String name, price, quantity, id, cat;

    public CartModel() {

    }

    public CartModel(String name, String price, String quantity, String id, String cat) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.id = id;
        this.cat = cat;
    }

    public String getCat() { return cat; }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getQuantity() { return quantity; }

    public void setQuantity(String quantity) { this.quantity = quantity; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
