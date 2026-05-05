package com.example.lostandfoundapp;

public class LostFoundItem {
    private final int id;
    private final String postType;
    private final String name;
    private final String phone;
    private final String description;
    private final String category;
    private final String location;
    private final String imagePath;
    private final String createdAt;

    public LostFoundItem(int id, String postType, String name, String phone, String description,
                         String category, String location, String imagePath, String createdAt) {
        this.id = id;
        this.postType = postType;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.category = category;
        this.location = location;
        this.imagePath = imagePath;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public String getPostType() { return postType; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getLocation() { return location; }
    public String getImagePath() { return imagePath; }
    public String getCreatedAt() { return createdAt; }
}
