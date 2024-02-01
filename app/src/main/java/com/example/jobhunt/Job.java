package com.example.jobhunt;

public class Job {
    private String title;
    private String description;
    private String documentId;


    public Job() {
        // Default constructor required for Firebase
    }

    public Job(String title, String description,String documentId) {
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
