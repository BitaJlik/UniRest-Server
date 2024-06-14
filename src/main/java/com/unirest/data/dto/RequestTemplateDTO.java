package com.unirest.data.dto;

import com.unirest.data.models.RequestTemplate;
import lombok.Data;

@Data
public class RequestTemplateDTO {
    private Long id;
    private boolean visible;
    private int level;
    private String keysJson;
    private String name;
    private String templateName;
    private Long dormitoryId;

    public RequestTemplateDTO(RequestTemplate template) {
        this.id = template.getId();
        this.visible = template.isVisible();
        this.level = template.getLevel();
        this.keysJson = template.getKeysJson();
        this.name = template.getName();
        this.templateName = template.getTemplateName();
        this.dormitoryId = template.getDormitory().getId();
    }
}
