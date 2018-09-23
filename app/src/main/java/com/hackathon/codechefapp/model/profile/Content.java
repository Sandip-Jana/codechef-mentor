
package com.hackathon.codechefapp.model.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Content {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("fullname")
    @Expose
    private String fullname;
    @SerializedName("country")
    @Expose
    private Country country;
    @SerializedName("state")
    @Expose
    private State state;
    @SerializedName("city")
    @Expose
    private City city;
    @SerializedName("rankings")
    @Expose
    private Rankings rankings;
    @SerializedName("ratings")
    @Expose
    private Ratings ratings;
    @SerializedName("occupation")
    @Expose
    private String occupation;
    @SerializedName("organization")
    @Expose
    private String organization;
    @SerializedName("problemStats")
    @Expose
    private ProblemStats problemStats;
    @SerializedName("submissionStats")
    @Expose
    private SubmissionStats submissionStats;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("band")
    @Expose
    private String band;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Rankings getRankings() {
        return rankings;
    }

    public void setRankings(Rankings rankings) {
        this.rankings = rankings;
    }

    public Ratings getRatings() {
        return ratings;
    }

    public void setRatings(Ratings ratings) {
        this.ratings = ratings;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public ProblemStats getProblemStats() {
        return problemStats;
    }

    public void setProblemStats(ProblemStats problemStats) {
        this.problemStats = problemStats;
    }

    public SubmissionStats getSubmissionStats() {
        return submissionStats;
    }

    public void setSubmissionStats(SubmissionStats submissionStats) {
        this.submissionStats = submissionStats;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getBand() {
        return band;
    }

    public void setBand(String band) {
        this.band = band;
    }

}
