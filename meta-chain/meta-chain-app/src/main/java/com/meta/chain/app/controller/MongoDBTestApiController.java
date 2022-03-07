package com.meta.chain.app.controller;

import com.meta.chain.mama.model.Test;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MongoDBTestApiController {

    private final MongoTemplate mongoTemplate;

    public MongoDBTestApiController(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping
    public String save() {
        return mongoTemplate.save(new Test(1L)).toString();
    }
}
