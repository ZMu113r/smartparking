package com.example.zacharymuller.smartparking.Util;

import java.util.ArrayList;
import java.util.Arrays;

import org.bson.Document;

import com.example.zacharymuller.smartparking.Entities.Spot;
import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;

/***
 *
 * @author 		Nathaniel
 * @description Listener class to provide live changes to
 */

public class GarageListener implements Runnable{
    private MongoCollection<Document> collection;
    private ArrayList<Spot> listRef;

    public GarageListener(MongoCollection<Document> collection) {
        this.collection = collection;
    }

    public GarageListener(MongoCollection<Document> collection, ArrayList<Spot> listRef) {
        this.listRef = listRef;
        this.collection = collection;
    }

    @Override
    public void run() {
        Block<ChangeStreamDocument<Document>> printBlock = new Block<ChangeStreamDocument<Document>>() {
            @Override
            public void apply(final ChangeStreamDocument<Document> changeStreamDocument) {
                Spot spot = new Spot(changeStreamDocument.getFullDocument().get("name").toString(), (Boolean)changeStreamDocument.getFullDocument().get("occupied"));
                if(listRef != null) listRef.add(spot);
                System.out.println("name: " + String.format("sensor_%04d_garage%s", spot.getSpotId(), spot.getGarageString().toLowerCase()) + ", occupied: " + spot.isOccupied());
            }
        };

        collection.watch(Arrays.asList(Aggregates.match(Filters.in("operationType", Arrays.asList("update")))))
                .fullDocument(FullDocument.UPDATE_LOOKUP).forEach(printBlock);
    }
}
