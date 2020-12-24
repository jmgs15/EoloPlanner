package com.urjc.planner.queues;

import com.google.gson.Gson;
import com.urjc.planner.WeatherServiceOuterClass;
import com.urjc.planner.dtos.GetTopographicInfoRequest;
import com.urjc.planner.dtos.GetTopographicInfoResponse;
import com.urjc.planner.models.PlantCreationNotification;
import com.urjc.planner.models.PlantCreationRequest;
import com.urjc.planner.models.State;
import com.urjc.planner.services.TopoServiceClient;
import com.urjc.planner.services.WeatherServiceClient;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class EoloplantCreationRequestsConsumer {

    private final TopoServiceClient topoServiceClient;
    private final WeatherServiceClient weatherServiceClient;
    private final EoloplantCreationProgressNotificationsProducer eoloplantProducer;

    public EoloplantCreationRequestsConsumer(TopoServiceClient topoServiceClient, WeatherServiceClient weatherServiceClient, EoloplantCreationProgressNotificationsProducer eoloplantProducer) {
        this.topoServiceClient = topoServiceClient;
        this.weatherServiceClient = weatherServiceClient;
        this.eoloplantProducer = eoloplantProducer;
    }

    @RabbitListener(queues = "eoloplantCreationRequests", ackMode = "AUTO")
    public void received(String message) {

        System.out.println("Message: "+ message);

        Gson gson = new Gson();
        PlantCreationRequest request = gson.fromJson(message, PlantCreationRequest.class);

        PlantCreationNotification plantCreation = PlantCreationNotification.builder()
                .id(request.getId())
                .city(request.getCity())
                .state(State.INITIAL)
                .planning("")
                .completed(false)
                .build();

        CompletableFuture<GetTopographicInfoResponse> topography = topoServiceClient.getTopography(new GetTopographicInfoRequest(plantCreation.getCity()));
        WeatherServiceOuterClass.GetWeatherRequest weatherRequest = WeatherServiceOuterClass.GetWeatherRequest.newBuilder().setCity(plantCreation.getCity()).build();
        CompletableFuture<WeatherServiceOuterClass.Weather> weather = weatherServiceClient.getWeather(weatherRequest);

        eoloplantProducer.sendStateChange(plantCreation, plantCreation.getCity());

        weather.whenCompleteAsync((response, error) -> {
            eoloplantProducer.sendStateChange(plantCreation, response.getWeather());
        });

        topography.whenCompleteAsync((response, error) -> {
            eoloplantProducer.sendStateChange(plantCreation, response.getLandscape());
        });
    }
}
