package server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sun.net.httpserver.HttpExchange;
import intermediateModel.structure.ASTClass;
import intermediateModel.visitors.creation.JDTVisitor;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import server.HttpServerConverter;
import server.handler.middleware.ParsePars;
import server.handler.middleware.indexMW;
import server.handler.outputFormat.Status;
import server.helper.Answer;
import server.helper.PrepareJsonClass;
import server.helper.SHA1;
import server.indexing.DBDataJSON;
import server.indexing.MongoConnectorServer;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Giovanni Liva (@thisthatDC)
 * @version %I%, %G%
 */
public class getFile extends indexMW {
	String par1 = "path";

	String lastFileServed = "";

	private static final Logger LOGGER = LogManager.getRootLogger();

	{
		Configurator.setRootLevel( HttpServerConverter.isDebugActive() ? Level.ALL : Level.OFF);
	}



	@Override
	public void handle(HttpExchange he, Map<String, String> parameters, String name) throws IOException {
		LOGGER.debug("Request getFile on {} parameters: [{}]", name, parameters);
		//validate input
		boolean flag = true;
		if(!parameters.containsKey(par1)){
			flag = false;
		}
		if(!flag){
			ParsePars.printErrorMessagePars(he, "Expected `" + par1 + "`");
			return;
		}
		String file_path = parameters.get(par1);
		if(!ParsePars.parseFileUrl(file_path, he)){
			return;
		}
		file_path = file_path.replace("file://","");

		//get base path of project
		MongoConnectorServer mongo = MongoConnectorServer.getInstance(name);
		String base_path = mongo.getBasePath();

		String file = base_path + "/" + file_path;
		//avoid bad path
		file = file.replace("//","/");
		lastFileServed = file;


		List<ASTClass> classes = JDTVisitor.parse(file, base_path, true);
		if(classes.size() == 0){
			String response = "File not found!";
			Status s = new Status("1", response);
			ObjectMapper json = ParsePars.getOutputFormat(new HashMap<>());
			json.enable(SerializationFeature.INDENT_OUTPUT);
			response = json.writeValueAsString(s);
			Answer.SendMessage(response, he, 400);
			return;
		}

		DBDataJSON search = new DBDataJSON();
		search.setSha1(SHA1.calcate(file));
		search.setPath(file);
		List<DBDataJSON> r = mongo.getIndex(search);
		if(r.size() > 0){
			Answer.SendMessage(r.get(0).getJson(), he);
			return;
		}

		String response = "";
		try {
			response = PrepareJsonClass.json(classes, parameters, file_path, base_path);
		} catch (Exception e){
			LOGGER.catching(e);
			System.err.println(e.getMessage());
		}
		Answer.SendMessage(response, he);
	}

	@Override
	protected void printLog() {
		super.printLog();
		System.out.println("File served: " + lastFileServed);
	}
}
