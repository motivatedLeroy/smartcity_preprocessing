package de.ines.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.ines.entities.GpsPoint;
import de.ines.entities.Route;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.*;

@Service
public class RoutePreprocessingService {



    public void preprocessingQueueMessage(ArrayList<Integer> message){
        System.out.println(message);
    }

    public GpsPoint[] staypointDetection(String jsonRoute) throws IOException {

        ArrayList<GpsPoint> staypoints = new ArrayList<GpsPoint>();
        ObjectMapper mapper = new ObjectMapper();
        Route route = null;
        route = mapper.readValue(jsonRoute, Route.class);
        GpsPoint[] points = route.route;

        int distThreshold = 50;
        int timeThreshold = 1800;

        int i = 0;
        while(i <points.length) {
            int j = i + 1;
            GpsPoint p_i = points[i];
            while (j < points.length){
                GpsPoint p_j = points[j];
                double distance = this.directDistance(p_i.getLatitude(), p_i.getLongitude(), p_j.getLatitude(), p_j.getLongitude());
                if (distance > distThreshold) {  // Found the region
                    long delta_time = p_j.date - p_i.date;
                    if (delta_time > timeThreshold) {
                        staypoints.add(points[j]);
                    }
                    i = j;
                    break;
                }
                else
                    j = j + 1;
            }

        }
        return (GpsPoint[])staypoints.toArray();
    }


    public GpsPoint[] outlierDeletion(String jsonRoute) throws  IOException{
        ObjectMapper mapper = new ObjectMapper();
        Route route = null;
        route = mapper.readValue(jsonRoute, Route.class);
        GpsPoint[] points = route.route;
        ArrayList<GpsPoint> resultingRoute = new ArrayList<>(Arrays.asList(points));

        int distThreshold = 50;

        for(int i = 0; i < resultingRoute.size()-1; i++){
            int j = i+1;
            GpsPoint p_i = points[i];
            GpsPoint p_j = points[j];
            double distance = this.directDistance(p_i.getLatitude(), p_i.getLongitude(), p_j.getLatitude(), p_j.getLongitude());
            if( distance > distThreshold){
                resultingRoute.remove(j);
                i--;
            }
        }
        return (GpsPoint[])resultingRoute.toArray();
    }

    public GpsPoint[] smoothRoute(String jsonRoute) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Route route = null;
        route = mapper.readValue(jsonRoute, Route.class);
        ArrayList<GpsPoint> resultingRoute = new ArrayList<>(Arrays.asList(route.route));
        for (int i = 1; i < (resultingRoute.size() - 1); i++) {
            GpsPoint point = resultingRoute.get(i);
            GpsPoint predecessor = resultingRoute.get(i - 1);
            GpsPoint successor = resultingRoute.get(i + 1);
            point.setLatitude((float) ((point.getLatitude() * 0.6) + (predecessor.getLatitude() * 0.2)
                    + (successor.getLatitude() * 0.2)));
            point.setLongitude((float) ((point.getLongitude() * 0.6) + (predecessor.getLongitude() * 0.2)
                    + (successor.getLongitude() * 0.2)));
        }
        return (GpsPoint[])resultingRoute.toArray();
    }







    public double ToRadians(double degrees){
        double radians = degrees * Math.PI / 180;
        return radians;
    }

    double directDistance(double lat1, double lng1, double lat2, double lng2){
        double earthRadius = 3958.75;
        double dLat = ToRadians(lat2-lat1);
        double dLng = ToRadians(lng2-lng1);
        double a = sin(dLat/2) * sin(dLat/2) +
                cos(ToRadians(lat1)) * cos(ToRadians(lat2)) *
                        sin(dLng/2) * sin(dLng/2);
        double c = 2 * atan2(sqrt(a), sqrt(1-a));
        double dist = earthRadius * c;
        double meterConversion = 1609.00;
        return dist * meterConversion;
    }



}


