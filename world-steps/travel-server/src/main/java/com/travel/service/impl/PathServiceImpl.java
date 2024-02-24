package com.travel.service.impl;

import com.travel.constant.MessageConstant;
import com.travel.dto.PathDTO;
import com.travel.dto.PathImageDTO;
import com.travel.dto.PathPageQueryDTO;
import com.travel.entity.Coordinate;
import com.travel.entity.Marker;
import com.travel.entity.Path;
import com.travel.exception.DeletionNotAllowedException;
import com.travel.repository.MarkerRepository;
import com.travel.repository.PathRepository;
import com.travel.result.PageResult;
import com.travel.service.PathService;
import com.travel.vo.PathShareVO;
import org.aspectj.bridge.Message;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonLineString;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PathServiceImpl implements PathService {

    @Autowired
    private PathRepository pathRepository;

    @Autowired
    private MarkerRepository markerRepository;

    /**
     * Save the path in database
     * @param pathDTO
     */
    public String save(PathDTO pathDTO) {
        Path path = new Path();
        BeanUtils.copyProperties(pathDTO, path);

        // Set the lineString in Path object
        List<Point> points = new ArrayList<>();
        for (Coordinate coordinate : pathDTO.getPath()) {
            points.add(new GeoJsonPoint(coordinate.getLng(), coordinate.getLat()));
        }

        GeoJsonLineString lineString = new GeoJsonLineString(points);
        path.setLineString(lineString);

        if (path.getName().trim().equals("")) {
            path.setName("Path (" + path.getEndTime() + ")");
        }
        pathRepository.save(path);

        return path.getId();
    }

    /**
     * Update the screenshot of path
     * @param pathImageDTO
     */
    public void updateImage(PathImageDTO pathImageDTO) {
        String pathId = pathImageDTO.getId();
        Path path = pathRepository.findById(pathId).orElseThrow(() -> new RuntimeException("Path not found"));

        path.setScreenshot(pathImageDTO.getScreenshot());
        pathRepository.save(path);
    }

    /**
     * Page query paths based on id
     * @param pathPageQueryDTO
     * @return
     */
    public PageResult pageQuery(PathPageQueryDTO pathPageQueryDTO) {
        // 创建PageRequest对象，设置页码和每页大小
        Pageable pageable = PageRequest.of(pathPageQueryDTO.getPage() - 1, pathPageQueryDTO.getPageSize());

        // 根据userId和分页信息进行查询
        Page<Path> pathPage = pathRepository.findByUserId(pathPageQueryDTO.getUserId(), pageable);

        return new PageResult(pathPage.getTotalPages(), pathPage.getContent());
    }

    /**
     * Delete by path id
     * @param pathId
     */
    public void deleteById(String pathId) {
        if (!pathRepository.existsById(pathId)) {
            throw new DeletionNotAllowedException(MessageConstant.PATH_NOT_FOUND);
        }

        List<Marker> markerList = markerRepository.findByPathID(pathId);
        for (Marker marker : markerList) {
            markerRepository.deleteById(marker.getId());
        }

        pathRepository.deleteById(pathId);
    }

    /**
     * Get path by id
     * @param pathId
     * @return
     */
    public PathShareVO getById(String pathId) {
        if (!pathRepository.existsById(pathId)) {
            throw new DeletionNotAllowedException(MessageConstant.PATH_NOT_FOUND);
        }

        Optional<Path> pathOptional = pathRepository.findById(pathId);
        Path path = pathOptional.get();
        PathShareVO pathShareVO = new PathShareVO();
        BeanUtils.copyProperties(path, pathShareVO);

        return pathShareVO;
    }
}
