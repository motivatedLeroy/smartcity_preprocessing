package de.ines.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.ines.Helpers.RouteProcessingHelper;
import de.ines.entities.GpsPoint;
import de.ines.entities.Route;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;


@Service
public class StaypointDetectionService {


    /**
     * Adapted and based on "https://gist.github.com/ckybonist/587b280b5abc8eb48a7c1a450b38ea23"
     * @param jsonRoute the route which staypoints should be detected
     * @param distThreshold distance threshold in meters
     * @param timeThreshold time threshold in seconds
     * @return the staypoint - cleansed route
     * @throws IOException
     */
    public GpsPoint[] staypointDetection(String jsonRoute, int distThreshold, int timeThreshold) throws IOException {

        ArrayList<GpsPoint> staypoints = new ArrayList<GpsPoint>();
        ObjectMapper mapper = new ObjectMapper();
        Route route = null;
        route = mapper.readValue(jsonRoute, Route.class);
        GpsPoint[] points = route.route;


        int i = 0;
        while(i <points.length) {
            int j = i + 1;
            GpsPoint p_i = points[i];
            while (j < points.length){
                GpsPoint p_j = points[j];
                double distance = RouteProcessingHelper.directDistance(p_i.getLatitude(), p_i.getLongitude(), p_j.getLatitude(), p_j.getLongitude());
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


}
