package gartham.apps.garthchat.api.communication.common.gids;

import java.util.Arrays;
import java.util.Random;

import org.alixia.javalibrary.JavaTools;

public class GIDProvider {
	private volatile long time;
	private int count;

	byte[] genbytes() {
		long timeMillis = System.currentTimeMillis();
		if (timeMillis == time) {
			synchronized (GID.class) {
				count++;
			}
		} else {
			synchronized (GID.class) {
				count = 0;
			}
			time = timeMillis;
		}
		int x;
		synchronized (GID.class) {
			x = count;
		}

		byte[] b = Arrays.copyOf(JavaTools.longToBytes(timeMillis), 20);
		System.arraycopy(JavaTools.intToBytes(x), 0, b, 8, 4);
		System.arraycopy(randomBytes(8), 0, b, 12, 8);
		return b;
	}

	public GID generateGid() {
		return new GID(genbytes());
	}

	private static byte[] randomBytes(int len) {
		byte[] b = new byte[len];
		new Random().nextBytes(b);
		return b;
	}
}
