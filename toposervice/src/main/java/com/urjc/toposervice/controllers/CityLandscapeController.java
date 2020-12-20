package com.urjc.toposervice.controllers;

import com.urjc.toposervice.entities.CityLandscape;
import com.urjc.toposervice.services.CityLandscapeService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/cities")
public class CityLandscapeController {

    private CityLandscapeService cityLandscapeService;

    public CityLandscapeController(CityLandscapeService cityLandscapeService) {
        this.cityLandscapeService = cityLandscapeService;
    }

    @GetMapping("/{cityName}")
    public Mono<CityLandscape> getCityLandscape(@PathVariable String cityName) {
        return cityLandscapeService.getCityLandscape(cityName);
    }

    @PostMapping
    public Flux<CityLandscape> createCityLandscapes(@RequestBody Flux<CityLandscape> cities) {
        return cities.flatMap(cityLandscapeService::createCityLandscape);
    }
}
