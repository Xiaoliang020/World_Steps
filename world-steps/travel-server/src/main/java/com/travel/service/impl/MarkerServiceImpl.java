package com.travel.service.impl;

import com.travel.constant.MessageConstant;
import com.travel.context.BaseContext;
import com.travel.dto.MarkerDTO;
import com.travel.entity.Marker;
import com.travel.entity.Path;
import com.travel.exception.MarkerException;
import com.travel.repository.MarkerRepository;
import com.travel.service.MarkerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MarkerServiceImpl implements MarkerService {

    @Autowired
    private MarkerRepository markerRepository;

    /**
     * Save the marker in database
     * @param markerDTO
     */
    public void save(MarkerDTO markerDTO) {
        Marker marker = new Marker();
        BeanUtils.copyProperties(markerDTO, marker);

        marker.setCreateTime(LocalDateTime.now());
        marker.setCreateUser(BaseContext.getCurrentId());

        markerRepository.save(marker);
    }

    /**
     * Update the marker in database
     * @param markerDTO
     */
    public void update(MarkerDTO markerDTO) {
        Optional<Marker> optionalMarker = markerRepository.findByMarkerID(markerDTO.getMarkerID());
        if (!optionalMarker.isPresent()) {
            throw new MarkerException(MessageConstant.MARKER_NOT_FOUND);
        }

        Marker oldMarker = optionalMarker.get();
        // 忽略"id"属性
        BeanUtils.copyProperties(markerDTO, oldMarker, "id");

        markerRepository.save(oldMarker);
    }
}
