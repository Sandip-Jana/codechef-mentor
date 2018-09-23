
package com.hackathon.codechefapp.model.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rankings {

    @SerializedName("allContestRanking")
    @Expose
    private AllContestRanking allContestRanking;
    @SerializedName("longRanking")
    @Expose
    private LongRanking longRanking;
    @SerializedName("shortRanking")
    @Expose
    private ShortRanking shortRanking;
    @SerializedName("ltimeRanking")
    @Expose
    private LtimeRanking ltimeRanking;
    @SerializedName("allSchoolRanking")
    @Expose
    private AllSchoolRanking allSchoolRanking;
    @SerializedName("longSchoolRanking")
    @Expose
    private LongSchoolRanking longSchoolRanking;
    @SerializedName("shortSchoolRanking")
    @Expose
    private ShortSchoolRanking shortSchoolRanking;
    @SerializedName("ltimeSchoolRanking")
    @Expose
    private LtimeSchoolRanking ltimeSchoolRanking;

    public AllContestRanking getAllContestRanking() {
        return allContestRanking;
    }

    public void setAllContestRanking(AllContestRanking allContestRanking) {
        this.allContestRanking = allContestRanking;
    }

    public LongRanking getLongRanking() {
        return longRanking;
    }

    public void setLongRanking(LongRanking longRanking) {
        this.longRanking = longRanking;
    }

    public ShortRanking getShortRanking() {
        return shortRanking;
    }

    public void setShortRanking(ShortRanking shortRanking) {
        this.shortRanking = shortRanking;
    }

    public LtimeRanking getLtimeRanking() {
        return ltimeRanking;
    }

    public void setLtimeRanking(LtimeRanking ltimeRanking) {
        this.ltimeRanking = ltimeRanking;
    }

    public AllSchoolRanking getAllSchoolRanking() {
        return allSchoolRanking;
    }

    public void setAllSchoolRanking(AllSchoolRanking allSchoolRanking) {
        this.allSchoolRanking = allSchoolRanking;
    }

    public LongSchoolRanking getLongSchoolRanking() {
        return longSchoolRanking;
    }

    public void setLongSchoolRanking(LongSchoolRanking longSchoolRanking) {
        this.longSchoolRanking = longSchoolRanking;
    }

    public ShortSchoolRanking getShortSchoolRanking() {
        return shortSchoolRanking;
    }

    public void setShortSchoolRanking(ShortSchoolRanking shortSchoolRanking) {
        this.shortSchoolRanking = shortSchoolRanking;
    }

    public LtimeSchoolRanking getLtimeSchoolRanking() {
        return ltimeSchoolRanking;
    }

    public void setLtimeSchoolRanking(LtimeSchoolRanking ltimeSchoolRanking) {
        this.ltimeSchoolRanking = ltimeSchoolRanking;
    }

}
