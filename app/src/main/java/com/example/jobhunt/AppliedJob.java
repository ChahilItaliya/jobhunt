package com.example.jobhunt;

public class AppliedJob {
    private String jobId;
    private String jobTitle;
    private String companyName;

    // Constructor
    public AppliedJob(String jobId, String jobTitle, String companyName) {
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
    }

    // Getters and Setters
    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
