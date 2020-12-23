package com.urjc.planner.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.*;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlantCreation {
    //{ "id": 1, "city": "Madrid", "progress": 100, "completed": true, "planning": "madrid-sunny-flat" }
    Long id;
    String city;
    State state;
    String planning;
    boolean completed;

    public void nextState() {
        this.state = State.values()[this.state.ordinal() + 1];
    }

    public void addResponse(String response) {
        if (planning.equals("")) {
            this.planning = response;
        } else {
            this.planning += "-" + response;
        }
    }

    public String toJsonString() {
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .create();
        Map<String, Object> response = new LinkedHashMap<>();

        response.put("id", this.id);
        response.put("city", this.city);
        response.put("progress", this.state.getProgress());
        response.put("completed", this.completed);
        this.planning = this.isFirstLetterLowerThanOrEqualsToM() ? this.planning.toLowerCase() : this.planning.toUpperCase();
        response.put("planning", this.completed ? this.planning : null);

        return gson.toJson(response);
    }

    private boolean isFirstLetterLowerThanOrEqualsToM() {
        assert this.city != null;
        char cityFirstLetter = this.city.toLowerCase().charAt(0);
        return cityFirstLetter <= 'm';
    }

    public boolean isBothResponsesReceivedState() {
        return this.state.equals(State.BOTH_RESPONSES_RECEIVED);
    }

    public void finish() {
        this.nextState();
        this.completed = true;
    }
    
}
