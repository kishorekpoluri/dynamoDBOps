package priv.dyndb.dyndbms.dao;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.internal.IteratorSupport;
import com.amazonaws.services.dynamodbv2.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import priv.dyndb.dyndbms.util.DBUtil;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Repository
public class ContactDaoDB {

    @Autowired
    private DynamoDB dynamoDB;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    private static final String tableName = "CONTACT";

    @PostConstruct
    public void postConstruct() {
        //deleteTable();
        //createTable();
        //list();
    }

    public void deleteTable(){
        Table table=dynamoDB.getTable(tableName);
        table.delete();
    }

        public void createTable(){
            boolean exists= DBUtil.checkIfTableExists(tableName,amazonDynamoDB);
            if (!exists) {
                CreateTableRequest request = new CreateTableRequest();
                request.setTableName(tableName);
                List<AttributeDefinition> attributeDefinitions = Arrays.asList(
                        new AttributeDefinition[]{
                                new AttributeDefinition("Name", ScalarAttributeType.S)
                        });
                request.setAttributeDefinitions(attributeDefinitions);
                List<KeySchemaElement> keySchema = Arrays.asList(
                        new KeySchemaElement[]{
                                new KeySchemaElement("Name", KeyType.HASH),
                        });
                request.setKeySchema(keySchema);
                request.setProvisionedThroughput(new ProvisionedThroughput(new Long(1), new Long(1)));
                try {
                    Table result = dynamoDB.createTable(request);
                    createInitialData();
                } catch (AmazonServiceException e) {
                    System.out.println(e.getErrorMessage());
                }
            }
        }

    public void createInitialData() {
        Table table = dynamoDB.getTable(tableName);
        try {
            Item item = new Item().withPrimaryKey("Name", "John Doe").withString("Phone", "123-456-7890")
                    .withString("Email", "johndoe@gmail.com");
            table.putItem(item);

            item = new Item().withPrimaryKey("Name", "Jane Doe").withString("Phone", "123-455-7890")
                    .withString("Email", "janedoe@gmail.com");
            table.putItem(item);

        } catch (Exception e) {
            System.err.println("Create items failed.");
            System.err.println(e.getMessage());

        }
    }

    public void list() {
        try {
            Table table=dynamoDB.getTable(tableName);
            ItemCollection<ScanOutcome> items=table.scan();
            IteratorSupport<Item,ScanOutcome> iteratorSupport=items.iterator();
            while(iteratorSupport.hasNext()) {
                Item item=iteratorSupport.next();
                System.out.println(item);
            }
        } catch (Exception e) {
            System.err.println("Create items failed.");
            System.err.println(e.getMessage());

        }
    }
}
