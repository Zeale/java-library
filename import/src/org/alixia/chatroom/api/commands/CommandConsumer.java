package org.alixia.chatroom.api.commands;

public abstract class CommandConsumer {

	public abstract boolean consume(String command, String... args);

}
