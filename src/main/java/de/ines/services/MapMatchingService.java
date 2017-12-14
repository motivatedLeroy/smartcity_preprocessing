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

import java.util.List;

@Service
public class MapMatchingService {

    public static GraphHopper hopper = new GraphHopperOSM();

    @Autowired
    public MapMatchingService(){
        hopper.setDataReaderFile("./map-data/leipzig_germany.osm.pbf");
    }


    public void mapMatching(){

        // import OpenStreetMap data
        hopper.setGraphHopperLocation("./target/mapmatchingtest");
        CarFlagEncoder encoder = new CarFlagEncoder();
        hopper.setEncodingManager(new EncodingManager(encoder));
        hopper.getCHFactoryDecorator().setEnabled(false);
        hopper.importOrLoad();

        // create MapMatching object, can and should be shared accross threads
        String algorithm = Parameters.Algorithms.DIJKSTRA_BI;
        Weighting weighting = new FastestWeighting(encoder);
        AlgorithmOptions algoOptions = new AlgorithmOptions(algorithm, weighting);
        MapMatching mapMatching = new MapMatching(hopper, algoOptions);

        // do the actual matching, get the GPX entries from a file or via stream
        List<GPXEntry> inputGPXEntries = new GPXFile().doImport("src/main/resources/test1.gpx").getEntries();
        MatchResult mr = mapMatching.doWork(inputGPXEntries);

        // return GraphHopper edges with all associated GPX entries
        List<EdgeMatch> matches = mr.getEdgeMatches();
        // now do something with the edges like storing the edgeIds or doing fetchWayGeometry etc
        for(int i =0; i < matches.size(); i++){
            EdgeIteratorState edgeIteratorState = matches.get(i).getEdgeState();
            System.out.println(edgeIteratorState.getAdjNode());
        }
    }



}
