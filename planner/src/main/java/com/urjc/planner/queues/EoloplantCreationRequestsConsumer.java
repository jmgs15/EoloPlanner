package com.urjc.planner.queues;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class EoloplantCreationRequestsConsumer {

    @RabbitListener(queues = "eoloplantCreationRequests", ackMode = "AUTO")
    public void received(String message) {

        System.out.println("Message: "+ message);
    }
}
