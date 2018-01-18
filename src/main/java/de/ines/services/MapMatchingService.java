package de.ines.services;

import com.graphhopper.GraphHopper;
import com.graphhopper.matching.EdgeMatch;
import com.graphhopper.matching.GPXFile;
import com.graphhopper.matching.MapMatching;
import com.graphhopper.matching.MatchResult;
import com.graphhopper.reader.osm.GraphHopperOSM;
import com.graphhopper.routing.AlgorithmOptions;
import com.graphhopper.routing.util.CarFlagEncoder;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.routing.weighting.FastestWeighting;
import com.graphhopper.routing.weighting.Weighting;
import com.graphhopper.util.EdgeIterator;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.GPXEntry;
import com.graphhopper.util.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class MapMatchingService {

    public static GraphHopper hopper = new GraphHopperOSM();
    public static CarFlagEncoder encoder;


    /**
     * Based on the map matching service on "https://github.com/graphhopper/map-matching"
     * Reads in map data (currently only the map of Leipzig, Germany)
     *  and enables the matching serivce to work in this area
     */
    @Autowired
    public MapMatchingService(){
        // import OpenStreetMap data
        hopper.setDataReaderFile("./map-data/baden-wuerttemberg-latest.osm.pbf");
        hopper.setGraphHopperLocation("./target/mapmatchingtest");
        encoder = new CarFlagEncoder();
        hopper.setEncodingManager(new EncodingManager(encoder));
        hopper.getCHFactoryDecorator().setEnabled(false);
        hopper.importOrLoad();

    }

    /**
     * Based on the map matching service on "https://github.com/graphhopper/map-matching"
     * @param gpxFileContent creates a file based on the gpxFileContent and hands it over to the map matching enginge
     * @return  a list of GraphHopper edges with all associated GPX entries. The edges (or their IDs can be stored for later use
     */

    public List<EdgeMatch> mapMatching(String gpxFileContent){
        // configure Map


        // create MapMatching object, can and should be shared accross threads
        String algorithm = Parameters.Algorithms.DIJKSTRA_BI;
        Weighting weighting = new FastestWeighting(encoder);
        AlgorithmOptions algoOptions = new AlgorithmOptions(algorithm, weighting);
        MapMatching mapMatching = new MapMatching(hopper, algoOptions);

        // do the actual matching, get the GPX entries from a file or via stream
        File file = new File("src/main/resources/route.gpx");
        try {
            FileWriter fileWriter = new FileWriter(file, false);
            fileWriter.write(gpxFileContent);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<GPXEntry> inputGPXEntries = new GPXFile().doImport("src/main/resources/route.gpx").getEntries();
        MatchResult mr = mapMatching.doWork(inputGPXEntries);

        // return GraphHopper edges with all associated GPX entries
        return mr.getEdgeMatches();

    }



}
