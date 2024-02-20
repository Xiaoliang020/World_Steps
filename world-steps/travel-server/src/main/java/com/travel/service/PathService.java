package com.travel.service;

import com.travel.dto.PathDTO;
import com.travel.dto.PathImageDTO;
import com.travel.dto.PathPageQueryDTO;
import com.travel.result.PageResult;
import com.travel.vo.PathVO;

import java.util.List;

public interface PathService {

    /**
     * Save the path in database
     * @param pathDTO
     */
    String save(PathDTO pathDTO);

    /**
     * Update the screenshot of path
     * @param pathImageDTO
     */
    void updateImage(PathImageDTO pathImageDTO);


    /**
     * Page query paths based on id
     * @param pathPageQueryDTO
     * @return
     */
    PageResult pageQuery(PathPageQueryDTO pathPageQueryDTO);

    /**
     * Delete by path id
     * @param pathId
     */
    void deleteById(String pathId);
}
