package not.org.saa.protege.explanation.joint;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.protege.editor.core.Disposable;

/**
 * Author: Matthew Horridge
 * The University Of Manchester
 * Information Management Group
 * Date: 19-Oct-2008
 */

public class AxiomsDisplayList extends JPanel implements Disposable {

	private AxiomsDisplay display;
	private int explanationNumber;

	public AxiomsDisplayList(AxiomsDisplay display, int explanationNumber) {
		this.display = display;
		this.explanationNumber = explanationNumber;
		createUI();
	}

	private void createUI() {
		setLayout(new BorderLayout(2, 2));

		JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel label = new JLabel("Justification " + explanationNumber);
		headerPanel.add(label);
		add(headerPanel, BorderLayout.NORTH);

		JPanel displayHolder = new JPanel(new BorderLayout());
		displayHolder.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		displayHolder.add((JComponent) display);
		add(displayHolder);
	}

	public void dispose() {
		display.dispose();
	}

}
