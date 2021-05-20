package com.hotmail.jamesnhendry.fza3077;

//This class serves as the data mode for the Recommendation object used throughout this platform.
public class Recommendation {

    private String body,description;

    public Recommendation() {

    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Recommendation(String body, String description) {
        this.body = body;
        this.description = description;
    }
}
