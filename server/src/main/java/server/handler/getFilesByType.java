package server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sun.net.httpserver.HttpExchange;
import server.handler.middleware.ParsePars;
import server.handler.middleware.indexMW;
import server.handler.outputFormat.OutputData;
import server.helper.Answer;
import server.indexing.DBDataJSON;
import server.indexing.MongoConnectorServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Giovanni Liva (@thisthatDC)
 * @version %I%, %G%
 */
public class getFilesByType extends indexMW {

	String par = "type";

	@Override
	public void handle(HttpExchange he, Map<String, String> parameters, String name) throws IOException {
		if(!ParsePars.ParseIndexStatus(name,he)){
			return;
		}
		if(!parameters.containsKey(par)){
			ParsePars.printErrorMessagePars(he);
			return;
		}
		String type = parameters.get(par);
		MongoConnectorServer db = MongoConnectorServer.getInstance(name);
		String base_path = db.getBasePath();
		List<DBDataJSON> extend = db.getClassesExtend(type);
		List<OutputData> files = new ArrayList<>();
		for(DBDataJSON d : extend){
				files.add(new OutputData(
					d.getPath().replace(base_path, ""),
					d.getKlassName(),
					d.getPackageName()
				));
		}
		ObjectMapper json = ParsePars.getOutputFormat(parameters);
		json.enable(SerializationFeature.INDENT_OUTPUT);
		String response = json.writeValueAsString(files);
		Answer.SendMessage(response, he);

	}

}
