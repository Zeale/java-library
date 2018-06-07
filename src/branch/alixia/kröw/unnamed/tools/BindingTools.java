package branch.alixia.kr�w.unnamed.tools;

import java.util.concurrent.Callable;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Background;
import javafx.scene.paint.Paint;

public final class BindingTools {

	private BindingTools() {
	}

	public static ObjectProperty<Paint> createColorToBackgroundBinding(ObjectProperty<Background> property) {
		ObjectProperty<Paint> colorProperty = new SimpleObjectProperty<>();
		property.bind(Bindings.createObjectBinding(new Callable<Background>() {

			@Override
			public Background call() throws Exception {
				return UnnamedFXTools.getBackgroundFromColor(colorProperty.get());
			}
		}, colorProperty));
		return colorProperty;
	}

}
