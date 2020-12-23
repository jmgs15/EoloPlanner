package com.urjc.planner.queues;


import com.urjc.planner.models.PlantCreation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EoloplantCreationProgressNotificationsProducer {

    private final RabbitTemplate rabbitTemplate;

    private int numData;

    public EoloplantCreationProgressNotificationsProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Scheduled(fixedRate = 1000)
    public void sendMessage(String message) {

        String data = "Data " + numData++;

        System.out.println("publishToQueue: '" + data + "'");

        rabbitTemplate.convertAndSend("eoloplantCreationProgressNotifications", data);
    }

    //■ 25% cuando las peticiones a los servicios se hayan enviado
    //■ 50% cuando llegue la respuesta al primer servicio
    //■ 75% cuando llegue la respuesta al segundo
    //■ 100% cuando se haya creado la planificación (concatenando las cadenas)

    //Crear objeto de salida con booleanos de finalizacion de los dos service
    public synchronized void sendStateChange(PlantCreation plant) {
        state.next();
        // La primera vez entra con 0 y se le asigna el sigueitne estado que sería el 25
        response.append(mensaje);

        sendMessage(mensaje);

        if(state.bothServicesRespond()) { // progress: 75
            state.next();
            //{ "id": 1, "city": "Madrid", "progress": 100, "completed": true, "planning": "madrid-sunny-flat" }
            sendMessage(mensaje); // progress: 100
        }
    }
}
