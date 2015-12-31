package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class DbGenerator {
    public static void main(String[] args) throws Exception{
        System.out.println("DbGenerator --> Started");
        Schema schema = new Schema(1000, "com.zibilal.dao");
        generateTable(schema);
        new DaoGenerator().generateAll(schema, "F:\\learning-curves\\android\\GreenDaoCheck\\app\\src\\main\\java");
        System.out.println("DbGenerator --> Finished");
    }

    private static void generateTable(Schema schema) {

        Entity contact = schema.addEntity("Contact");
        contact.addIdProperty();
        contact.addStringProperty("displayName").unique();
        contact.addStringProperty("firstName");
        contact.addStringProperty("lastName");
        contact.addStringProperty("middleName");
        contact.addStringProperty("nickName");
        contact.addStringProperty("title");
        contact.addIntProperty("contactId");
        contact.addLongProperty("deviceContactid");
        contact.addDateProperty("createDate").notNull();

    }
}
