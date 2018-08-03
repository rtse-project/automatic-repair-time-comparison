package server.indexing;

import intermediateModelHelper.indexing.mongoConnector.MongoConnector;
import intermediateModelHelper.indexing.mongoConnector.MongoOptions;
import org.bson.BsonSerializationException;

import java.util.List;

public class MongoConnectorServer extends MongoConnector {
    /**
     * Protected constructor. We want to give a database as singleton.
     *
     * @param db_name Name of the DB to use
     * @param ip
     * @param port
     */
    protected MongoConnectorServer(String db_name, String ip, int port) {
        super(db_name, ip, port);
    }

    public static MongoConnectorServer getInstance(String name){
        MongoOptions options = MongoOptions.getInstance();
        MongoConnectorServer m = new MongoConnectorServer(name, options.getIp(), options.getPort());
        return m;
    }


    public synchronized void add(DBDataJSON data){
        if(existIndex(data)){
            return;
        }
        //System.out.println("Saving " + indexStructureClass.getClassName());
        try {
            datastore.save(data);
        } catch (BsonSerializationException e){
            System.err.println("File too big, cannot handle it!");
        } 
    }

    public boolean existIndex(DBDataJSON data) {
        return getIndex(data).size() > 0;
    }
    public List<DBDataJSON> getIndex(DBDataJSON data) {
        List<DBDataJSON> out =  datastore.createQuery(DBDataJSON.class)
                .field("sha1").equal(data.sha1)
                .field("path").equal(data.path)
                .asList();
        return out;
    }


    public List<DBDataJSON> getClassesWithMain() {
        List<DBDataJSON> out =  datastore.createQuery(DBDataJSON.class)
                .field("hasMain").equal("true")
                .asList();
        return out;
    }

    public List<DBDataJSON> getClassesWithThread() {
        List<DBDataJSON> out =  datastore.createQuery(DBDataJSON.class)
                .field("hasThread").equal("true")
                .asList();
        return out;
    }

    public List<DBDataJSON> getClassesExtend(String extended) {
        List<DBDataJSON> out =  datastore.createQuery(DBDataJSON.class)
                .field("extend").contains(extended)
                .asList();
        return out;
    }

}
