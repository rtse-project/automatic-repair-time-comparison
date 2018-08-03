package debugger;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Giovanni Liva (@thisthatDC)
 * @version %I%, %G%
 */
public class ServerDebugger {



	int port = 0;
	private final ExecutorService httpThreadPool;
	HttpServer server;

	public int getPort() {
		return port;
	}



	public ServerDebugger() throws IOException {
		InetSocketAddress addr = new InetSocketAddress(findFreePort());
		server = HttpServer.create(addr, 0);
		this.port = addr.getPort();

		httpThreadPool = Executors.newFixedThreadPool(2);

		//Debug urls
		server.createContext("/", OutputLogs.getInstance());

		server.setExecutor(httpThreadPool);
		server.start();
	}

	private static int findFreePort() {
		ServerSocket socket = null;
		try {
			socket = new ServerSocket(0);
			socket.setReuseAddress(true);
			int port = socket.getLocalPort();
			try {
				socket.close();
			} catch (IOException e) {
				// Ignore IOException on close()
			}
			return port;
		} catch (IOException e) {
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
		throw new IllegalStateException("Could not find a free TCP/IP port to start embedded Jetty HTTP Server on");
	}

	public void stop() {
		System.out.println("server stopped at " + port);
		server.stop(1);
		httpThreadPool.shutdownNow();
		try {
			Thread.currentThread().sleep(1000);
		} catch (InterruptedException e) {
			//we try but who cares
		}
	}
}
