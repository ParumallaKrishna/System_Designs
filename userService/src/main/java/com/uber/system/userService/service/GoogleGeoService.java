// package com.uber.system.userService.service;

// import com.google.maps.DirectionsApi;
// import com.google.maps.GeoApiContext;
// import com.google.maps.GeocodingApi;
// import com.google.maps.model.DirectionsResult;
// import com.google.maps.model.DirectionsRoute;
// import com.google.maps.model.GeocodingResult;
// import com.google.maps.model.TravelMode;
// import com.uber.system.userService.dto.Coordinates;
// import com.uber.system.userService.dto.PriceEstimateResponse;
// import com.uber.system.userService.repository.PriceEstimateRepository;
// import com.google.maps.errors.ApiException;

// import java.io.IOException;
// import java.math.BigDecimal;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;

// @Service
// public class GoogleGeoService {

// private final GeoApiContext context;
// private final PriceEstimateRepository priceEstimateRepository;

// // public GoogleGeoService(String apiKey) {
// // this.context = new GeoApiContext.Builder()
// // .apiKey(apiKey)
// // .build();
// // this.priceEstimateRepository = null;
// // }
// public GoogleGeoService(@Value("${google.api.key}") String apiKey,
// PriceEstimateRepository priceEstimateRepository) {
// this.context = new GeoApiContext.Builder()
// .apiKey(apiKey)
// .build();
// this.priceEstimateRepository = priceEstimateRepository;
// }

// public Coordinates getLatLng(String address) throws IOException,
// InterruptedException, ApiException {
// GeocodingResult[] results = GeocodingApi.geocode(context, address).await();
// Coordinates coordinates = null;
// if (results.length > 0) {
// double lat = results[0].geometry.location.lat;
// double lng = results[0].geometry.location.lng;
// System.out.println("Latitude: " + lat + ", Longitude: " + lng);
// coordinates = Coordinates.builder()
// .latitude(lat)
// .longitude(lng)
// .build();

// }
// return coordinates;
// }

// public PriceEstimateResponse getDirections(String origin, String destination)
// throws IOException, InterruptedException, ApiException {
// DirectionsResult result = DirectionsApi.getDirections(context, origin,
// destination)
// .mode(TravelMode.DRIVING) // or WALKING, BICYCLING, TRANSIT
// .await();
// PriceEstimateResponse priceEstimateResponse = null;
// if (result.routes != null && result.routes.length > 0) {
// DirectionsRoute route = result.routes[0];
// String summary = route.summary;
// long distanceInMeters = route.legs[0].distance.inMeters;
// long durationInSeconds = route.legs[0].duration.inSeconds;
// Double KM = distanceInMeters / 1000.0;
// System.out.println("Route Summary: " + summary);
// System.out.println("Distance: " + distanceInMeters / 1000.0 + " km");
// System.out.println("Duration: " + durationInSeconds / 60.0 + " mins");
// priceEstimateResponse = PriceEstimateResponse.builder()
// .estimatedPrice(new BigDecimal(10 * KM)) // Placeholder for estimated price
// .distanceMeters(distanceInMeters)
// .distanceKm(new BigDecimal(distanceInMeters / 1000.0))
// .pickup(Coordinates.builder().latitude(route.legs[0].startLocation.lat)
// .longitude(route.legs[0].startLocation.lng).build())
// .drop(Coordinates.builder().latitude(route.legs[0].endLocation.lat)
// .longitude(route.legs[0].endLocation.lng)
// .build())
// .build();
// }
// priceEstimateRepository.save(priceEstimateResponse);
// return priceEstimateResponse;
// }
// }
