package de.ines.requestWrappers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.ines.entities.Route;

public class OutlierDeletionWrapper {

    public Route route;
    public int distThreshold;

    /**
     * Basic constructor to create the Wrapper object from json
     * A possible json Value is:
     *
     *{

        "route":{
            "id":560754436,
            "route":[{"id":1,"latitude":0.6808774554612159,"longitude":0.7218983487386227,"date":-1670088896008342321,"heading":0.15907077599959962,"speed":0.6552999906327358,"acceleration":0.02741700074302611 }]
        }
        ,"distThreshold":20
     }
     *
     */
    public OutlierDeletionWrapper(){
    }

}
