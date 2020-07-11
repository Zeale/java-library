package zeale.apps.stuff.api.chatroom.events;

public interface EventSystem<E extends Event> {
	EventManager<E> getEventManager();
}
