package branch.alixia.kröw.unnamed.tools;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Background;
import javafx.scene.paint.Paint;

public final class BindingTools {

	public static ObjectProperty<Paint> createColorToBackgroundBinding(final ObjectProperty<Background> property) {
		final ObjectProperty<Paint> colorProperty = new SimpleObjectProperty<>();
		property.bind(Bindings.createObjectBinding(() -> UnnamedFXTools.getBackgroundFromColor(colorProperty.get()),
				colorProperty));
		return colorProperty;
	}

	private BindingTools() {
	}

}
