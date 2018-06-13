package org.alixia.javalibrary.strings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.alixia.chatroom.api.QuickList;

public final class Tools {
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

}
