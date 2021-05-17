package com.store.okidosmobileapp.Model;

public class FeedBack {
    private String email;
    private String name;
    private String feedback;

    public FeedBack() {
    }

    public FeedBack(String email, String name, String feedback) {
        this.email = email;
        this.name = name;
        this.feedback = feedback;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
