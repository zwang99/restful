package com.example.shop_project_detail.service;

import com.example.shop_project_detail.domain.response.MostPurchasedResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
public class ScheduleEmailService {
    private OrderService orderService;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public ScheduleEmailService(OrderService orderService, RabbitTemplate rabbitTemplate) {
        this.orderService = orderService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Scheduled(cron = "0 13 16 * * ?") // run at 4:13 PM every day
    public void voidPublishEmail(){
        List<MostPurchasedResponse> mostPurchasedResponses = orderService.getMostRecentlyPurchased(1);

        // send the message to the emailExchange exchange with a routing key of "emailKey"

        rabbitTemplate.convertAndSend("emailExchange", "emailKey", mostPurchasedResponses);
        // log the message to the console
        System.out.println("Scheduled email message sent at " + LocalTime.now());
    }
}
