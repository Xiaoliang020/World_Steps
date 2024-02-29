package com.travel.mapper;

import com.github.pagehelper.Page;
import com.travel.dto.PostPageQueryDTO;
import com.travel.entity.Post;
import com.travel.vo.PostVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PostMapper {

    /**
     * Insert a new post
     * @param post
     */
    @Insert("insert into post (title, content, username, user_id, avatar, path_id, create_time, screenshot) "
            + "values" +
            "(#{title}, #{content}, #{username}, #{userId}, #{avatar}, #{pathId}, #{createTime}, #{screenshot})")
    void insert(Post post);

    /**
     * Post page query
     * @param postPageQueryDTO
     * @return
     */
    Page<PostVO> pageQuery(PostPageQueryDTO postPageQueryDTO);

    /**
     * Get by id
     * @param id
     * @return
     */
    @Select("select * from post where id = #{id}")
    Post getById(Long id);
}
