package zeale.apps.stuff_modules.discord.bots.taige.api.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

public final class Util {

	public static String prettyPrint(Duration dur) {
		int nanoseconds = dur.getNano();
		long seconds = dur.getSeconds();
		final StringBuilder b = new StringBuilder();
		final int nan = nanoseconds % 1000, mic = (nanoseconds /= 1000) % 1000, mil = nanoseconds / 1000,
				sec = (int) (seconds % 60), mns = (int) ((seconds /= 60) % 60), hrs = (int) ((seconds /= 60) % 24),
				dys = (int) ((seconds /= 24) % 365), yrs = (int) (seconds /= 365);
		if (yrs != 0)
			b.append(yrs).append("y ");
		if (dys != 0)
			b.append(dys).append("d ");
		if (hrs != 0)
			b.append(hrs).append("h ");
		if (mns != 0)
			b.append(mns).append("m ");
		if (sec != 0)
			b.append(sec).append("s ");
		if (mil != 0)
			b.append(mil).append("ms ");
		if (mic != 0)
			b.append(mic).append("\u00b5s ");
		if (nan != 0 || b.length() == 0)
			b.append(nan).append("ns ");
		return b.toString().trim();
	}

	public static String prettyPrintBytes(long bytes) {
		final int byt = (int) (bytes % 1024), kil = (int) ((bytes /= 1024) % 1024),
				meg = (int) ((bytes /= 1024) % 1024), gig = (int) ((bytes /= 1024) % 1024),
				ter = (int) ((bytes /= 1024) % 1024), pet = (int) ((bytes /= 1024) % 1024),
				exa = (int) ((bytes /= 1024) % 1024);
		final StringBuilder b = new StringBuilder();
		if (exa != 0)
			b.append(exa).append("EiB ");
		if (pet != 0)
			b.append(pet).append("PiB ");
		if (ter != 0)
			b.append(ter).append("TiB ");
		if (gig != 0)
			b.append(gig).append("GiB ");
		if (meg != 0)
			b.append(meg).append("MiB ");
		if (kil != 0)
			b.append(kil).append("KiB ");
		if (byt != 0 || b.length() == 0)
			b.append(byt).append("B ");
		return b.toString().trim();
	}

	public static String strip(String msg) {
		return stripEveryonePings(stripHerePings(msg));
	}

	public static String stripEveryonePings(String msg) {
		return msg.replace("@everyone", "@\u200Beveryone");
	}

	public static String stripHerePings(String msg) {
		return msg.replace("@here", "@\u200Bhere");
	}

	public static InputStream toInputStream(WritableImage image, String type) throws IOException {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(SwingFXUtils.fromFXImage(image,
				new BufferedImage((int) image.getWidth(), (int) image.getHeight(), BufferedImage.TYPE_INT_ARGB)), type,
				out);
		return new ByteArrayInputStream(out.toByteArray());
	}

	public static InputStream toInputStream(WritableImage image) throws IOException {
		return toInputStream(image, "png");
	}

}
