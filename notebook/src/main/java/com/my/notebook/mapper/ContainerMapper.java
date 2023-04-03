package com.my.notebook.mapper;

import com.my.notebook.domain.ContainerDTO;
import com.my.notebook.domain.container.CreateContainerDTO;
import com.my.notebook.domain.post.ListPostDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ContainerMapper {
    @Insert("insert into CONTAINER_TBL (a_id, c_id, c_title)\n" +
            "VALUES (${accountId}, c_id_seq.nextval, #{containerTitle})")
    void insertContainer(CreateContainerDTO createContainerDTO);

    @Select("select * from container_tbl WHERE a_id = ${accountId}")
    List<ContainerDTO> selectContainerByAccountId(long accountId);

    @Delete("delete from container_tbl where c_id = ${containerId}")
    void deleteContainerByContainerId(long containerId);

}
