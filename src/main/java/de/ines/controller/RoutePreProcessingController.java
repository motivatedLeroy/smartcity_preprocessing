package de.ines.controller;

import de.ines.entities.GpsPoint;
import de.ines.requestWrappers.OutlierDeletionWrapper;
import de.ines.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class RoutePreProcessingController {

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

    @RequestMapping(value = "/staypointDetection", method = RequestMethod.POST)
    public GpsPoint[] staypointDetection(@RequestBody String staypointDetectionWrapper) throws IOException {
        return staypointDetectionService.staypointDetection(staypointDetectionWrapper);
    }

    @RequestMapping(value = "/outlierDeletion", method = RequestMethod.POST)
    public GpsPoint[] outlierDeletion(@RequestBody String outlierDeletionWrapper) throws IOException {
        return outlierDeletionService.outlierDeletion(outlierDeletionWrapper);
    }

    @RequestMapping(value = "/smoothing", method = RequestMethod.POST)
    public GpsPoint[] smoothing(@RequestBody String jsonRoute) throws IOException {
        return smoothRouteService.smoothRoute(jsonRoute);
    }

    @RequestMapping(value = "/douglasPeucker", method = RequestMethod.POST)
    public GpsPoint[] douglasPeucker(@RequestBody String douglasPeuckerWrapper) throws IOException {
        return douglasPeuckerService.simplify(douglasPeuckerWrapper);
    }

    @RequestMapping("/mapMatching")
    public void mapMatchting(@RequestParam("gpxFileContent") String gpxFileContent){
        mapMatchingService.mapMatching(gpxFileContent);
    }

    @RequestMapping("/test")
    public String test(){
        return "hallo";
    }



}
