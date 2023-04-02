package com.my.notebook.mapper;

import com.my.notebook.domain.ContainerDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ContainerMapperTest {

    @Autowired
    private ContainerMapper containerMapper;
    @Autowired
    private SeqMapper seqMapper;

    @BeforeEach
    public void beforeEach() {
        // Sequence 초기화
        seqMapper.dropContainerIdSeq();
        seqMapper.createContainerIdSeq();

        containerMapper.insertContainer("라");
        containerMapper.insertContainer("하");
        containerMapper.insertContainer("가");
    }

    @Test
    public void insertContainer() {
        List<ContainerDTO> containers = containerMapper.selectAll();

        assertThat(containers.size()).isEqualTo(3);
    }

    @Test
    public void selectAll() {
        List<ContainerDTO> containers = containerMapper.selectAll();

        assertThat(containers.get(0).getC_title()).isEqualTo("가");
        assertThat(containers.get(1).getC_title()).isEqualTo("라");
        assertThat(containers.get(2).getC_title()).isEqualTo("하");
    }

    @Test
    public void selectById() {
        assertThat(containerMapper.selectById(1L).getC_title()).isEqualTo("라");
        assertThat(containerMapper.selectById(2L).getC_title()).isEqualTo("하");
        assertThat(containerMapper.selectById(3L).getC_title()).isEqualTo("가");
    }



    @Test
    void deleteConatainerById() {
        containerMapper.deleteContainerById(2l);

        assertThat(containerMapper.selectById(2L)).isNull();

        List<ContainerDTO> containers = containerMapper.selectAll();
        assertThat(containers.size()).isEqualTo(2);
    }

    @Test
    void updateTitleById(){
        Long id = 2L;
        String title = "또 바뀐 두번째";

        containerMapper.updateTitleById(id, title);
        ContainerDTO container = containerMapper.selectById(id);

        assertThat(id).isEqualTo(container.getC_id());
        assertThat(title).isEqualTo(container.getC_title());
    }
}