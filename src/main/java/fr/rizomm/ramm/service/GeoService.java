package fr.rizomm.ramm.service;

import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.LatLng;

import java.util.List;

/**
 * Created by Maximilien on 19/03/2015.
 */
public interface GeoService {

    GeoApiContext getGeoApiContext();

    DistanceMatrix distance(String[] origin,
                            String[] destination) throws Exception;

    DistanceMatrix distance(String origin,
                            String destination) throws Exception;

    List<String> coordinateToAddress(Double lat, Double lng) throws Exception;

    List<String> coordinateToAddress(LatLng latLng) throws Exception;

}
