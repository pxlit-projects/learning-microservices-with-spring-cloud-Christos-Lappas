package be.pxl.services;

import be.pxl.services.client.LogbookClient;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class QueueService {
    private final LogbookClient logbookClient;

    public QueueService(LogbookClient logbookClient) {
        this.logbookClient = logbookClient;
    }
    @RabbitListener(queues = "myQueue")
    public void listen(String in) {
        System.out.println("Message read from myQueue : " + in);

        logbookClient.addLog(in);
    }

}
