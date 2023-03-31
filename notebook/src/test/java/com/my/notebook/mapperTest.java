package com.my.notebook;

import com.my.notebook.domain.BoardMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class mapperTest {

    @Autowired
    private BoardMapper boardMapper;

    @Test
    public void testNow(){
        System.out.println(boardMapper.getTest());
    }

}
