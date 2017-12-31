package de.ines.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.ines.entities.GpsPoint;
import de.ines.entities.Route;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

@Service
public class DouglasPeuckerService {


    private GpsPoint[] sampleArray;

    /**
     * Based on the simplify algorithm of "https://github.com/hgoebl/simplify-java"
     * @param jsonRoute the route to be entered via json
     * @param tolerance used to compute the squared tolerance that limitates the number of points in the simplified route
     * @param highestQuality defines that either radial distance or Douglas - Peucker is used to determine the simplified route
     * @return the simplified route
     * @throws IOException
     */
    public GpsPoint[] simplify(String jsonRoute, double tolerance, boolean highestQuality) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        Route route = null;
        route = mapper.readValue(jsonRoute, Route.class);
        GpsPoint[] points = route.route;

        if (points == null || points.length <= 2) {
            return points;
        }

        double sqTolerance = tolerance * tolerance;

        if (!highestQuality) {
            points = simplifyRadialDistance(points, sqTolerance);
        }

        points = simplifyDouglasPeucker(points, sqTolerance);

        return points;
    }

    GpsPoint[] simplifyRadialDistance(GpsPoint[] points, double sqTolerance) {
        GpsPoint point = null;
        GpsPoint prevPoint = points[0];

        List<GpsPoint> newPoints = new ArrayList<GpsPoint>();
        newPoints.add(prevPoint);

        for (int i = 1; i < points.length; ++i) {
            point = points[i];

            if (getSquareDistance(point, prevPoint) > sqTolerance) {
                newPoints.add(point);
                prevPoint = point;
            }
        }

        if (prevPoint != point) {
            newPoints.add(point);
        }

        return newPoints.toArray(sampleArray);
    }

    private static class Range {
        private Range(int first, int last) {
            this.first = first;
            this.last = last;
        }
        int first;
        int last;
    }

    /**
     * The Douglas - Peucker algorithm based on "Topologically Consistent Line Simplification with the Douglas-Peucker Algorithm".
     * @param points
     * @param sqTolerance
     * @return
     */
    GpsPoint[] simplifyDouglasPeucker(GpsPoint[] points, double sqTolerance) {

        BitSet bitSet = new BitSet(points.length);
        bitSet.set(0);
        bitSet.set(points.length - 1);

        List<Range> stack = new ArrayList<Range>();
        stack.add(new Range(0, points.length - 1));

        while (!stack.isEmpty()) {
            Range range = stack.remove(stack.size() - 1);

            int index = -1;
            double maxSqDist = 0f;

            // find index of point with maximum square distance from first and last point
            for (int i = range.first + 1; i < range.last; ++i) {
                double sqDist = getSquareSegmentDistance(points[i], points[range.first], points[range.last]);

                if (sqDist > maxSqDist) {
                    index = i;
                    maxSqDist = sqDist;
                }
            }

            if (maxSqDist > sqTolerance) {
                bitSet.set(index);

                stack.add(new Range(range.first, index));
                stack.add(new Range(index, range.last));
            }
        }

        List<GpsPoint> newPoints = new ArrayList<GpsPoint>(bitSet.cardinality());
        for (int index = bitSet.nextSetBit(0); index >= 0; index = bitSet.nextSetBit(index + 1)) {
            newPoints.add(points[index]);
        }

        return newPoints.toArray(sampleArray);
    }


    /**
     * Computes the squared distance between two GpsPoints (of a route).
     * @param p1 the first GpsPoint
     * @param p2 the second GpsPoint
     * @return the squared distance between p1 and p2
     */
    public double getSquareDistance(GpsPoint p1, GpsPoint p2){

        double dx = p1.getLatitude() - p2.getLatitude();
        double dy = p1.getLongitude() - p2.getLongitude();

        return dx * dx + dy * dy;
    }

    /**
     * Computes the squared distance between a segment and a point. The segment is stretched out
     * between two points.
     * @param p0 the point to which the distance of a segment is calculated
     * @param p1 the first corner of the stretched out segment
     * @param p2 the second croner of the stretched out segment
     * @return the squared distance between the segment defined by p1 and p2 and the single point p0
     */
    public  double getSquareSegmentDistance(GpsPoint p0, GpsPoint p1, GpsPoint p2){
        double x0, y0, x1, y1, x2, y2, dx, dy, t;

        x1 = p1.getLatitude();
        y1 = p1.getLongitude();
        x2 = p2.getLatitude();
        y2 = p2.getLongitude();
        x0 = p0.getLatitude();
        y0 = p0.getLongitude();

        dx = x2 - x1;
        dy = y2 - y1;

        if (dx != 0.0d || dy != 0.0d) {
            t = ((x0 - x1) * dx + (y0 - y1) * dy)
                    / (dx * dx + dy * dy);

            if (t > 1.0d) {
                x1 = x2;
                y1 = y2;
            } else if (t > 0.0d) {
                x1 += dx * t;
                y1 += dy * t;
            }
        }

        dx = x0 - x1;
        dy = y0 - y1;

        return dx * dx + dy * dy;
    }



}
