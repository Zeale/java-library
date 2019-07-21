package org.alixia.javalibrary.networking.sockets;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class Client implements Closeable {
	private Socket socket;
	private final ObjectInputStream in;
	private final ObjectOutputStream out;

	private static enum CommunicationCommands {
		CONNECTION_CHECK, COMMUNICATION_RESPONSE;
	}

	/**
	 * Sends the specified {@link Serializable} over the connection.
	 * 
	 * @param item The {@link Serializable} to send.
	 * @return <code>true</code> if no {@link IOException} was thrown while
	 *         attempting to write the specified object, <code>false</code>
	 *         otherwise.
	 */
	public boolean send(Serializable item) {
		try {
			out.writeObject(item);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Sends a "test" object. If a {@link Client}, like this, is on the other end,
	 * it should ignore the object. This allows the user to check if a connection is
	 * ok, since an exception will be thrown if it isn't.
	 * 
	 * @param item
	 * @return
	 */
	public boolean testConnection() {
		return send(CommunicationCommands.CONNECTION_CHECK);
	}

	public boolean isConnected() {
		return socket != null;
	}

	/**
	 * Creates a {@link Client} object out of the connected socket.
	 * 
	 * @param socket The socket to use for the client.
	 * @throws IOException In case {@link Socket#getOutputStream()} or
	 *                     {@link Socket#getInputStream()} throws an
	 *                     {@link IOException}, or
	 */
	public Client(Socket socket) throws IOException {
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream((this.socket = socket).getInputStream());
	}

	/**
	 * Closes this {@link Client}'s underlying {@link Socket}.
	 * 
	 * @throws IOException As specified by {@link Socket#close()}.
	 */
	public void close() throws IOException {
		if (socket == null)
			return;
		socket.close();
		socket = null;
	}

	/**
	 * Blocks until a {@link Serializable} is received from the {@link #out internal
	 * ObjectInputStream hooked up to the Socket}. This method will return
	 * <code>null</code> if an {@link IOException} is thrown by the underlying
	 * {@link ObjectInputStream}. As soon as this happens, this {@link Client}
	 * should be considered closed.
	 * 
	 * @return The next {@link Serializable} received.
	 * @throws ClassNotFoundException As specified by
	 *                                {@link ObjectInputStream#readObject()}.
	 */
	@SuppressWarnings("incomplete-switch")
	public Serializable read() throws ClassNotFoundException {
		try {
			Object readObject = in.readObject();
			while (readObject instanceof CommunicationCommands) {
				switch ((CommunicationCommands) readObject) {
//				case CONNECTION_CHECK:
//					send(CommunicationCommands.COMMUNICATION_RESPONSE);
				// This will be dealt with later.
				}
				readObject = in.readObject();
			}
			return (Serializable) readObject;
		} catch (IOException e) {
			return null;
		}
	}

}
