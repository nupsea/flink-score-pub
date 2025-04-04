package org.anse.app.schema;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * POJO class for DeliveryEvent
 */

public class DeliveryEvent {
    @JsonProperty("inning")
    private int inning;
    @JsonProperty("team_name")
    private String teamName;
    @JsonProperty("over_num")
    private int overNum;
    @JsonProperty("batsman_name")
    private String batsmanName;
    @JsonProperty("bowler")
    private String bowler;
    @JsonProperty("non_striker")
    private String nonStriker;
    @JsonProperty("extra_type")
    private String extraType;
    @JsonProperty("extra_run")
    private int extraRun;
    @JsonProperty("total_run")
    private int totalRun;
    @JsonProperty("batsman_run")
    private int batsmanRun;
    @JsonProperty("dismissal_kind")
    private String dismissalKind;
    @JsonProperty("dismissed_player")
    private String dismissedPlayer;
    @JsonProperty("fielders_name")
    private String fieldersName;
    @JsonProperty("match_id")
    private String matchId;

    public int getInning() {
        return inning;
    }

    public void setInning(int inning) {
        this.inning = inning;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getOverNum() {
        return overNum;
    }

    public void setOverNum(int overNum) {
        this.overNum = overNum;
    }

    public String getBatsmanName() {
        return batsmanName;
    }

    public void setBatsmanName(String batsmanName) {
        this.batsmanName = batsmanName;
    }

    public String getBowler() {
        return bowler;
    }

    public void setBowler(String bowler) {
        this.bowler = bowler;
    }

    public String getNonStriker() {
        return nonStriker;
    }

    public void setNonStriker(String nonStriker) {
        this.nonStriker = nonStriker;
    }

    public String getExtraType() {
        return extraType;
    }

    public void setExtraType(String extraType) {
        this.extraType = extraType;
    }

    public int getExtraRun() {
        return extraRun;
    }

    public void setExtraRun(int extraRun) {
        this.extraRun = extraRun;
    }

    public int getTotalRun() {
        return totalRun;
    }

    public void setTotalRun(int totalRun) {
        this.totalRun = totalRun;
    }

    public int getBatsmanRun() {
        return batsmanRun;
    }

    public void setBatsmanRun(int batsmanRun) {
        this.batsmanRun = batsmanRun;
    }

    public String getDismissalKind() {
        return dismissalKind;
    }

    public void setDismissalKind(String dismissalKind) {
        this.dismissalKind = dismissalKind;
    }

    public String getDismissedPlayer() {
        return dismissedPlayer;
    }

    public void setDismissedPlayer(String dismissedPlayer) {
        this.dismissedPlayer = dismissedPlayer;
    }

    public String getFieldersName() {
        return fieldersName;
    }

    public void setFieldersName(String fieldersName) {
        this.fieldersName = fieldersName;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    @Override
    public String toString() {
        return "DeliveryEvent{" +
                "inning=" + inning +
                ", teamName='" + teamName + '\'' +
                ", overNum=" + overNum +
                ", batsmanName='" + batsmanName + '\'' +
                ", bowler='" + bowler + '\'' +
                ", nonStriker='" + nonStriker + '\'' +
                ", extraType='" + extraType + '\'' +
                ", extraRun=" + extraRun +
                ", totalRun=" + totalRun +
                ", batsmanRun=" + batsmanRun +
                ", dismissalKind='" + dismissalKind + '\'' +
                ", dismissedPlayer='" + dismissedPlayer + '\'' +
                ", fieldersName='" + fieldersName + '\'' +
                ", matchId='" + matchId + '\'' +
                '}';
    }
}