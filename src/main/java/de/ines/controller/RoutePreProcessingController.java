package de.ines.controller;

import de.ines.entities.GpsPoint;
import de.ines.entities.Route;
import de.ines.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class RoutePreProcessingController {

    @Autowired
    public RoutePreprocessingService routePreprocessingService;

    @Autowired
    public DouglasPeuckerService douglasPeuckerService;

    @Autowired
    public SmoothRouteService smoothRouteService;

    @Autowired
    public StaypointDetectionService staypointDetectionService;

    @Autowired
    public OutlierDeletionService outlierDeletionService;

    @Autowired
    public MapMatchingService mapMatchingService;

    @RequestMapping("/staypointDetection")
    public GpsPoint[] staypointDetection(@RequestParam("route") String jsonRoute) throws IOException {
        return staypointDetectionService.staypointDetection(jsonRoute);
    }

    @RequestMapping("/outlierDeletion")
    public GpsPoint[] outlierDeletion(@RequestParam("route") String jsonRoute) throws IOException {
        return outlierDeletionService.outlierDeletion(jsonRoute);
    }

    @RequestMapping("/smoothing")
    public GpsPoint[] smoothing(@RequestParam("route") String jsonRoute) throws IOException {
        return smoothRouteService.smoothRoute(jsonRoute);
    }

    @RequestMapping("/compressRoute")
    public GpsPoint[] compressRoute(@RequestParam("route") String jsonRoute, @RequestParam("tolerance")double tolerance, @RequestParam("highQuality") boolean highQuality) throws IOException {
        return douglasPeuckerService.simplify(jsonRoute, tolerance, highQuality);
    }

    @RequestMapping("/mapMatching")
    public void mapMatchting(){
        mapMatchingService.mapMatching();
    }



}
