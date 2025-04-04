package org.anse.app.process;

import org.anse.app.schema.DeliveryEvent;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;


import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;

import org.apache.flink.api.common.typeinfo.Types;

public class ComputeFirstInnings extends ProcessWindowFunction<DeliveryEvent, Tuple2<String, Integer>, String, TimeWindow> {
    private transient ValueState<Integer> totalRunsState;

    @Override
    public void open(Configuration parameters) throws Exception {
        totalRunsState = getRuntimeContext().getState(new ValueStateDescriptor<>("totalRunsState", Types.INT));
    }

    @Override
    public void process(String key, Context context, Iterable<DeliveryEvent> elements, Collector<Tuple2<String, Integer>> out) throws Exception {
        int totalRuns = totalRunsState.value() != null ? totalRunsState.value() : 0;
        int totalWickets = 0;
        int ballCount = 0;

        for (DeliveryEvent event : elements) {
            if (event.getExtraType() != null && event.getExtraType().equals("match_start")) {
                clearState();
            }
            totalRuns += event.getTotalRun();
            totalWickets += (event.getDismissalKind() != null && !event.getDismissalKind().isEmpty()) ? 1 : 0;
            ballCount++;
        }

        totalRunsState.update(totalRuns);

        int targetScore = totalRuns + 1;
        out.collect(new Tuple2<>(key, targetScore));
    }

    public void clearState() throws Exception {
        totalRunsState.clear();
    }
}