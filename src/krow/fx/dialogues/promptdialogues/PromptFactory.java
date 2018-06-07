package krow.fx.dialogues.promptdialogues;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import krow.fx.dialogues.promptdialogues.prompts.IncrementablePrompt;

/**
 * A class that allows the quick production and showing of prompts. A
 * {@link PromptDialogue} may be needed for some of the classes or methods in
 * this class.
 * 
 * @author Zeale
 *
 */
public final class PromptFactory {

	public static String promptString(String prompt, String hint) {

		Dialog<String> dialog = new Dialog<>();

		Text promptText = new Text(prompt);
		TextField field = new TextField();
		field.setPromptText(hint);
		Button doneButton = new Button("Continue");

		doneButton.setOnAction(event -> {
			String result = field.getText();
			dialog.setResult(result);
			dialog.close();
		});

		VBox wrapper = new VBox(5);
		wrapper.getChildren().addAll(promptText, field, doneButton);

		dialog.getDialogPane().setContent(wrapper);

		dialog.setResultConverter(param -> {
			if (!param.getButtonData().isDefaultButton())
				return null;
			return field.getText();
		});

		return dialog.showAndWait().orElse(null);

	}

	public static String promptString(String string) {
		return promptString(string, null);
	}

	/**
	 * A numerical prompt that can be placed inside a {@link PromptDialogue} and
	 * shown to a user. See {@link PromptDialogue} for more details on showing
	 * prompts.
	 * 
	 * @author Zeale
	 *
	 * @param <K>
	 *            The key type of the encapsulating {@link PromptDialogue}.
	 */
	public static class NumberPrompt<K> extends IncrementablePrompt<K, Number> {

		private DoubleProperty minValue = new SimpleDoubleProperty(Double.NaN),
				maxValue = new SimpleDoubleProperty(Double.NaN);

		public void removeMaxValue() {
			maxValue.set(Double.NaN);
		}

		public void removeMinValue() {
			minValue.set(Double.NaN);
		}

		@Override
		public void setValue(Number number) {
			if (!checkMax(number.doubleValue()))
				number = maxValue.get();
			if (!checkMin(number.doubleValue()))
				number = minValue.get();
			field.setText("" + number.doubleValue());
		}

		{
			field.setOnKeyTyped(event -> {
				for (char c : event.getCharacter().toCharArray()) {
					if (c == '.')
						if (field.getText().contains("."))
							event.consume();
						else
							;
					else if (c == '-') {
						// Cache the caret position.
						int carPos = field.getCaretPosition();

						if (field.getText().contains("-")) {
							// Remove the '-' in front.
							field.setText(field.getText().substring(1));
							field.positionCaret(carPos - 1);// A char was removed.
						} else {
							field.setText('-' + field.getText());
							field.positionCaret(carPos + 1);// A char was added.
						}

						event.consume();
					} else if (!Character.isDigit(c))
						event.consume();

				}
			});

			field.caretPositionProperty().addListener((ChangeListener<Number>) (observable, oldValue, newValue) -> {
				if (field.getText().contains("-") && newValue.intValue() < 1)
					field.positionCaret(1);
			});

			minValue.addListener((ChangeListener<Number>) (observable, oldValue, newValue) -> {
				if (newValue.doubleValue() > maxValue.get())
					minValue.set((double) oldValue);
			});

			maxValue.addListener((ChangeListener<Number>) (observable, oldValue, newValue) -> {
				if (newValue.doubleValue() < minValue.get())
					maxValue.set((double) oldValue);
			});

		}

		public NumberPrompt(PromptDialogue<? super K, ? super Number> owner, K key, String description) {
			super(owner, key, description);
		}

		public NumberPrompt(PromptDialogue<? super K, ? super Number> owner, K key, String description,
				Number defaultValue) {
			super(owner, key, description);
			setValue(defaultValue);
		}

		@Override
		public Number getValue() {
			return Double.parseDouble(field.getText());
		}

		private boolean checkMin(double number) {
			if (!Double.isNaN(minValue.get()))
				if (number < minValue.get())
					return false;
			return true;
		}

		private boolean checkMax(double number) {
			if (!Double.isNaN(maxValue.get()))
				if (number > maxValue.get())
					return false;
			return true;
		}

		@Override
		protected boolean verifyValue() {
			try {
				double value = Double.parseDouble(field.getText());

				if (!checkMin(value))
					return false;
				if (!checkMax(value))
					return false;

				return true;
			} catch (Exception e) {
				return false;
			}
		}

		@Override
		protected void increment(double amount) {
			double numb = 0;
			CALC: try {
				numb = Double.parseDouble(getText());

				if (numb >= -1 && numb <= 0.1) {
					numb = (int) numb + 1;
					break CALC;
				}

				numb += numb > 0 ? Math.pow(10, Math.floor(Math.log10(Math.abs(numb))))
						: numb > -Math.pow(10, Math.ceil(Math.log10(Math.abs(numb))) - 1) * 2
								&& numb < -Math.pow(10, Math.ceil(Math.log10(Math.abs(numb))) - 1)
										? Math.pow(10, Math.ceil(Math.log10(Math.abs(numb))) - 2)
										: Math.pow(10, Math.ceil(Math.log10(Math.abs(numb))) - 1);

			} catch (Exception e) {
			} finally {
				setValue(numb);
			}
		}

		@Override
		protected void decrement(double amount) {

			double numb = 0;
			CALC: try {
				numb = Double.parseDouble(getText());

				if (numb <= 1 && numb >= -0.1) {
					numb = (int) numb - 1;
					break CALC;
				}

				numb -= numb < 0 ? Math.pow(10, Math.floor(Math.log10(Math.abs(numb))))
						: numb < Math.pow(10, Math.ceil(Math.log10(Math.abs(numb))) - 1) * 2
								&& numb > Math.pow(10, Math.ceil(Math.log10(Math.abs(numb))) - 1)
										? Math.pow(10, Math.ceil(Math.log10(Math.abs(numb))) - 2)
										: Math.pow(10, Math.ceil(Math.log10(Math.abs(numb))) - 1);

			} catch (Exception e) {
			} finally {
				setValue(numb);
			}

		}

		public final DoubleProperty minValueProperty() {
			return this.minValue;
		}

		public final double getMinValue() {
			return this.minValueProperty().get();
		}

		public final void setMinValue(final double minValue) {
			this.minValueProperty().set(minValue);
		}

		public final DoubleProperty maxValueProperty() {
			return this.maxValue;
		}

		public final double getMaxValue() {
			return this.maxValueProperty().get();
		}

		public final void setMaxValue(final double maxValue) {
			this.maxValueProperty().set(maxValue);
		}

	}

	/**
	 * <p>
	 * A Prompt that can be placed in a {@link PromptDialogue}. Prompts will take in
	 * data from the user and return them in a {@link Map} via the attached prompt
	 * dialogue when the user is ready.
	 * <p>
	 * See {@link PromptDialogue} to build a dialogue and add Prompts, like this
	 * one, to it.
	 * 
	 * @author Zeale
	 *
	 * @param <K>
	 *            The Key type of the {@link PromptDialogue} prompt will be a part
	 *            of.
	 */
	public static class DateStringPrompt<K> extends PromptDialogue<? super K, ? super Date>.TextFieldPrompt<Date> {

		private DateFormat formatter = new SimpleDateFormat();
		private Button currentDate = new Button("•");

		// Used to display the input field and the current date button next to each
		// other.
		private HBox content = new HBox(5, field, currentDate);
		{
			addContent(content);
			currentDate.setOnAction(event -> setCurrentDate());
		}

		/**
		 * Sets the value of this {@link DateStringPrompt} as the current date.
		 */
		public void setCurrentDate() {
			setValue(new Date());
		}

		public DateStringPrompt(PromptDialogue<? super K, ? super Date> owner, K key, String description) {
			owner.super(key, description);
		}

		public DateStringPrompt(PromptDialogue<? super K, ? super Date> owner, K key, String description,
				Date defaultValue) {
			owner.super(key, description);
			setValue(defaultValue);
		}

		public DateStringPrompt(PromptDialogue<? super K, ? super Date> owner, K key, String description,
				String defaultValue) {
			owner.super(key, description);
			setValue(defaultValue);
		}

		public void setValue(String value) {
			setText(value);
		}

		@Override
		public void setValue(Date value) {
			setValue(value.toString());
		}

		@Override
		protected boolean verifyValue() {
			try {
				formatter.parse(getText());
				return true;
			} catch (ParseException e) {
				return false;
			}
		}

		@Override
		protected Date getValue() {
			try {
				return formatter.parse(getText());
			} catch (ParseException e) {
			}
			return null;
		}

	}

	/**
	 * <p>
	 * A Boolean prompt for use with a {@link PromptDialogue}. See
	 * {@link PromptDialogue} for details on creating a dialogue.
	 * <p>
	 * Creating a new instance of this prompt will require you to pass in the
	 * dialogue it belongs to. In doing so, this class's constructor will add the
	 * new {@link BooleanPrompt} instance that you've created to the prompt dialogue
	 * that you have.
	 * 
	 * @author Zeale
	 *
	 * @param <K>
	 *            The key type of this prompt.
	 */
	public static class BooleanPrompt<K> extends PromptDialogue<? super K, ? super Boolean>.Prompt<Boolean> {

		public BooleanPrompt(PromptDialogue<? super K, ? super Boolean> dialogue, K key, String description,
				Boolean defaultValue) {
			dialogue.super(key, description);

			// This must be done here because if the super class calls #setValue(Boolean
			// value) during init, #box will haven't been defined.
			setValue(defaultValue);
		}

		private final CheckBox box = new CheckBox("Select");

		{
			addContent(box);
		}

		public void setPromptText(String text) {
			box.setText(text);
		}

		public String getPromptText() {
			return box.getText();
		}

		public BooleanPrompt(PromptDialogue<? super K, ? super Boolean> dialogue, K key, String description) {
			dialogue.super(key, description);
		}

		@Override
		protected Boolean getValue() {
			return box.isSelected();
		}

		@Override
		public void setValue(Boolean value) {
			box.setSelected(value);
		}

		@Override
		protected boolean verifyValue() {
			// Return false if check box is indeterminate.
			return !box.isIndeterminate();
		}

	}

	public static class ChoicePrompt<K, I> extends PromptDialogue<? super K, ? super I>.Prompt<I> {

		public ChoicePrompt(PromptDialogue<? super K, ? super I> dialogue, K key, String description, I defaultValue) {
			dialogue.super(key, description);

			Objects.requireNonNull(defaultValue);
			selector.getSelectionModel().select(defaultValue);
		}

		public ChoicePrompt(PromptDialogue<? super K, ? super I> dialogue, K key, String description) {
			dialogue.super(key, description);
		}

		private final ChoiceBox<I> selector = new ChoiceBox<>(FXCollections.observableArrayList());

		{
			addContent(selector);
		}

		/**
		 * Constructs a new {@link ChoicePrompt} with the specified items, (in
		 * <code>possibleValues</code>), as options. <code>defaultValue</code> does not
		 * have to be inside the list of possible values for it to be selected, and it
		 * doesn't have to be an option for the user to be selected by default. This is
		 * due to the underlying use of a {@link CheckBox} for a selection UI.
		 * 
		 * @param dialogue
		 *            The {@link PromptDialogue} that this prompt will be added to.
		 * @param key
		 *            The key for the return value of this prompt. Use this to get
		 *            whatever this prompt returns from the map received after the
		 *            window is closed.
		 * @param description
		 *            A description of this prompt. It will be displayed above this
		 *            prompt's content, (AKA above this prompt's {@link ChoiceBox}).
		 * @param defaultValue
		 *            The initially selected, default value of the prompt.
		 * @param possibleValues
		 *            Other values that the user can select. These values are all
		 *            options in the {@link ChoiceBox} displayed by this prompt.
		 */
		@SafeVarargs
		public ChoicePrompt(PromptDialogue<? super K, ? super I> dialogue, K key, String description, I defaultValue,
				I... possibleValues) {
			dialogue.super(key, description);

			Objects.requireNonNull(defaultValue);
			selector.getSelectionModel().select(defaultValue);

			for (I i : possibleValues)
				addPossibleValue(i);

		}

		@SafeVarargs
		public ChoicePrompt(PromptDialogue<? super K, ? super I> dialogue, K key, String description,
				I... possibleValues) {
			dialogue.super(key, description);

			for (I i : possibleValues)
				addPossibleValue(i);
		}

		public void addPossibleValue(I value) {
			Objects.requireNonNull(value);
			if (selector.getItems().contains(value))
				return;
			selector.getItems().add(value);
		}

		@Override
		protected I getValue() {
			return selector.getValue();
		}

		@Override
		public void setValue(I value) {
			Objects.requireNonNull(value);
			selector.setValue(value);
		}

		@Override
		protected boolean verifyValue() {
			// If nothing is in the list of selected items, return false.
			// Note that, with a CheckBox, this "list" is of size 1.
			return !selector.getSelectionModel().isEmpty();
		}

	}

}
