package org.alixia.javalibrary.strings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.alixia.chatroom.api.QuickList;

public final class Tools {
	/**
	 * Average run times in the <code>main</code> method below:
	 * 
	 * 1009106.54275
	 * 1025802.77775
	 * 982015.71175
	 * 974763.36975
	 * 1050773.4235
	 */
	public static boolean containsIgnoreCase(String string, String... possiblePieces) {

		Map<String, Integer> matchingPieces = new HashMap<>(0);
		List<String> standbies = new QuickList<>(possiblePieces), mute = new ArrayList<>();

		for (char c : string.toCharArray()) {
			for (Entry<String, Integer> e : matchingPieces.entrySet()) {
				char nextChar = e.getKey().charAt(e.getValue());
				if (nextChar == Character.toLowerCase(c) || nextChar == Character.toUpperCase(c)) {
					e.setValue(e.getValue() + 1);
					if (e.getValue() == e.getKey().length())
						return true;
				} else {
					mute.add(e.getKey());
				}
			}
			for (String s : standbies) {
				char firstChar = s.charAt(0);
				if (firstChar == Character.toLowerCase(c) || firstChar == Character.toUpperCase(c))
					matchingPieces.put(s, 1);
			}
			for (String s : mute) {
				standbies.add(s);
				matchingPieces.remove(s);
			}
			mute.clear();
		}

		return false;
	}

	/**
	 * Average run times in the <code>main</code> method below:
	 * 
	 * 1055767.9245
	 * 1106234.6595
	 * 1049367.86275
	 * 1068556.62675
	 * 1047758.252
	 */
	public static boolean containsIgnoreCase_Impl2(String string, String... possiblePieces) {

		Map<Integer, Integer> matchingPieces = new HashMap<>(0);
		List<Integer> standbies = new QuickList<>(), mute = new ArrayList<>();
		for (int i = 0; i < possiblePieces.length; i++)
			standbies.add(i);

		for (char c : string.toCharArray()) {
			for (Entry<Integer, Integer> e : matchingPieces.entrySet()) {
				char nextChar = possiblePieces[e.getKey()].charAt(e.getValue());
				if (nextChar == Character.toLowerCase(c) || nextChar == Character.toUpperCase(c)) {
					e.setValue(e.getValue() + 1);
					if (e.getValue() == possiblePieces[e.getKey()].length())
						return true;
				} else {
					mute.add(e.getKey());
				}
			}
			for (int s : standbies) {
				char firstChar = possiblePieces[s].charAt(0);
				if (firstChar == Character.toLowerCase(c) || firstChar == Character.toUpperCase(c))
					matchingPieces.put(s, 1);
			}
			for (int s : mute) {
				standbies.add(s);
				matchingPieces.remove(s);
			}
			mute.clear();
		}

		return false;
	}

	public static void main(String[] args) {

		long totalExecutionTime = 0;
		final int runCount = 4000;

		for (int j = 0; j < runCount; j++) {

			final long start = System.nanoTime();

			/////////////////////////////////////////////// Runtime

			for (int i = 0; i < 5000; i++)
				containsIgnoreCase("myFiLe.pnG", "pNg");

			///////////////////////////////////////////////

			final long end = System.nanoTime();
			long time = end - start;

			// The first 200 or so runthroughs seem to be unoptimized by the JIT compiler.
			// I'm excluding those so that I don't get a slur of results.
			if (j > 200)
				totalExecutionTime += time;
			System.out.println(j + ": " + time);
		}

		System.out.println("\n\nTotal Time: " + totalExecutionTime);
		System.out.println("Average Time: " + (double) totalExecutionTime / runCount);

	}
}
