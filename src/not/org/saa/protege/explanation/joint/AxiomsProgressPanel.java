package not.org.saa.protege.explanation.joint;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import not.org.saa.protege.explanation.joint.service.JustificationComputation;

public class AxiomsProgressPanel extends JPanel {

	private JLabel messageLabel;

	private int numberFound = 0;

	private static final String MESSAGE = "Computing explanations. Found ";

	private Action cancelAction;
	
	private JustificationComputation computation;

	/**
	 * Creates a new <code>JPanel</code> with a double buffer and a flow layout.
	 */
	public AxiomsProgressPanel(JustificationComputation computation) {
		this.computation = computation;		
		setLayout(new BorderLayout(12, 12));
		setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		setPreferredSize(new Dimension(400, 100));
		JPanel progressPanel = new JPanel(new BorderLayout(3, 3));
		add(progressPanel, BorderLayout.NORTH);
		messageLabel = new JLabel(MESSAGE + "0  ");
		progressPanel.add(messageLabel, BorderLayout.NORTH);
		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		progressPanel.add(progressBar, BorderLayout.SOUTH);
		cancelAction = new AbstractAction("Stop searching") {
			public void actionPerformed(ActionEvent e) {
				//cancelled = true;
				computation.interruptComputation();
				setEnabled(false);
			}
		};
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		add(buttonPanel, BorderLayout.SOUTH);
		buttonPanel.add(new JButton(cancelAction));
	}

	public void reset() {
		numberFound = 0;
		cancelAction.setEnabled(true);
	}

	public void setExplanationCount(int count) {
		numberFound = count;
		Runnable runnable = () -> messageLabel.setText(MESSAGE + numberFound);
		if (SwingUtilities.isEventDispatchThread()) {
			runnable.run();
		} else {
			SwingUtilities.invokeLater(runnable);
		}
	}
}
