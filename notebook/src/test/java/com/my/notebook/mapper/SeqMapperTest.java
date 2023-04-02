package com.my.notebook.mapper;

import jdk.jfr.StackTrace;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SeqMapperTest {
    @Autowired ContainerMapper containerMapper;
    @Autowired SeqMapper seqMapper;

    @BeforeEach
    public void beforeEach() {
        seqMapper.dropContainerIdSeq();
        seqMapper.createContainerIdSeq();
    }

    @Test
    @Transactional
    public void getContainerIdCurrval() {
        containerMapper.insertContainer("test");
        assertThat(seqMapper.getContainerIdSeqCurrval()).isEqualTo(1);

        containerMapper.insertContainer("test");
        assertThat(seqMapper.getContainerIdSeqCurrval()).isEqualTo(2);

        containerMapper.insertContainer("test");
        assertThat(seqMapper.getContainerIdSeqCurrval()).isEqualTo(3);
    }
}