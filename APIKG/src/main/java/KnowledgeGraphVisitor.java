import org.yaml.snakeyaml.Yaml;

import java.io.IOException;

public class KnowledgeGraphVisitor {
    public static void main(String[] args) throws IOException{
        var yaml=new Yaml();
        //使用getResourceAsStream从而动态获取某个文件
        try(var configFile=KnowledgeGraphVisitor.class.getResourceAsStream("/application.yaml")){

        }
    }
}
