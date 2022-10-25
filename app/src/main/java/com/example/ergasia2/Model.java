package com.example.ergasia2;

public class Model {

    // a model class for the book display recycler view to display the books on the cart and their information

    private String name, price, imageUrl, availability, description, id, cat;


    public Model(){

    }

    public Model(String name, String price, String imageUrl, String availability, String description, String id, String cat) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.availability = availability;
        this.description = description;
        this.id = id;
        this.cat = cat;
    }

    public String getCat() { return cat; }

    public void setCat(String cat) { this.cat = cat; }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
