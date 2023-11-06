package com.vivi.cybernetics.client.gui.event;

import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class StateEvent extends Event {

    private static final int NO_ENTITY_POS = -999;
    private final Screen screen;
    private final State state;
    private final int entityX;
    private final int entityY;
    public StateEvent(Screen screen, State state) {
        this(screen, state, NO_ENTITY_POS, NO_ENTITY_POS);
    }
    public StateEvent(Screen screen, State state, int entityX, int entityY) {
        this.screen = screen;
        this.state = state;
        this.entityX = entityX;
        this.entityY = entityY;
    }

    public Screen getScreen() {
        return screen;
    }

    public State getState() {
        return state;
    }

    public int getEntityX() {
        return entityX;
    }

    public int getEntityY() {
        return entityY;
    }

    public boolean hasEntityPosData() {
        return entityX != NO_ENTITY_POS && entityY != NO_ENTITY_POS;
    }


    public enum State {
        MAIN {
            @Override
            public State getNextState() {
                return TRANSITION_SUB;
            }

            @Override
            public String toString() {
                return "main";
            }
        },
        TRANSITION_SUB {
            @Override
            public State getNextState() {
                return SUB;
            }
            @Override
            public String toString() {
                return "transition_sub";
            }
        },
        SUB {
            @Override
            public State getNextState() {
                return TRANSITION_MAIN;
            }
            @Override
            public String toString() {
                return "sub";
            }
        },
        TRANSITION_MAIN {
            @Override
            public State getNextState() {
                return MAIN;
            }
            @Override
            public String toString() {
                return "transition_main";
            }
        };


        public abstract State getNextState();
    }
}
