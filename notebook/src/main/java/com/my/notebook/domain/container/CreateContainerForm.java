package com.my.notebook.domain.container;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CreateContainerForm {

    @NotEmpty
    @Size(max = 50)
    private String containerTitle;

    public CreateContainerForm(String containerTitle) {
        this.containerTitle = containerTitle;
    }
}
