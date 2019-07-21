package org.alixia.javalibrary.networking.sockets;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;

public class Server implements Closeable {
	private final ServerSocket server;

	public Server(ServerSocket server) {
		this.server = server;
	}

	public Client acceptConnection() throws IOException {
		return new Client(server.accept());
	}

	@Override
	public void close() throws IOException {
		server.close();
	}

}
