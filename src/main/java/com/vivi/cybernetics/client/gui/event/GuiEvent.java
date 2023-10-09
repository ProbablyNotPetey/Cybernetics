package com.vivi.cybernetics.client.gui.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuiEvent {
    private final List<Object> data = new ArrayList<>();

    public GuiEvent addData(Object... data) {
        this.data.addAll(Arrays.asList(data));
        return this;
    }
    public List<Object> getData() {
        return data;
    }
}
