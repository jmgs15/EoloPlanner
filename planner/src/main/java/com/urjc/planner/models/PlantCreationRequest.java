package com.urjc.planner.models;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlantCreationRequest {
    //{ "id": 1, "city": "Madrid" }
    Long id;
    String city;
}
