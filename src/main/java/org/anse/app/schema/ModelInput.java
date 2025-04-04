package org.anse.app.schema;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO class for ModelEvent
 */

public class ModelInput {
    @JsonProperty("match_id")
    private String matchId;
    @JsonProperty("inning")
    private int inning;
    @JsonProperty("season")
    private String season;
    @JsonProperty("match_type")
    private String matchType;
    @JsonProperty("batting_team")
    private String battingTeam;
    @JsonProperty("toss_winner")
    private String tossWinner;
    @JsonProperty("city")
    private String city;
    @JsonProperty("target_runs")
    private int targetRuns;
    @JsonProperty("current_score")
    private int currentScore;
    @JsonProperty("wickets_down")
    private int wicketsDown;
    @JsonProperty("balls_remaining")
    private int ballsRemaining;
    @JsonProperty("runs_last_12_balls")
    private int runsLast12Balls;
    @JsonProperty("wickets_last_12_balls")
    private int wicketsLast12Balls;

    // Getters and setters

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public int getInning() {
        return inning;
    }

    public void setInning(int inning) {
        this.inning = inning;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public String getBattingTeam() {
        return battingTeam;
    }

    public void setBattingTeam(String battingTeam) {
        this.battingTeam = battingTeam;
    }

    public String getTossWinner() {
        return tossWinner;
    }

    public void setTossWinner(String tossWinner) {
        this.tossWinner = tossWinner;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getTargetRuns() {
        return targetRuns;
    }

    public void setTargetRuns(int targetRuns) {
        this.targetRuns = targetRuns;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public int getWicketsDown() {
        return wicketsDown;
    }

    public void setWicketsDown(int wicketsDown) {
        this.wicketsDown = wicketsDown;
    }

    public int getBallsRemaining() {
        return ballsRemaining;
    }

    public void setBallsRemaining(int ballsRemaining) {
        this.ballsRemaining = ballsRemaining;
    }

    public int getRunsLast12Balls() {
        return runsLast12Balls;
    }

    public void setRunsLast12Balls(int runsLast12Balls) {
        this.runsLast12Balls = runsLast12Balls;
    }

    public int getWicketsLast12Balls() {
        return wicketsLast12Balls;
    }

    public void setWicketsLast12Balls(int wicketsLast12Balls) {
        this.wicketsLast12Balls = wicketsLast12Balls;
    }

    @Override
    public String toString() {
        return "ModelInput{" +
                "matchId='" + matchId + '\'' +
                ", inning=" + inning +
                ", season='" + season + '\'' +
                ", matchType='" + matchType + '\'' +
                ", battingTeam='" + battingTeam + '\'' +
                ", tossWinner='" + tossWinner + '\'' +
                ", city='" + city + '\'' +
                ", targetRuns=" + targetRuns +
                ", currentScore=" + currentScore +
                ", wicketsDown=" + wicketsDown +
                ", ballsRemaining=" + ballsRemaining +
                ", runsLast12Balls=" + runsLast12Balls +
                ", wicketsLast12Balls=" + wicketsLast12Balls +
                '}';
    }
}