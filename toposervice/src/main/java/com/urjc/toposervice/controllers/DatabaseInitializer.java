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
                new CityLandscape("madrid", "flat"),
                new CityLandscape("leon", "mountain"),
                new CityLandscape("alicante", "mountain"),
                new CityLandscape("asturias", "mountain"),
                new CityLandscape("almeria", "flat"),
                new CityLandscape("zaragoza", "flat"));

        cities
                .flatMap(this.cityLandscapeRepository::save)
                .blockLast();
    }
}
