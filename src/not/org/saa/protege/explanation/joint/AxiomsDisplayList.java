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
		JLabel label = new JLabel("Explanation " + explanationNumber);
		headerPanel.add(label);
		add(headerPanel, BorderLayout.NORTH);

		JPanel displayHolder = new JPanel(new BorderLayout());
		Border marginBorder = BorderFactory.createEmptyBorder(0, 20, 0, 0);
		Border lineBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
		displayHolder.setBorder(BorderFactory.createCompoundBorder(marginBorder, lineBorder));
		displayHolder.add((JComponent) display);
		add(displayHolder);
	}

	public void dispose() {
		display.dispose();
	}

}
