package server;

import server.cmd.ArgumentParser;

/**
 * @author Giovanni Liva (@thisthatDC)
 * @version %I%, %G%
 */
public class CommandLine {

	static boolean debug = false;

	public static void main(String[] args) throws Exception {
		ArgumentParser ap = ArgumentParser.parse(args);
		ap.call();
	}
}
