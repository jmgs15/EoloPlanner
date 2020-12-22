package services;

import com.google.common.util.concurrent.ListenableFuture;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class WeatherServiceClient {

    @GrpcClient("weatherServer")
    private WeatherServiceGrpc.WeatherServiceFutureStub client;

    public WeatherServiceOuterClass.Weather getWeather(WeatherServiceOuterClass.GetWeatherRequest request) {
        //ListenableFuture<WeatherServiceOuterClass.Weather> response = client.weather(request);
        return client.weather(request);
    }

}
