package com.example.jobhunt;

public class Recommend_job {
    private String companyName;
    private String jobTitle;
    private String img;

    public Recommend_job(String companyName, String jobTitle, String img) {
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.img = img;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getImg() {
        return img;
    }
}
