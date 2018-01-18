package de.ines.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.ines.Helpers.RouteProcessingHelper;
import de.ines.entities.GpsPoint;
import de.ines.entities.Route;
import de.ines.requestWrappers.OutlierDeletionWrapper;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@Service
public class OutlierDeletionService {

    /**
     * Based on Simon Beckmann's outlier deletion - algorithm (bachelor thesis)
     * @param jsonWrapper the wrapper object holding the route (including arrays of GpsPoints, ids ...) and the distance Threshold value
     * @return the cleaned up route
     * @throws IOException
     */

    public GpsPoint[] outlierDeletion(String jsonWrapper) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        OutlierDeletionWrapper wrapper = null;
        wrapper = mapper.readValue(jsonWrapper, OutlierDeletionWrapper.class);
        Route route = null;
        route = wrapper.route;
        GpsPoint[] points = route.route;
        ArrayList<GpsPoint> resultingRoute = new ArrayList<>(Arrays.asList(points));

        for(int i = 0; i < resultingRoute.size()-1; i++){
            int j = i+1;
            GpsPoint p_i = points[i];
            GpsPoint p_j = points[j];
            double distance = RouteProcessingHelper.directDistance(p_i.getLatitude(), p_i.getLongitude(), p_j.getLatitude(), p_j.getLongitude());
            if( distance > wrapper.distThreshold){
                resultingRoute.remove(j);
                i--;
            }
        }
        GpsPoint[] result = new GpsPoint[resultingRoute.size()];
        for(int i = 0; i < resultingRoute.size(); i++){
            result[i] = resultingRoute.get(i);
        }
        return result;
    }

}
