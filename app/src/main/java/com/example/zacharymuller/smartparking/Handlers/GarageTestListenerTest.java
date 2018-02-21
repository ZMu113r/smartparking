package com.example.zacharymuller.smartparking.Handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.bson.Document;

import com.example.zacharymuller.smartparking.R;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.example.zacharymuller.smartparking.Util.GarageListener;

import com.example.zacharymuller.smartparking.Crypto.CryptoUtils;
import com.example.zacharymuller.smartparking.Crypto.CryptoException;

/**
 * Created by Nate Houlihan on 1/29/2018.
 */

public class GarageTestListenerTest {
    @SuppressWarnings("resource")
    public static void run() throws Exception {
        Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.SEVERE);

        Scanner scanner = new Scanner(new File("res/resources.cfg"));

        //insert crypto code here when ready
        String pwd = "";

        MongoClientURI uri = new MongoClientURI("mongodb+srv://n8houl:" + pwd + "@seniordesign2-ssssl.mongodb.net/");

        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase db = mongoClient.getDatabase("Garages");
        MongoCollection<Document> collectionTest = db.getCollection("GarageTest");

        Thread threadTest = new Thread(new GarageListener(collectionTest), "ThreadTest");
        threadTest.start();
    }
}
