package de.ines.Services;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class GpsPointPreprocessingService {



    public void preprocessingQueueMessage(ArrayList<Integer> message){
        System.out.println(message);
    }
}
