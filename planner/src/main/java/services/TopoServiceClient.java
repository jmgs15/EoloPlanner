package services;

import dtos.GetTopographicInfoRequest;
import dtos.GetTopographicInfoResponse;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
public class TopoServiceClient {

    private final RestTemplate restTemplate;

    public TopoServiceClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Async
    public CompletableFuture<GetTopographicInfoResponse> getTopography(GetTopographicInfoRequest request) {
        String url = String.format("localhost:8080/api/topographicdetails/%s", request.getCity());
        GetTopographicInfoResponse response = restTemplate.getForObject(url, GetTopographicInfoResponse.class);
        return CompletableFuture.completedFuture(response);
    }
}
