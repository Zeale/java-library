package org.alixia.javalibrary.networking.sockets;

import java.io.Closeable;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

import org.alixia.javalibrary.util.Box;

/**
 * <p>
 * The {@link Client} class provides reading and writing {@link Serializable}
 * objects over a {@link Socket}. It can be paired with a {@link Server} to make
 * the development of connections that communicate with {@link Serializable}
 * objects easy.
 * </p>
 * <p>
 * If attempting unsupported/lower-level communication with this class, please
 * note that this class creates a new {@link ObjectOutputStream} over its given
 * {@link Socket}'s {@link Socket#getOutputStream() output stream}, and then
 * creates a new {@link ObjectInputStream} over the {@link Socket}'s
 * {@link Socket#getInputStream() input stream}. These two operations
 * <q>[write] the serialization stream header to the underlying stream</q>, and
 * expect the same stream header from the other end of the connection,
 * respectively.
 * </p>
 * 
 * @author Zeale
 *
 */
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
			out.flush();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Sends a "test" object. If a {@link Client}, like this, is on the other end,
	 * it should ignore the object. This allows the user to check if a connection is
	 * ok, since an exception will be thrown if it isn't.
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
		out.flush();
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

	public synchronized Box<Serializable> read(int millisTimeout) throws ClassNotFoundException, IOException {
		socket.setSoTimeout(millisTimeout);
		long startTime = System.currentTimeMillis();
		try {
			Object readObject = in.readObject();
			while (readObject instanceof CommunicationCommands) {
				// Handle CommunicationCommands here.

				long remainingTime = millisTimeout - System.currentTimeMillis() + startTime;
				if (remainingTime < 0)
					throw new InterruptedIOException();
				socket.setSoTimeout((int) remainingTime);
				readObject = in.readObject();
			}
			return new Box<>((Serializable) readObject);
		} catch (InterruptedIOException e) {
			return null;
		} finally {
			socket.setSoTimeout(0);
		}
	}

	/**
	 * <p>
	 * Blocks until a {@link Serializable} piece of data is received from the
	 * {@link #out internal ObjectInputStream hooked up to the Socket}.
	 * </p>
	 * <p>
	 * Please note that: <b>All {@link Exception}s are fatal to this {@link Client}
	 * object</b> as they may leave the {@link Client}'s underlying {@link #in input
	 * stream} in an indeterminate state.
	 * </p>
	 * 
	 * @return The next {@link Serializable} received.
	 * @throws ClassNotFoundException As specified by
	 *                                {@link ObjectInputStream#readObject()}.
	 * @throws IOException            If an {@link IOException} occurs while reading
	 *                                the object.
	 */
	public synchronized Serializable read() throws ClassNotFoundException, IOException {
		Object readObject = in.readObject();
		while (readObject instanceof CommunicationCommands) {
//			switch ((CommunicationCommands) readObject) {
//				case CONNECTION_CHECK:
//					send(CommunicationCommands.COMMUNICATION_RESPONSE);
			// This will be dealt with later.
//			}
			readObject = in.readObject();
		}
		return (Serializable) readObject;
	}

}
