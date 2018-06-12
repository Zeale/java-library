package branch.alixia.kröw.unnamed;

import javafx.scene.Node;
import javafx.scene.Parent;

public final class Debug {
	public static void list(final Parent parent, final int level) {
		for (final Node n : parent.getChildrenUnmodifiable()) {
			System.out.println("Parent: " + parent + ", Node: " + n + ", Level: " + level);
			if (n instanceof Parent) {
				list((Parent) n, level + 1);
				System.out.println();
			}
		}
	}
}
