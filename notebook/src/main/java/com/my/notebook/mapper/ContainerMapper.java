package com.my.notebook.mapper;

import com.my.notebook.domain.ContainerDTO;
import com.my.notebook.domain.ids.ACIdsDTO;
import com.my.notebook.domain.container.CreateContainerDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ContainerMapper {
    @Insert("insert into CONTAINER_TBL (a_id, c_id, c_title)\n" +
            "VALUES (${accountId}, (select C_id_seq from CONTAINER_seq_tbl where a_id=${accountId}), #{containerTitle})")
    void insertContainer(CreateContainerDTO createContainerDTO);

    @Select("select * from container_tbl WHERE a_id = ${accountId}")
    List<ContainerDTO> selectContainersByAccountId(long accountId);

    @Select("select * from container_tbl where a_id = ${accountId} and c_id = ${containerId}")
    ContainerDTO selectContainerByContainerId(ACIdsDTO acIdsDTO);

    @Delete("delete from container_tbl where a_id = ${accountId} and c_id = ${containerId}")
    void deleteContainerByContainerId(ACIdsDTO acIdsDTO);

}
