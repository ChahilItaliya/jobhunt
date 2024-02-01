package com.example.jobhunt;

public class Card {
    private String title;
    private String description;
    private String documentId;


    public Card() {
        // Default constructor required for Firebase
    }

    public Card(String title, String description,String documentId) {
//        this.photo = photo;
        this.title = title;
        this.description = description;
        this.documentId = documentId;

    }

//    public String getPhoto() {
//        return photo;
//    }
//
//    public void setPhoto(String photo) {
//        this.photo = photo;
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
