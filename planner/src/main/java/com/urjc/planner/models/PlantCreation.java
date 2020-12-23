package com.urjc.planner.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlantCreation {
    //{ "id": 1, "city": "Madrid", "progress": 100, "completed": true, "planning": "madrid-sunny-flat" }
    String planning;
    Long id;
    String city;
    State state;
    boolean completed;
}
