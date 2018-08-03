package server.indexing;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;
import org.mongodb.morphia.utils.IndexType;

import java.util.List;

@Entity("DBDataJSON")
@Indexes(
        @Index(fields = @Field(value = "$**", type = IndexType.TEXT))
)
public class DBDataJSON {
    @Id
    private ObjectId id;
    String path;
    String json;
    String sha1;
    String hasMain;
    String hasThread;
    String klassName;
    String packageName;
    List<String> extend;


    public DBDataJSON() {
    }

    public List<String> getExtend() {
        return extend;
    }

    public void setExtend(List<String> extend) {
        this.extend = extend;
    }

    public DBDataJSON(ObjectId id, String path, String json, String sha1, String hasMain, String hasThread, String klassName, String packageName, List<String> extend) {
        this.id = id;
        this.path = path;
        this.json = json;
        this.sha1 = sha1;
        this.hasMain = hasMain;
        this.hasThread = hasThread;
        this.klassName = klassName;
        this.packageName = packageName;
        this.extend = extend;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public String getHasMain() {
        return hasMain;
    }

    public void setHasMain(String hasMain) {
        this.hasMain = hasMain;
    }


    public String getHasThread() {
        return hasThread;
    }

    public void setHasThread(String hasThread) {
        this.hasThread = hasThread;
    }

    public String getKlassName() {
        return klassName;
    }

    public void setKlassName(String klassName) {
        this.klassName = klassName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
