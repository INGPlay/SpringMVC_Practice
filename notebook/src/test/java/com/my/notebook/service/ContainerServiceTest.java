package com.my.notebook.service;

import com.my.notebook.domain.ContainerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class ContainerServiceTest {
    private final ContainerService containerService;

    @Autowired
    ContainerServiceTest(ContainerService containerService) {
        this.containerService = containerService;
    }

    @Test
    void listContainers() {
        List<ContainerDTO> a = containerService.listContainerByAccountId(7);

        System.out.println("dddd" + a.size());
        System.out.println("dddd" + a);
    }
}