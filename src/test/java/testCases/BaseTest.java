package testCases;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class BaseTest {
    String result;
    FileInputStream fileInputStream;
    Properties properties;


    public String getData(String key){
        try{
            String filePath = System.getProperty("user.dir")+"\\src\\test\\resources\\config.properties";
            fileInputStream = new FileInputStream(new File(filePath));
            properties = new Properties();
            if(fileInputStream != null){
                properties.load(fileInputStream);
                result = properties.getProperty(key);
            }else
                System.out.println("File is not available");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

}
