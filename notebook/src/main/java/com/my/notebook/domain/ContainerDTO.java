package com.my.notebook.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ContainerDTO {
    private Long c_id;
    private String c_title;

    public ContainerDTO(String title) {
        this.c_title = title;
    }
}
