package priv.dyndb.dyndbms.config;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

@Configuration
public class DBConfig {

    @Value("${jsa.aws.access_key_id}")
    private String awsId;

    @Value("${jsa.aws.secret_access_key}")
    private String awsKey;

    private AmazonDynamoDB amazonDynamoDB;

    /*@Value("${jsa.s3.region}")
    private String region;*/

    @Bean
    public AmazonDynamoDB  amazonDynamoDB() throws AmazonClientException {
        try {
            amazonDynamoDB = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
                    new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
                    .withCredentials(new AWSStaticCredentialsProvider
                            (new BasicAWSCredentials("","")))
                    .build();
        }catch (Exception e){
            e.printStackTrace();
        }
        return amazonDynamoDB;
    }

    @Bean
    public DynamoDB  dynamoDB() {
        return new DynamoDB(amazonDynamoDB);
    }
}
