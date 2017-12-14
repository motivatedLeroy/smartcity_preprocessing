package de.ines.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.ines.Helpers.RouteProcessingHelper;
import de.ines.entities.GpsPoint;
import de.ines.entities.Route;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

import static java.lang.Math.*;

@Service
public class RoutePreprocessingService {


    public void preprocessingQueueMessage(ArrayList<Integer> message){
        System.out.println(message);
    }












}


