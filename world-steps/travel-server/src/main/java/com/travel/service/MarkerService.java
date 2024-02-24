package com.travel.service;

import com.travel.dto.MarkerDTO;

public interface MarkerService {

    void save(MarkerDTO markerDTO);

    void update(MarkerDTO markerDTO);
}
