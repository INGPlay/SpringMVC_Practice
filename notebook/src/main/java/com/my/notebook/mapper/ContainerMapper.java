package com.my.notebook.mapper;

import com.my.notebook.domain.ContainerDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ContainerMapper {
    List<ContainerDTO> selectAll();

    ContainerDTO selectById(Long id);

    void insertContainer(String title);

    void deleteContainerById(Long id);

    void updateTitleById(@Param("id") Long id, @Param("title") String title);
}
