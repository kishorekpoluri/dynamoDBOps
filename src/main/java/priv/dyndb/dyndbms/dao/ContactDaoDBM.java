package priv.dyndb.dyndbms.dao;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import priv.dyndb.dyndbms.item.Contact;
import priv.dyndb.dyndbms.util.DBUtil;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class ContactDaoDBM {

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @Autowired
    private DynamoDB dynamoDB;

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    private static final String tableName = "CONTACT";

    @PostConstruct
    public void postConstruct() {
        //deleteTable();
        createTable();
        //list();
    }

    public void deleteTable(){
        Table table=dynamoDB.getTable(tableName);
        table.delete();
    }

    private void createTable() {
        boolean exists= DBUtil.checkIfTableExists(tableName,amazonDynamoDB);
        if (!exists) {
            try {
                CreateTableRequest createTableRequest = dynamoDBMapper.generateCreateTableRequest(Contact.class);
                createTableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
                dynamoDB.createTable(createTableRequest);
                createInitialData();
            }catch (AmazonServiceException e) {
                System.out.println(e.getErrorMessage());
            }

        }
    }

    public void createInitialData() {
        try {
            Contact contact1=new Contact();
            contact1.setName("John Doe");
            contact1.setPhone("123-456-7890");
            contact1.setEmail("johndoe@gmail.com");
            dynamoDBMapper.save(contact1);
            Contact contact2=new Contact();
            contact2.setName("Jane Doe");
            contact2.setPhone("123-455-7890");
            contact2.setEmail("janedoe@gmail.com");
            dynamoDBMapper.save(contact2);

        } catch (Exception e) {
            System.err.println("Create items failed.");
            System.err.println(e.getMessage());

        }
    }

    public List<Contact> list(){
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        PaginatedScanList<Contact> paginatedScanList=dynamoDBMapper.scan(Contact.class,scanExpression);
        Iterator<Contact> contactIterator=paginatedScanList.iterator();
        List<Contact> list = new ArrayList<>();
        contactIterator.forEachRemaining(list::add);
        return list;
        /*while(contactIterator.hasNext()){
            Contact contact=contactIterator.next();
            System.out.println(contact.getName());
        }*/
    }

    public void add(Contact contact) {
        try {
            dynamoDBMapper.save(contact);
        } catch (Exception e) {
            System.err.println("Create items failed.");
            System.err.println(e.getMessage());
        }
    }

    public void delete(String name) {
        try {
            Contact contact=new Contact();
            contact.setName(name);
            dynamoDBMapper.delete(contact);
        } catch (Exception e) {
            System.err.println("Create items failed.");
            System.err.println(e.getMessage());
        }
    }

}
