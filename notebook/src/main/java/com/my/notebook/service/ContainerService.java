package com.my.notebook.service;

import com.my.notebook.domain.ContainerDTO;
import com.my.notebook.domain.ids.ACIdsDTO;
import com.my.notebook.domain.container.CreateContainerDTO;
import com.my.notebook.mapper.ContainerMapper;
import com.my.notebook.mapper.seq.ContainerSeqMapper;
import com.my.notebook.mapper.seq.PostSeqMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class ContainerService {
    private final ContainerMapper containerMapper;
    private final ContainerSeqMapper containerSeqMapper;
    private final PostSeqMapper postSeqMapper;

    @Autowired
    public ContainerService(ContainerMapper containerMapper, ContainerSeqMapper containerSeqMapper, PostSeqMapper postSeqMapper) {
        this.containerMapper = containerMapper;
        this.containerSeqMapper = containerSeqMapper;
        this.postSeqMapper = postSeqMapper;
    }

    public List<ContainerDTO> selectContainersByAccountId(long accountId){
        List<ContainerDTO> containers = containerMapper.selectContainersByAccountId(accountId);

        return containers;
    }

    public ContainerDTO getContainerByContainerId(ACIdsDTO acIdsDTO){
        return containerMapper.selectContainerByContainerId(acIdsDTO);
    }

    public boolean createContainer(CreateContainerDTO createContainerDTO){
        
        // 글자 수 제한
        if (createContainerDTO.getContainerTitle().length() > 50){
            return false;
        }

        try {
            long accountId = createContainerDTO.getAccountId();
            long containerId = containerSeqMapper.getContainerIdSeqCurrvalByAccountId(accountId);

            containerMapper.insertContainer(createContainerDTO);
            containerSeqMapper.updateContainerIdSeqNextvalByAccountId(accountId);

            // 시퀀스 생성
            ACIdsDTO acIdsDTO = new ACIdsDTO();
            acIdsDTO.setAccountId(accountId);
            acIdsDTO.setContainerId(containerId);
            postSeqMapper.insertPostIdSeqByACIds(acIdsDTO);
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void deleteContainer(ACIdsDTO acIdsDTO){
        containerMapper.deleteContainerByContainerId(acIdsDTO);
    }
}
