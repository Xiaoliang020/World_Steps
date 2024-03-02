package com.travel.service;

import com.travel.dto.MarkerDTO;
import com.travel.entity.Marker;

import java.util.List;

public interface MarkerService {

    /**
     * Save a marker
     * @param markerDTO
     */
    void save(MarkerDTO markerDTO);

    /**
     * Update a marker
     * @param markerDTO
     */
    void update(MarkerDTO markerDTO);

    /**
     * Get markers by path id
     * @param pathId
     * @return
     */
    List<Marker> getByPathId(String pathId);
}
