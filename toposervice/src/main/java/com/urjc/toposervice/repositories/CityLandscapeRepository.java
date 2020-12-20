package com.urjc.toposervice.repositories;

import com.urjc.toposervice.entities.CityLandscape;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CityLandscapeRepository extends ReactiveMongoRepository<CityLandscape, String> {

    Mono<CityLandscape> findByCity(String city);
}
