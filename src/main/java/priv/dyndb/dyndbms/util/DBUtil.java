package priv.dyndb.dyndbms.util;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableCollection;
import com.amazonaws.services.dynamodbv2.document.internal.IteratorSupport;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;

public class DBUtil {

    public static boolean checkIfTableExists(String tableName, AmazonDynamoDB amazonDynamoDB){
        ListTablesResult tablesResult = amazonDynamoDB.listTables();
        boolean exists = tablesResult.getTableNames().stream().anyMatch((table) -> tableName.equals(tableName));
        /*TableCollection<ListTablesResult> tableCollection= dynamoDB.listTables();
        boolean exists=false;
        IteratorSupport<Table,ListTablesResult> iteratorSupport=tableCollection.iterator();
        while (iteratorSupport.hasNext()) {
            Table table=iteratorSupport.next();
            if(table.getTableName().equals(tableName)) {
                exists=true;
                break;
            }
        }*/
        return exists;
    }
}
