package fr.rizomm.ramm.controller.rest;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeocodingApi;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import fr.rizomm.ramm.service.GeoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maximilien on 19/03/2015.
 */
@RestController
@RequestMapping("/api/geo")
@Slf4j
public class GeoRestController {

    private final GeoService geoService;

    @Autowired
    public GeoRestController(GeoService geoService) {
        this.geoService = geoService;
    }

    @RequestMapping(value = "/addresses", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> coordinatesToAddresses(@RequestParam("lat") Double lat, @RequestParam("lng") Double lng) throws Exception {
        return geoService.coordinateToAddress(lat, lng);
    }

    @RequestMapping(value = "/distance", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DistanceMatrix distance(@RequestParam("origin") String[] origin,
                                               @RequestParam("destination") String[] destination) throws Exception {
        return geoService.distance(origin, destination);
    }


}
