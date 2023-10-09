package com.vivi.cybernetics.util;

public record ScheduledTask(long startTime, long endTime, Runnable task, boolean continuous) {

}
