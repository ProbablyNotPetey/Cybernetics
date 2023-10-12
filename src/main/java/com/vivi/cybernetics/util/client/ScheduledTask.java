package com.vivi.cybernetics.util.client;

public record ScheduledTask(long startTime, long endTime, Runnable task, boolean continuous) {

}
