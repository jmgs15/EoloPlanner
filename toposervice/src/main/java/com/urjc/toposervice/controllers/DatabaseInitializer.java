package com.urjc.toposervice.controllers;

import com.urjc.toposervice.entities.CityLandscape;
import com.urjc.toposervice.repositories.CityLandscapeRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;

@Component
public class DatabaseInitializer {

    private CityLandscapeRepository cityLandscapeRepository;

    public DatabaseInitializer(CityLandscapeRepository cityLandscapeRepository) {
        this.cityLandscapeRepository = cityLandscapeRepository;
    }

    @PostConstruct
    public void initializeDatabase() {

        Flux<CityLandscape> cities = Flux.just(
                new CityLandscape("Madrid", "flat"),
                new CityLandscape("Leon", "mountain"),
                new CityLandscape("Alicante", "mountain"),
                new CityLandscape("Asturias", "mountain"),
                new CityLandscape("Almeria", "flat"));

        cities
                .flatMap(this.cityLandscapeRepository::save)
                .blockLast();
    }
}
