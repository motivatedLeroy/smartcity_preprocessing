package de.ines.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.ines.Helpers.RouteProcessingHelper;
import de.ines.entities.GpsPoint;
import de.ines.entities.Route;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@Service
public class OutlierDeletionService {

    public GpsPoint[] outlierDeletion(String jsonRoute) throws IOException {
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
            double distance = RouteProcessingHelper.directDistance(p_i.getLatitude(), p_i.getLongitude(), p_j.getLatitude(), p_j.getLongitude());
            if( distance > distThreshold){
                resultingRoute.remove(j);
                i--;
            }
        }
        return (GpsPoint[])resultingRoute.toArray();
    }

}
