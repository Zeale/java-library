package zeale.apps.stuff_modules.documenters.construct_documenter.api.data.cml;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;

public class CMLWriter {
	private boolean closeWithName;

	public void setCloseWithName(boolean closeWithName) {
		this.closeWithName = closeWithName;
	}

	public boolean isCloseWithName() {
		return closeWithName;
	}

	public void writeNode(final Node node, final OutputStream output, final Charset charset) {
		final PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset));
		writeNode(node, writer, "");
		writer.flush();
	}

	private void writeNode(final Node node, final PrintWriter printer, final String indentation) {
		printer.print(indentation);
		printer.print('<');
		printer.print(node.name());
		printer.print('>');
		if (node.hasChildren()) {
			for (final Node n : node.getChildren()) {
				printer.println();
				writeNode(n, printer, "\t" + indentation);
			}
			printer.println();
			printer.print(indentation);
		} else if (node.hasValue()) {
			final String val = node.value();
			if (val.contains("\n")) {
				printer.print("\n\t");
				printer.print(indentation);
				printer.print(val);
				printer.println();
				printer.print(indentation);
			} else
				printer.print(val);
		}
		printer.print(closeWithName ? "</" + node.name() + '>' : "</>");
	}

}
