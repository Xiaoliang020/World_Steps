package com.travel.mapper;

import com.github.pagehelper.Page;
import com.travel.dto.CommentPageQueryDTO;
import com.travel.entity.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper {

    /**
     * Comment page query
     * @param commentPageQueryDTO
     * @return
     */
    Page<Comment> pageQuery(CommentPageQueryDTO commentPageQueryDTO);

    /**
     * Save a new comment
     * @param comment
     */
    @Insert("insert into comment (user_id, entity_type, entity_id, target_id, username, avatar, content, status, create_time) "
            + "values" +
            "(#{userId}, #{entityType}, #{entityId}, #{targetId}, #{username}, #{avatar}, #{content}, #{status}, #{createTime})")
    void save(Comment comment);

    /**
     * Get by entity type and id
     * @param entityType
     * @param entityId
     * @return
     */
    @Select("select * from comment where entity_id = #{entityId} and entity_type = #{entityType} and status = 0")
    List<Comment> getByEntity(int entityType, Long entityId);

}
