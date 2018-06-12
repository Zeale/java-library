package zeale.libs.fxtools.guis;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Window<T extends Parent> extends Stage {

	protected final Scene scene;
	protected final T root;

	public Window(final T root) {
		scene = new Scene(root);
		this.root = root;
	}

	public Window(final T root, final StageStyle style) {
		this(root);
		initStyle(style);
	}

}
