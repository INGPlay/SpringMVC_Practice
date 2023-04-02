package com.my.notebook.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestMapper {

    String getTest();

    String showUser();
}
