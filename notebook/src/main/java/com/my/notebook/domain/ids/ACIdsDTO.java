package com.my.notebook.domain.ids;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ACIdsDTO {
    private long accountId;
    private long containerId;

    public ACIdsDTO(long accountId, long containerId) {
        this.accountId = accountId;
        this.containerId = containerId;
    }
}
