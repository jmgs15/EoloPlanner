package com.urjc.planner.models;

public enum State {
    INITIAL(0),
    SERVICES_CALLED(25),
    ONE_RESPONSE_RECEIVED(50),
    BOTH_RESPONSES_RECEIVED(75),
    PLANTE_CREATED(100);

    int progress;

    State(int progress) {
        this.progress = progress;
    }
}
