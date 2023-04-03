package com.my.notebook.domain.container;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateContainerDTO {
    private long accountId;
    private String containerTitle;
}
