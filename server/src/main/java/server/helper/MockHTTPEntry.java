package server.helper;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import server.cmd.ArgumentParser;
import server.handler.openProject;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockHTTPEntry {
    HttpExchange mock = mock(HttpExchange.class);
    openProject opMock = mock(openProject.class);
    Headers mockHeader = mock(Headers.class);

    public MockHTTPEntry(ArgumentParser ap) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        if(ap.getName().length() > 0) {
            sb.append(String.format("%s=%s&", "name", ap.getName()));
        }
        if(ap.getFormat().length() > 0){
            sb.append(String.format("%s=%s&", "format", ap.getFormat()));
        }
        if(ap.getPath().length() > 0){
            sb.append(String.format("%s=%s&", "path", ap.getPath()));
        }
        String requestBody = sb.toString();

        when(mock.getRequestBody()).thenReturn(new ByteArrayInputStream(requestBody.getBytes("UTF-8")));
        when(mock.getResponseBody()).thenReturn(System.out);
        when(mock.getResponseHeaders()).thenReturn(mockHeader);

        when(opMock.doesItExists( any(String.class) )).thenReturn(false);
    }

    public HttpExchange getMock(){
        return mock;
    }
    public openProject getOPMock(){
        return opMock;
    }
}
