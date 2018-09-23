
package com.hackathon.codechefapp.model.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubmissionStats {

    @SerializedName("partiallySolvedProblems")
    @Expose
    private Integer partiallySolvedProblems;
    @SerializedName("solvedProblems")
    @Expose
    private Integer solvedProblems;
    @SerializedName("attemptedProblems")
    @Expose
    private Integer attemptedProblems;
    @SerializedName("submittedSolutions")
    @Expose
    private Integer submittedSolutions;
    @SerializedName("wrongSubmissions")
    @Expose
    private Integer wrongSubmissions;
    @SerializedName("runTimeError")
    @Expose
    private Integer runTimeError;
    @SerializedName("timeLimitExceed")
    @Expose
    private Integer timeLimitExceed;
    @SerializedName("compilationError")
    @Expose
    private Integer compilationError;
    @SerializedName("partiallySolvedSubmissions")
    @Expose
    private Integer partiallySolvedSubmissions;
    @SerializedName("acceptedSubmissions")
    @Expose
    private Integer acceptedSubmissions;

    public Integer getPartiallySolvedProblems() {
        return partiallySolvedProblems;
    }

    public void setPartiallySolvedProblems(Integer partiallySolvedProblems) {
        this.partiallySolvedProblems = partiallySolvedProblems;
    }

    public Integer getSolvedProblems() {
        return solvedProblems;
    }

    public void setSolvedProblems(Integer solvedProblems) {
        this.solvedProblems = solvedProblems;
    }

    public Integer getAttemptedProblems() {
        return attemptedProblems;
    }

    public void setAttemptedProblems(Integer attemptedProblems) {
        this.attemptedProblems = attemptedProblems;
    }

    public Integer getSubmittedSolutions() {
        return submittedSolutions;
    }

    public void setSubmittedSolutions(Integer submittedSolutions) {
        this.submittedSolutions = submittedSolutions;
    }

    public Integer getWrongSubmissions() {
        return wrongSubmissions;
    }

    public void setWrongSubmissions(Integer wrongSubmissions) {
        this.wrongSubmissions = wrongSubmissions;
    }

    public Integer getRunTimeError() {
        return runTimeError;
    }

    public void setRunTimeError(Integer runTimeError) {
        this.runTimeError = runTimeError;
    }

    public Integer getTimeLimitExceed() {
        return timeLimitExceed;
    }

    public void setTimeLimitExceed(Integer timeLimitExceed) {
        this.timeLimitExceed = timeLimitExceed;
    }

    public Integer getCompilationError() {
        return compilationError;
    }

    public void setCompilationError(Integer compilationError) {
        this.compilationError = compilationError;
    }

    public Integer getPartiallySolvedSubmissions() {
        return partiallySolvedSubmissions;
    }

    public void setPartiallySolvedSubmissions(Integer partiallySolvedSubmissions) {
        this.partiallySolvedSubmissions = partiallySolvedSubmissions;
    }

    public Integer getAcceptedSubmissions() {
        return acceptedSubmissions;
    }

    public void setAcceptedSubmissions(Integer acceptedSubmissions) {
        this.acceptedSubmissions = acceptedSubmissions;
    }

}
