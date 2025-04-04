package org.anse.app.process;

import org.anse.app.schema.DeliveryEvent;
import org.anse.app.schema.ModelInput;
import org.apache.flink.api.common.state.*;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;

import org.apache.flink.streaming.api.functions.co.KeyedBroadcastProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.api.common.typeinfo.Types;

import java.util.ArrayList;

public class ComputeChaseState extends KeyedBroadcastProcessFunction<String, DeliveryEvent, Tuple2<String, Integer>, ModelInput> {
    private transient ValueState<Integer> scoreState;
    private transient ValueState<Integer> wicketsState;
    private transient ValueState<Integer> ballCountState;
    private transient ListState<Tuple2<Integer, Integer>> last12BallsState;

    @Override
    public void open(Configuration parameters) {
        scoreState = getRuntimeContext().getState(new ValueStateDescriptor<>("scoreState", Types.INT));
        wicketsState = getRuntimeContext().getState(new ValueStateDescriptor<>("wicketsState", Types.INT));
        ballCountState = getRuntimeContext().getState(new ValueStateDescriptor<>("ballCountState", Types.INT));
        last12BallsState = getRuntimeContext().getListState(new ListStateDescriptor<>("last12BallsState", Types.TUPLE(Types.INT, Types.INT)));
    }


    @Override
    public void processElement(DeliveryEvent value, ReadOnlyContext ctx, Collector<ModelInput> out) throws Exception {
        if (value.getExtraType() != null && value.getExtraType().equals("match_start")) {
           clearState();
        }
        String matchId = value.getMatchId();
        int inning = value.getInning();
        ReadOnlyBroadcastState<String, Integer> broadcastState = ctx.getBroadcastState(new MapStateDescriptor<>("first_innings_target", Types.STRING, Types.INT));
        Integer targetScore = broadcastState.get(matchId);

        if (targetScore == null) {
            targetScore = 200; // default target score
        }

        int currentScore = scoreState.value() != null ? scoreState.value() : 0;
        int wicketsDown = wicketsState.value() != null ? wicketsState.value() : 0;
        int ballCount = ballCountState.value() != null ? ballCountState.value() : 0;

        int runThisBall = value.getTotalRun();
        int isWicket = (value.getDismissalKind() != null && !value.getDismissalKind().isEmpty()) ? 1 : 0;

        currentScore += runThisBall;
        wicketsDown += isWicket;

        // No ball or wide does not count as a ball
        if (value.getExtraType() == null || value.getExtraType().isEmpty()) {
            ballCount++;
        } else {
            if (!value.getExtraType().equals("noballs") && !value.getExtraType().equals("wides")) {
                ballCount++;
            }
        }

        scoreState.update(currentScore);
        wicketsState.update(wicketsDown);
        ballCountState.update(ballCount);

        // Compute the chase state
        int ballsRemaining = 120 - ballCount; // assuming 20 overs (120 balls) per innings

        ModelInput chaseState = new ModelInput();
        chaseState.setMatchId(matchId);
        chaseState.setInning(inning);
        chaseState.setTargetRuns(targetScore);
        chaseState.setCurrentScore(currentScore);
        chaseState.setWicketsDown(wicketsDown);
        chaseState.setBallsRemaining(ballsRemaining);

        // Retrieve the current state of the last 12 balls
        ArrayList<Tuple2<Integer, Integer>> deliveries = new ArrayList<>();
        for (Tuple2<Integer, Integer> delivery : last12BallsState.get()) {
            deliveries.add(delivery);
        }

        // Add the new delivery information
        deliveries.add(Tuple2.of(runThisBall, isWicket));

        // Ensure the list size does not exceed 12
        if (deliveries.size() > 12) {
            deliveries.remove(0);
        }

        // Update the state with the new list
        last12BallsState.update(deliveries);

        // Compute the total runs and wickets in the last 12 balls
        int runsLast12Balls = 0;
        int wicketsLast12Balls = 0;
        for (Tuple2<Integer, Integer> delivery : deliveries) {
            runsLast12Balls += delivery.f0;
            wicketsLast12Balls += delivery.f1;
        }

        // Set the computed values in the chase state
        chaseState.setRunsLast12Balls(runsLast12Balls);
        chaseState.setWicketsLast12Balls(wicketsLast12Balls);


        // Convert chaseState to JSON string
        //        ObjectMapper objectMapper = new ObjectMapper();
        //        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        //        String chaseStateJson = objectMapper.writeValueAsString(chaseState);

        out.collect(chaseState);
    }

    @Override
    public void processBroadcastElement(Tuple2<String, Integer> value, Context ctx, Collector<ModelInput> out) throws Exception {
        ctx.getBroadcastState(new MapStateDescriptor<>("first_innings_target", Types.STRING, Types.INT)).put(value.f0, value.f1);
    }

    public void clearState() {
        scoreState.clear();
        wicketsState.clear();
        ballCountState.clear();
        last12BallsState.clear();
    }
}