package com.my.notebook.service;

import com.my.notebook.domain.ContainerDTO;
import com.my.notebook.domain.container.CreateContainerDTO;
import com.my.notebook.mapper.ContainerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContainerService {
    private final ContainerMapper containerMapper;

    @Autowired
    public ContainerService(ContainerMapper containerMapper) {
        this.containerMapper = containerMapper;
    }

    public List<ContainerDTO> listContainerByAccountId(long accountId){
        List<ContainerDTO> containers = containerMapper.selectContainerByAccountId(accountId);

        return containers;
    }

    public boolean createContainer(CreateContainerDTO createContainerDTO){
        try {
            containerMapper.insertContainer(createContainerDTO);
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void deleteContainer(long containerId){
        containerMapper.deleteContainerByContainerId(containerId);
    }
}
