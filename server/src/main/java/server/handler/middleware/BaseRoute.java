package server.handler.middleware;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import server.Config;
import server.HttpServerConverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by giovanni on 03/03/2017.
 */
public abstract class BaseRoute implements HttpHandler {

    public BaseRoute(){
    }

    public void handle(HttpExchange he) throws IOException {
        if(Config.isDebug())
            this.printBaseLog(he);
        handleConnection(he);
        if(Config.isDebug()){
            printLog();
            System.out.println("======= " + Config.getRandomFace() + " ========");
        }
    }

    private void printBaseLog(HttpExchange he) {
        String route =  getClass().getCanonicalName();
        System.out.println(
            String.format("Requested: %s\nServed by: %s",
                    he.getHttpContext().getPath(),
                    route
            ));
        //System.out.println(HttpServerConverter.getLastParameters().toString());
    }

    protected abstract void handleConnection(HttpExchange he) throws IOException;

    protected void printLog() {
        String route =  getClass().getSimpleName();
        System.out.print("Serving " + route + " :: ");
        System.out.println(HttpServerConverter.getLastParameters().toString());

    }
}
