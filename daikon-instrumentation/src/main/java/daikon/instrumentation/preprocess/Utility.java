package daikon.instrumentation.preprocess;

/**
 * Created by giovanni on 28/04/2017.
 */
public class Utility {
    public static String convertJavaToPkgName(String className){
        return className.replace("/",".");
    }
    public static String convertPkgNametoJava(String className){
        String out = className.replace(".","/");
        if(out.lastIndexOf("/") == -1)
            return out;
        String lastPart = out.substring(out.lastIndexOf("/"));
        if(Character.isUpperCase(lastPart.charAt(0))){
            out = out.substring(0, out.lastIndexOf("/")) + "$" +lastPart;
        }
        return out;
    }
}
