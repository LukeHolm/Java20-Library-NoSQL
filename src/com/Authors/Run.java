package com.Authors;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.eq;


public class Run {

    private static MongoClient mongoClient = null;
    private static MongoDatabase Cerberus_Assembly = null;
    private static MongoCollection<Document> collection = null;
    private static final InputHandler userInput = new InputHandler();
    private static final String dbName = "Cerberus_Assembly";
    private static final Menu MENU = new Menu();
    private static boolean exitProgram;

    Run() {
        exitProgram = false;
    }

    public void Program() {

        try {
            connectToDbms();
            connectToDatabase();
            connectToCollection();
        } catch (Exception e) {
        }
        try {
            System.out.println("Welcome to the Cerberus Assembly!");
            while (!exitProgram){
                int selection = MENU.mainMenu();
                mainMenu(selection);
            }
        } catch (Exception e) {
        } finally {
            mongoClient.close();
        }
    }

    public void mainMenu(int selection) {
        switch (selection) {
            case 1 -> addAuthor();
            case 2 -> addBook();
            case 3 -> printAll();
            case 4 -> showWork();
            case 5 -> removeAuthor();
            case 6 -> addDefaultAuthors();

            case 0 -> exitProgram = true;
        }
    }

    private static void addAuthor() {
        System.out.println("Please enter name of the author you want to add.");
        String author = userInput.getStringFromUser().toUpperCase();

        Document newAuthor = new Document("name", author)
                .append("Books", Arrays.asList(
                ));

        collection.insertOne(newAuthor);
    }

    private static void addBook() {
        System.out.println("Please enter name of the author.");
        String author = userInput.getStringFromUser().toUpperCase();

        System.out.println("Please enter name of the book you want to add.");
        String book = userInput.getStringFromUser();

        Document query = new Document("name", author);
        Document update = new Document("$push", new Document("Books",
                book
        ));
        collection.updateOne(query, update);
    }

    private static void addDefaultAuthors() {
        Document doc1 = new Document("name", "TERRY PRATCHETT")
                .append("Books",
                        Arrays.asList(
                                "The Colour of Magic",
                                "The Light Fantastic",
                                "Equal Rites",
                                "Mort",
                                "Sourcery",
                                "Wyrd Sisters",
                                "Pyramids",
                                "Guards! Guards!",
                                "Faust Eric",
                                "Moving Pictures"
                        ));

        Document doc2 = new Document("name", "URSULA K. LE GUIN")
                .append("Books",
                        Arrays.asList(
                                "A Wizard of Earthsea",
                                "The Tombs of Atuan",
                                "The Farthest Shore",
                                "Tehanu",
                                "The Other Wind"
                        ));


        Document doc3 = new Document("name", "GEORGE R.R. MARTIN")
                .append("Books",
                        Arrays.asList(
                                "A Game of Thrones",
                                "A Clash of Kings",
                                "A Storm of Swords",
                                "A Feast for Crows",
                                "A Dance with Dragons",
                                "The Winds of Winter",
                                "A Dream of Spring"
                        ));

        List<Document> documents = new ArrayList<Document>();
        documents.add(doc1);
        documents.add(doc2);
        documents.add(doc3);

        collection.insertMany(documents);

        System.out.println("" + documents.size() + " authors were added");
    }

    private static void printAll() {
        MongoIterable<Document> result = collection.find();

        for (Document doc : result) {
            System.out.println(doc.get("name"));
        }
    }

    private static void showWork() {
        System.out.println("Please enter name of author");
        String value = userInput.getStringFromUser().toUpperCase();
        MongoIterable<Document> result = collection.find(eq("name", value));

        for (Document Authors : result) {
            System.out.println("Author: ");
            Set<String> keys = Authors.keySet();
            for (String key : keys) {
                System.out.print(key + ": ");
                System.out.println(Authors.get(key));
            }
        }
    }

    private static void removeAuthor() {
        System.out.println("Please enter name of the author you want to remove.");
        String author = userInput.getStringFromUser().toUpperCase();

        if(userInput.confirm("Are you sure you want to remove " + author + "?")) {
            DeleteResult deleteResult = collection.deleteMany(eq("name", author));
            System.out.println("\n" + deleteResult + "\n");
            System.out.println(author + " has been removed from the Cerberus Assembly!");
        } else {
            System.out.println("Removal has been cancelled.");
        }

    }

    private static void connectToDbms() {
        Logger.getLogger("org.mongodb.driver").setLevel(Level.OFF);
        mongoClient = new MongoClient("localhost", 27017);

        System.out.println("Connection Successful!");
    }
    private static void connectToCollection() {
        Logger.getLogger("org.mongodb.driver").setLevel(Level.OFF);
        collection = Cerberus_Assembly.getCollection("Authors");
    }
    private static void connectToDatabase() {
        Logger.getLogger("org.mongodb.driver").setLevel(Level.OFF);
        Cerberus_Assembly = mongoClient.getDatabase(dbName);
    }

}
