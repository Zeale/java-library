package krow.fx.dialogues.promptdialogues;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import main.alixia.javalibrary.javafx.tools.FXTools;
import zeale.libs.fxtools.fixes.ScrollPaneBackgroundFixer;

/**
 * <p>
 * Opens a prompt dialogue with multiple {@link TextField}s for the user to
 * submit values. When building this dialogue, the programmer can add fields for
 * the user to submit data to. Each field will have a Key, and a description.
 * The Key is of the type of the type parameter K, and the description is a
 * description of what the user should type into the text field. The programmer
 * may also supply an <i>optional</i> String called the <code>hint</code>. The
 * hint is the gray PromptText that shows up in the {@link TextField}. See
 * {@link TextField#promptTextProperty()}.
 * <p>
 * The prompt is shown in a <code>showAndWait</code> manner, meaning that the
 * call to show the {@link PromptDialogue} will block until the user exits the
 * dialogue.
 *
 * @author Zeale
 *
 * @param <K>
 *            The type of the Key that will be used to obtain a text field's
 *            value in the returned map.
 * @param <V>
 *            The type of Values stored in the returned map. If the returned map
 *            will store Doubles, this should be {@link Double}. If the returned
 *            map will store Integers, this should be {@link Integer}. If the
 *            returned map will store both Doubles and Integers as values,
 *            &lt;V&gt; should be the most immediate superclass of the two, so
 *            {@link Number}. This may often be {@link Object}.
 */
public class PromptDialogue<K, V> extends Dialog<Map<K, V>> {

	public class BasicPrompt extends PromptDialogue<? super K, ? super String>.TextFieldPrompt<String> {

		// This will become abstract, to follow the change described right above the
		// class declaration. Next commit :D

		public BasicPrompt(final K key, final String description) {
			super(key, description);
		}

		public BasicPrompt(final K key, final String description, final String hint) {
			this(key, description);
			setHint(hint);
		}

		public BasicPrompt(final K key, final String description, final String hint, final String defaultValue) {
			this(key, description);
			setHint(hint);
			setValue(defaultValue);
		}

		@Override
		protected String getValue() {
			return field.getText();
		}

		public void setHint(final String text) {
			field.setPromptText(text);
		}

		@Override
		public void setValue(final String value) {
			field.setText(value);
		}

		@Override
		protected boolean verifyValue() {
			return !getValue().isEmpty();
		}

	}

	/**
	 * A superclass for Prompts used by this {@link PromptDialogue}. When a
	 * {@link Prompt} is created, it automatically adds itself to its parent
	 * {@link PromptDialogue} [defined in this class].
	 *
	 * @author Zeale
	 *
	 * @param <PV>
	 *            <p>
	 *            The return value of this Prompt.
	 *            <p>
	 *            This does NOT have to match the return type of the parent
	 *            {@link PromptDialogue}. For example, a {@link PromptDialogue} that
	 *            has a &lt;V&gt; of {@link Number} can have two prompts.
	 *            <ul>
	 *            <li>The first prompt can have a &lt;PV&gt; of {@link Double},</li>
	 *            <li>and the second prompt can have a &lt;PV&gt; of
	 *            {@link Integer}.</li>
	 *            <ul>
	 *            <p>
	 *            Simply put &lt;PV&gt; must be a subtype of &lt;V&gt; or must be
	 *            the same as &lt;V>.
	 */
	public abstract class Prompt<PV extends V> extends VBox {

		private K key;

		private final Text fieldDescription = new Text();

		private boolean required;

		{
			addPrompt(this);
			getChildren().add(fieldDescription);

			// A bit of *default* styling.
			setAlignment(Pos.CENTER);
			setBackground(FXTools.getBackgroundFromColor(Color.GRAY));
			setPadding(new Insets(10, 15, 10, 15));
			setSpacing(20);
			setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2))));
		}

		public Prompt(final K key, final String description) {
			if (!setKey(key))
				throw new RuntimeException("Invalid key.");
			setDescription(description);
		}

		public Prompt(final K key, final String description, final PV defaultValue) {
			this(key, description);
			setValue(defaultValue);
		}

		protected final void addContent(final Node content) {
			getChildren().add(content);
		}

		private K getKey() {
			return key;
		}

		protected abstract PV getValue();

		private void highlightDescription(final Color color) {
			fieldDescription.setFill(color);
			fieldDescription.setFont(Font.font(13.2));
		}

		public final boolean isRequired() {
			return required;
		}

		/**
		 * Cannot be undone.
		 */
		public final void remove() {
			removePrompt(this);
		}

		protected final void removeContent(final Node content) {
			getChildren().remove(content);
		}

		private void removeDescHighlight() {
			fieldDescription.setFill(Color.BLACK);
			fieldDescription.setFont(Font.getDefault());
		}

		public final void setDescription(final String text) {
			fieldDescription.setText(text);
		}

		/**
		 * Attempts to set this prompt's key.
		 *
		 * @param key
		 *            The new key.
		 * @return <code>true</code> if this succeeded, <code>false</code> otherwise.
		 */
		public final boolean setKey(final K key) {
			if (keyTaken(key))
				return false;
			this.key = key;
			return true;
		}

		public final void setRequired(final boolean required) {
			this.required = required;
		}

		public abstract void setValue(PV value);

		/**
		 * <p>
		 * This method is called by a {@link PromptDialogue} when the user pushes the
		 * <code>continue</code> button on each of the dialogue's prompts that are
		 * {@link #required}. This method should return <code>true</code> if this Prompt
		 * has a value that is acceptable.
		 * <p>
		 * If this method returns false when it is called by the PromptDialogue, then
		 * the dialogue will remain hidden and this Prompt's description will become
		 * red.
		 *
		 * @return <code>true</code> if the value is acceptable, <code>false</code>
		 *         otherwise.
		 */
		protected abstract boolean verifyValue();
	}

	public abstract class TextFieldPrompt<PV extends V> extends Prompt<PV> {

		protected final TextField field = new TextField();

		{
			addContent(field);
		}

		public TextFieldPrompt(final K key, final String description) {
			super(key, description);
		}

		public TextFieldPrompt(final K key, final String description, final PV defaultValue) {
			super(key, description, defaultValue);
		}

		protected String getText() {
			return field.getText();
		}

		protected void setText(final String text) {
			field.setText(text);
		}

	}

	// Will contain the scrollPort and the doneButton.
	private final VBox fullContent = new VBox(20);

	// Will contain each individual prompt.
	private final VBox promptWrapper = new VBox(15);

	// Will contain the promptWrapper, allowing the user to scroll down if there are
	// an exceedingly large amount of prompts. :)
	private final ScrollPane scrollPort = new ScrollPane(promptWrapper);

	// The button that the user will push when they are ready to submit all the
	// values.
	private final Button doneButton = new Button("Continue");

	private final ObservableList<Prompt<? extends V>> basicPrompts = FXCollections.observableArrayList();

	{

		// Add doneButton and scrollPort to the fullContent.
		fullContent.getChildren().addAll(scrollPort, doneButton);

		// Set this Dialog's content as fullContent.
		getDialogPane().setContent(fullContent);

		doneButton.setOnAction(event -> {
			final Map<K, V> values = new HashMap<>();

			boolean requiredPromptsFilled = true;
			for (final Prompt<? extends V> p : basicPrompts)
				// If this prompt's input isn't acceptable
				if (!p.verifyValue()) {
					// AND if the prompt is required
					if (p.isRequired()) {
						// Notify the user (colored description)
						p.highlightDescription(Color.RED);
						// Make sure that we don't close the prompt dialogue and give the calling
						// program a value.
						requiredPromptsFilled = false;
					} else
						// So we notify the user but with orange, not red.
						p.highlightDescription(Color.DARKORANGE);
					// The orange coloring will only show if there are any necessary prompts that
					// aren't filled as well, since this code will close the prompt window if all
					// necessary prompts are filled. The user won't be able to see the color once
					// the prompt closes.
				}

				// This prompt's value is acceptable.
				else {
					p.removeDescHighlight();
					// So if required prompts are filled so far in the loop,
					if (requiredPromptsFilled)
						// Then we can continue to build the map that we will return.
						values.put(p.getKey(), p.getValue());
				}

			// If they haven't filled out required prompts then don't close the prompt;
			// we're not done here.
			if (!requiredPromptsFilled)
				return;

			setResult(values);

			// This will return the showAndWait() call.
			close();
		});

		// Stylize and space out the dialogue.
		promptWrapper.setFillWidth(true);
		promptWrapper.setPrefWidth(500);
		promptWrapper.setAlignment(Pos.CENTER);

		// A bit of default styling. :)
		fullContent.setBackground(null);
		fullContent.setFillWidth(true);
		fullContent.setAlignment(Pos.CENTER);

		promptWrapper.setBackground(null);
		fullContent.setBackground(null);
		scrollPort.setBackground(null);
		setFill(Color.DARKGRAY);

		// Scroll pane transparent bg fix:
		scrollPort.getStylesheets().add(ScrollPaneBackgroundFixer.getBackgroundFixerLoc());

	}

	private final Collection<K> keys = new LinkedList<>();

	public PromptDialogue() {
	}

	public PromptDialogue(final String title) {
		setTitle(title);
	}

	private void addPrompt(final Prompt<? extends V> prompt) {
		if (basicPrompts.contains(prompt))
			return;
		basicPrompts.add(prompt);
		promptWrapper.getChildren().add(prompt);
	}

	public final double getMaxHeight() {
		return fullContent.getMaxHeight();
	}

	public final double getMaxWidth() {
		return fullContent.getMaxWidth();
	}

	public final double getMinHeight() {
		return fullContent.getMinHeight();
	}

	public final double getMinWidth() {
		return fullContent.getMinWidth();
	}

	private boolean keyTaken(final K key) {
		return keys.contains(key);
	}

	public final DoubleProperty maxHeightProperty() {
		return fullContent.maxHeightProperty();
	}

	public final DoubleProperty maxWidthProperty() {
		return fullContent.maxWidthProperty();
	}

	public final DoubleProperty minHeightProperty() {
		return fullContent.minHeightProperty();
	}

	public final DoubleProperty minWidthProperty() {
		return fullContent.minWidthProperty();
	}

	private void removePrompt(final Prompt<? extends V> p) {
		while (basicPrompts.contains(p))
			basicPrompts.remove(p);
		while (promptWrapper.getChildren().contains(p))
			promptWrapper.getChildren().remove(p);
	}

	public void setFill(final Color color) {
		getDialogPane().setBackground(FXTools.getBackgroundFromColor(color));
	}

	public final void setMaxHeight(final double value) {
		fullContent.setMaxHeight(value);
	}

	public void setMaxSize(final double maxWidth, final double maxHeight) {
		fullContent.setMaxSize(maxWidth, maxHeight);
	}

	public final void setMaxWidth(final double value) {
		fullContent.setMaxWidth(value);
	}

	public final void setMinHeight(final double value) {
		fullContent.setMinHeight(value);
	}

	public void setMinSize(final double minWidth, final double minHeight) {
		fullContent.setMinSize(minWidth, minHeight);
	}

	public final void setMinWidth(final double value) {
		fullContent.setMinWidth(value);
	}
}
