package de.ines.controller;

import de.ines.entities.GpsPoint;
import de.ines.entities.Route;
import de.ines.services.RoutePreprocessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class RoutePreProcessingController {

    @Autowired
    public RoutePreprocessingService routePreprocessingService;

    @RequestMapping("/staypointDetection")
    public GpsPoint[] staypointDetection(@RequestParam("route") String jsonRoute) throws IOException {
        return routePreprocessingService.staypointDetection(jsonRoute);
    }

    @RequestMapping("/outlierDeletion")
    public GpsPoint[] outlierDeletion(@RequestParam("route") String jsonRoute) throws IOException {
        return routePreprocessingService.outlierDeletion(jsonRoute);
    }

    @RequestMapping("/smoothing")
    public GpsPoint[] smoothing(@RequestParam("route") String jsonRoute) throws IOException {
        return routePreprocessingService.smoothRoute(jsonRoute);
    }

}
