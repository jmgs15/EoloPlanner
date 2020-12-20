package com.urjc.toposervice.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document
public class CityLandscape {

    @Id
    private String id;
    private String city;
    private String landscape;

}
