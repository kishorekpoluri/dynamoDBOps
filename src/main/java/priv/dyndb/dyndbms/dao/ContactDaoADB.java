package priv.dyndb.dyndbms.dao;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;

@Repository
public class ContactDaoADB {

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    private static final String tableName = "CONTACT";

    @PostConstruct
    public void postConstruct() {
        //createTable();
        //createInitialRecords();
    }

    public void createTable(){
        ListTablesResult tablesResult = amazonDynamoDB.listTables();
        boolean exists = tablesResult.getTableNames().stream().anyMatch((tableName) -> tableName.equals("CONTACT"));
        if (!exists) {
            CreateTableRequest request = new CreateTableRequest();
            request.setTableName(tableName);
            List<AttributeDefinition> attributeDefinitions = Arrays.asList(
                    new AttributeDefinition[]{
                            new AttributeDefinition("Name", ScalarAttributeType.S),
                            //new AttributeDefinition("Phone", ScalarAttributeType.S),
                            //new AttributeDefinition("Email", ScalarAttributeType.S)
                    });
            request.setAttributeDefinitions(attributeDefinitions);

            /* Create & Set a list of KeySchemaElement */
            List<KeySchemaElement> keySchema = Arrays.asList(
                    new KeySchemaElement[]{
                            new KeySchemaElement("Name", KeyType.HASH),
                    });
            request.setKeySchema(keySchema);

            /* Setting Provisioned Throughput */
            request.setProvisionedThroughput(new ProvisionedThroughput(new Long(1), new Long(1)));

            try {
                /* Send Create Table Request */
                CreateTableResult result = amazonDynamoDB.createTable(request);

                System.out.println("Status : " + result.getSdkHttpMetadata().getHttpStatusCode());

                System.out.println("Table Name : " + result.getTableDescription().getTableName());

                createInitialRecords();
            } catch (AmazonServiceException e) {
                System.out.println(e.getErrorMessage());
            }
        }
    }



    public void createInitialRecords() {
        /* Create an Object of PutItemRequest */
        PutItemRequest request = new PutItemRequest();

        /* Setting Table Name */
        request.setTableName(tableName);

        /* Setting Consumed Capacity */
        request.setReturnConsumedCapacity(ReturnConsumedCapacity.TOTAL);

        /* To get old value of item's attributes before it is overwritten */
        request.setReturnValues(ReturnValue.ALL_OLD);

        try {
            /* Create a Map of attributes */
            Map<String, AttributeValue> map = new HashMap<>();
            map.put("Name", new AttributeValue("John Doe"));
            map.put("Phone", new AttributeValue("123-456-7890"));
            map.put("Email", new AttributeValue("johndoe@gmail.com"));

            request.setItem(map);

            /* Send Put Item Request */
            PutItemResult result = amazonDynamoDB.putItem(request);

            /* Printing Old Attributes Name and Values */
            if (result.getAttributes() != null) {
                result.getAttributes().entrySet().stream()
                        .forEach(e -> System.out.println(e.getKey() + " " + e.getValue()));
            }

            map.put("Name", new AttributeValue("Jane Doe"));
            map.put("Phone", new AttributeValue("123-455-7890"));
            map.put("Email", new AttributeValue("janedoe@gmail.com"));
            result = amazonDynamoDB.putItem(request);

            System.out.println("Status : " + result.getSdkHttpMetadata().getHttpStatusCode());

            System.out.println("Consumed Capacity : " + result.getConsumedCapacity().getCapacityUnits());

            /* Printing Old Attributes Name and Values */
            if (result.getAttributes() != null) {
                result.getAttributes().entrySet().stream()
                        .forEach(e -> System.out.println(e.getKey() + " " + e.getValue()));
            }

        } catch (AmazonServiceException e) {
            System.out.println(e.getErrorMessage());
        }
    }

    public void retrieveRecords() {
        ScanRequest scanRequest = new ScanRequest()
                .withTableName(tableName);

        ScanResult result = amazonDynamoDB.scan(scanRequest);
        for (Map<String, AttributeValue> item : result.getItems()) {
            System.out.println(item);
        }
    }

}
