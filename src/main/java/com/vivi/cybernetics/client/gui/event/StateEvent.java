package com.vivi.cybernetics.client.gui.event;

import com.vivi.cybernetics.client.gui.CyberwareScreen;
import com.vivi.cybernetics.client.gui.event.GuiEvent;

public class StateEvent extends GuiEvent {
    private final CyberwareScreen.State state;
    public StateEvent(CyberwareScreen.State state) {
        this.state = state;
    }

    public CyberwareScreen.State getState() {
        return state;
    }
}
