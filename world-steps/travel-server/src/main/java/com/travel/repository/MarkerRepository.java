package com.travel.repository;

import com.travel.entity.Marker;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface MarkerRepository extends MongoRepository<Marker, String> {
    List<Marker> findByPathID(String pathid);
    Optional<Marker> findByMarkerID(String markerID);
}
