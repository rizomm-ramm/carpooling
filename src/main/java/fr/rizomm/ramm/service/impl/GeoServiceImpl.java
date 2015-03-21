package fr.rizomm.ramm.service.impl;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import fr.rizomm.ramm.service.GeoService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maximilien on 19/03/2015.
 */
@Service("geoService")
public class GeoServiceImpl implements GeoService {
    private final GeoApiContext context;


    public GeoServiceImpl() {
        context = new GeoApiContext().setApiKey("AIzaSyD200mcJPOCttxgR41W-AN0sswv6s65oCo");
    }

    @Override
    public GeoApiContext getGeoApiContext() {
        return context;
    }

    @Override
    public DistanceMatrix distance(String[] origin, String[] destination) throws Exception {
        DistanceMatrixApiRequest distanceMatrixApiRequest = DistanceMatrixApi.getDistanceMatrix(context, origin, destination);
        distanceMatrixApiRequest.mode(TravelMode.DRIVING);
        distanceMatrixApiRequest.language("fr");

        return distanceMatrixApiRequest.await();
    }

    @Override
    public DistanceMatrix distance(String origin, String destination) throws Exception {
        return distance(new String[] {origin}, new String[] {destination});
    }

    @Override
    public List<String> coordinateToAddress(Double lat, Double lng) throws Exception {
        return coordinateToAddress(new LatLng(lat, lng));
    }

    @Override
    public List<String> coordinateToAddress(LatLng latLng) throws Exception {
        GeocodingResult[] request = GeocodingApi.reverseGeocode(context, latLng).await();

        List<String> addresses = new ArrayList<>();
        for (GeocodingResult geocodingApiRequest : request) {
            addresses.add(geocodingApiRequest.formattedAddress);
        }

        return addresses;
    }
}
