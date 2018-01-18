package de.ines.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.ines.Helpers.RouteProcessingHelper;
import de.ines.entities.GpsPoint;
import de.ines.entities.Route;
import de.ines.requestWrappers.StaypointDetectionWrapper;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;


@Service
public class StaypointDetectionService {


    /**
     * Adapted and based on "https://gist.github.com/ckybonist/587b280b5abc8eb48a7c1a450b38ea23" Detects points where the user moved less than the given time Threshold
     * the given time threshold
     * @param jsonWrapper the json holding information for the wrapper object. The created StaypointDetectionWrapper holds
     *                    values for route, time - and distance thresholds in seconds and meters.
     *
     * Possible jsonWrapper:
     * {
    "route":{
    "id":560754436,
    "route":[{"id":1,"latitude":0.6808774554612159,"longitude":0.7218983487386227,"date":-1670088896008342321,"heading":0.15907077599959962,"speed":0.6552999906327358,"acceleration":0.02741700074302611 }]
    }
    ,"distThreshold":10
    ,"timeThreshold":400
    }
     *
     *
     * @return the staypoints
     * @throws IOException
     */
    public GpsPoint[] staypointDetection(String jsonWrapper) throws IOException {

        ArrayList<GpsPoint> staypoints = new ArrayList<GpsPoint>();
        ObjectMapper mapper = new ObjectMapper();
        StaypointDetectionWrapper wrapper = null;
        wrapper = mapper.readValue(jsonWrapper, StaypointDetectionWrapper.class);
        Route route = null;
        route = wrapper.route;
        GpsPoint[] points = route.route;


        int i = 0;
        while(i <points.length) {
            int j = i + 1;
            GpsPoint p_i = points[i];
            while (j < points.length){
                GpsPoint p_j = points[j];
                double distance = RouteProcessingHelper.directDistance(p_i.getLatitude(), p_i.getLongitude(), p_j.getLatitude(), p_j.getLongitude());
                if (distance > wrapper.distThreshold) {  // Found the region
                    long delta_time = p_j.date - p_i.date;
                    if (delta_time > wrapper.timeThreshold) {
                        staypoints.add(points[j]);
                    }
                    i = j;
                    break;
                }
                else
                j = j + 1;
            }
            i++;
        }
        GpsPoint[] result = new GpsPoint[staypoints.size()];
        for(int j = 0; j < staypoints.size(); j++){
            result[j] = staypoints.get(j);
        }
        return result;
    }


}
