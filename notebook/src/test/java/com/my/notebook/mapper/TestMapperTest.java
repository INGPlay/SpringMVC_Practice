package com.my.notebook.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestMapperTest {

    @Autowired
    private TestMapper testMapper;

    @Test
    public void getTest(){
        System.out.println("sysdate : " + testMapper.getTest());
    }

    @Test
    public void showUser(){
        System.out.println("User : " + testMapper.showUser());
    }

}
