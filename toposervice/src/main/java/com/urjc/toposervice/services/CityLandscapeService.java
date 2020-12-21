package com.urjc.toposervice.services;

import com.urjc.toposervice.entities.CityLandscape;
import com.urjc.toposervice.repositories.CityLandscapeRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class CityLandscapeService {

    private CityLandscapeRepository cityLandscapeRepository;

    public CityLandscapeService(CityLandscapeRepository cityLandscapeRepository) {
        this.cityLandscapeRepository = cityLandscapeRepository;
    }

    public Mono<CityLandscape> createCityLandscape(CityLandscape city) {
        return this.cityLandscapeRepository.save(city);
    }

    public Mono<CityLandscape> getCityLandscape(String city) {
        int randomSeconds = new Random().nextInt(3) + 1;
        System.out.println(randomSeconds);
        return this.cityLandscapeRepository.findById(city)
                .delayElement(Duration.ofSeconds(randomSeconds))
                .switchIfEmpty(
                        Mono.error(new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "City with name " + city + " not found.")));

    }
}
