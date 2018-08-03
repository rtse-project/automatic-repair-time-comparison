package debugger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OutputLogs implements HttpHandler {
    private static OutputLogs instance = null;
    private final static int _MAX = 50;
    private String[] logs = new String[_MAX];
    private int position = 0;
    private static String name = "";
    private static String scripts = "<script>setTimeout(function(){\n" +
            "   window.location.reload(1);\n" +
            "}, 5000);\n" +
            "var textarea = document.getElementById('t');\n" +
            "textarea.scrollTop = textarea.scrollHeight;\n</script>";
    private static String header = "<html><head><title>" + name + "</title></head><body><textarea id=\"t\" style=\"width:100%;height:100%;\">";

    public static synchronized OutputLogs getInstance(){
        if(instance == null) {
            instance = new OutputLogs();
        }
        return instance;
    }

    public void add(String msg){
        logs[position] = msg;
        position++;
        if(position >= _MAX){
            position = 0;
        }
    }

    @Override
    public void handle(HttpExchange he) throws IOException {
        Map<String, String> parameters = new HashMap<>();
        String query = he.getRequestURI().getQuery();
        parseQuery(query, parameters);
        StringBuilder sb = new StringBuilder();
        sb.append(header);
        for(int i = 0; i < logs.length; i++){
            if(logs[i] != null)
                sb.append(logs[i]);
            if(i == (position-1)){
                sb.append("\n________\n");
            }
            sb.append("\n");
        }
        sb.append("</textarea>");
        if(parameters.containsKey("r"))
            sb.append(scripts);
        sb.append("</body></html>");
        String response = sb.toString();
        he.sendResponseHeaders(400, response.length());
        OutputStream os = he.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }

    public void setName(String n) {
        name = n;
        header = "<html><head><title>" + name + "</title></head><body><textarea id=\"t\" style=\"width:100%;height:100%;\">";
    }

    public static void parseQuery(String query, Map<String,String> parameters) throws UnsupportedEncodingException {

        if (query != null) {
            String pairs[] = query.split("[&]");
            for (String pair : pairs) {
                String param[] = pair.split("[=]");
                String key = null;
                String value = null;
                if (param.length > 0) {
                    key = URLDecoder.decode(param[0],
                            System.getProperty("file.encoding"));
                }

                if (param.length > 1) {
                    value = URLDecoder.decode(param[1],
                            System.getProperty("file.encoding"));
                }

                if (parameters.containsKey(key)) {
                    Object obj = parameters.get(key);
                    if (obj instanceof List<?>) {
                        List<String> values = (List<String>) obj;
                        values.add(value);

                    }
                } else {
                    parameters.put(key, value);
                }
            }
        }
    }

}
