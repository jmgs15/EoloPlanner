package com.urjc.planner;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import dtos.GetTopographicInfoRequest;
import dtos.GetTopographicInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import services.TopoServiceClient;
import services.WeatherServiceClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class CommandRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(CommandRunner.class);

    private final TopoServiceClient topoServiceClient;
    private final WeatherServiceClient weatherServiceClient;

    public CommandRunner(TopoServiceClient topoServiceClient, WeatherServiceClient weatherServiceClient) {
        this.topoServiceClient = topoServiceClient;
        this.weatherServiceClient = weatherServiceClient;
    }

    @Override
    public void run(String... args) throws Exception {
        // Kick of multiple, asynchronous lookups
        CompletableFuture<GetTopographicInfoResponse> topography = topoServiceClient.getTopography(new GetTopographicInfoRequest("Leon"));
        ListenableFuture<WeatherServiceOuterClass.Weather> weather = weatherServiceClient.getWeather(WeatherServiceOuterClass.GetWeatherRequest.newBuilder().setCity("Leon").build());

        // Wait until they are all done
        //CompletableFuture.allOf(topography).join();
        Futures.addCallback(weather, new FutureCallback<WeatherServiceOuterClass.Weather>() {
            @Override
            public void onSuccess(WeatherServiceOuterClass.Weather result) {
                logger.info("Result weather: {}", result.getWeather());
            }

            @Override
            public void onFailure(Throwable t) {
                // Should never reach here.
                //throw new Error(t);
                logger.info("Result topography: {}");
            }
        }, MoreExecutors.directExecutor());

        topography.whenCompleteAsync((response, error) -> logger.info("Result topography: {}", response.getLandscape()));
    }

}