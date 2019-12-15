package com.daswort.web.controller;

import com.daswort.core.entity.IdName;
import com.daswort.core.service.IdNameCollection;
import com.daswort.core.service.idname.IdNameService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class IdNameController {

    private final IdNameService idNameService;

    public IdNameController(IdNameService idNameService) {
        this.idNameService = idNameService;
    }

    @GetMapping
    public IdName getIdNameById(IdNameCollection collection) {
        return idNameService.getById(collection, "");
    }

}
