package de.ines.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.ines.entities.GpsPoint;
import de.ines.entities.Route;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@Service
public class SmoothRouteService {

    /**
     * Based on Simon Beckmann's smoothing - algorithm (bachelor thesis)
     * @param jsonRoute the route to be smoothed
     * @return the smoothed route
     * @throws IOException
     */

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

}
