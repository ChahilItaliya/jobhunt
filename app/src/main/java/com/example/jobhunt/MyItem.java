package com.example.jobhunt;

public class MyItem {
    private String title;
    private String description;
    private String documentId,Id;
    private String photo;


    public MyItem() {
        // Default constructor required for Firebase
    }

    public MyItem(String title, String description,String documentId,String Id,String photo) {
        this.photo = photo;
        this.title = title;
        this.description = description;
        this.documentId = documentId;
        this.Id=Id;

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

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhoto() {
        return photo;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getId() {
        return Id;
    }
}
