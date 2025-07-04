package com.vinno.osa.entity;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class Inventory {

    private MongoCollection<Document> collection;

    public Inventory() {
    }

    public Inventory(MongoCollection<Document> collection) {
        this.collection = collection;
    }
}
