package server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sun.net.httpserver.HttpExchange;
import intermediateModelHelper.indexing.mongoConnector.MongoConnector;
import org.javatuples.Pair;
import server.handler.middleware.ParsePars;
import server.handler.middleware.indexMW;
import server.handler.outputFormat.Status;
import server.helper.Answer;
import server.indexing.IndexProjectJson;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Giovanni Liva (@thisthatDC)
 * @version %I%, %G%
 */
public class openProjectCMD extends indexMW {

	String par1 = "path";
	String par2 = "invalidCache";

	HashMap<String, Pair<Boolean,String>> indexProcess = new HashMap<>();
	public void handle(HttpExchange he, Map<String, String> parameters, String name) throws IOException {

		MongoConnector mongo = MongoConnector.getInstance(name);
		boolean flag = true;
		if(!parameters.containsKey(par1)){
			flag = false;
		}
		if(!flag){
			ParsePars.printErrorMessagePars(he, "Expected the parameter `path`");
			return;
		}
		String base_path = parameters.get(par1);
		if(!ParsePars.parseFileUrl(base_path, he)){
			return;
		}

		//if path is defined as parameter we MUST use it rewriting previous values
		if(parameters.containsKey(par1)){
			base_path = parameters.get(par1);
		}
		boolean delete = parameters.containsKey(par2) && parameters.get(par2).equals("1");
		base_path = base_path.replace("file://","");


		IndexProjectJson index = new IndexProjectJson(name);
		index.indexProject(base_path, delete);

		Status msg;
		//does the path exists?
		if (new File(base_path).exists()) {
			mongo.ensureIndexes();
			mongo.setIndexFinish();
			mongo.setBasePath(base_path);
			msg = new Status("0", "");
		} else {
			msg = new Status("1", "Path does not exists");
		}

		ObjectMapper json = ParsePars.getOutputFormat(parameters);
		json.enable(SerializationFeature.INDENT_OUTPUT);
		String response = json.writeValueAsString(msg);
		Answer.SendMessage(response, he);
	}
}
