package com.vivi.cybernetics.client.util;

public record ScheduledTask(long startTime, long endTime, Runnable task, boolean continuous) {

}
