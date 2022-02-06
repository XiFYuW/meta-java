package com.meta.chain.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meta.chain.mama.model.SaleOrder;
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
        mongoTemplate.save(new SaleOrder(1L));
        return "ok";
    }
}
