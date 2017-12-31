package de.ines.Helpers;

import static java.lang.Math.*;
import static java.lang.Math.sqrt;

public class RouteProcessingHelper {

    /**
     * Transforms a degrees value to radians value.
     * @param degrees
     * @return
     */
    public static double ToRadians(double degrees){
        double radians = degrees * Math.PI / 180;
        return radians;
    }

    /**
     * Computes the harvesine distance based on given longitude and latitude values.
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */
    public static double directDistance(double lat1, double lng1, double lat2, double lng2){
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
