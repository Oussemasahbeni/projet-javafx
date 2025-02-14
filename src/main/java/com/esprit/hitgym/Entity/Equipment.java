package com.esprit.hitgym.Entity;

public class Equipment {
    private String name;
    private String imageUrl;
    private String category;

    public Equipment(String name, String imageUrl, String category) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
