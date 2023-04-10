package com.my.notebook.domain.container;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class CreateContainerDTO {
    private long accountId;
    private String containerTitle;

    public CreateContainerDTO(long accountId, String containerTitle) {
        this.accountId = accountId;
        this.containerTitle = containerTitle;
    }
}
