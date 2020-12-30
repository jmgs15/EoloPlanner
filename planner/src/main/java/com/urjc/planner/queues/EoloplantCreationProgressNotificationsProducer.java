package com.urjc.planner.queues;

import com.urjc.planner.models.PlantCreationNotification;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class EoloplantCreationProgressNotificationsProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(PlantCreationNotification plantCreation) {

        String data = plantCreation.toJsonString();

        System.out.println("publishToQueue: '" + data + "'");

        rabbitTemplate.convertAndSend("eoloplantCreationProgressNotifications", data);
    }

    //■ 25% cuando las peticiones a los servicios se hayan enviado
    //■ 50% cuando llegue la respuesta al primer servicio
    //■ 75% cuando llegue la respuesta al segundo
    //■ 100% cuando se haya creado la planificación (concatenando las cadenas)

    //Crear objeto de salida con booleanos de finalizacion de los dos service
    public synchronized void sendStateChange(PlantCreationNotification plantCreation, String message) {
        plantCreation.nextState();
        plantCreation.addResponse(message);

        sendMessage(plantCreation);

        if (plantCreation.isBothResponsesReceivedState()) {
            //{ "id": 1, "city": "Madrid", "progress": 100, "completed": true, "planning": "madrid-sunny-flat" }
            plantCreation.finish();
            sleep();
            sendMessage(plantCreation);
        }
    }

    private void sleep() {
        try {
            int randomMilliSeconds = (new Random().nextInt(3) + 1) * 1000;
            Thread.sleep(randomMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
