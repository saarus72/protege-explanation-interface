package not.org.saa.protege.explanation.joint;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owl.explanation.api.ExplanationGenerator;
import org.semanticweb.owl.explanation.api.ExplanationProgressMonitor;
import org.semanticweb.owlapi.model.OWLAxiom;

import not.org.saa.protege.explanation.joint.service.JustificationComputation;

/**
 * Author: Matthew Horridge
 * Stanford University
 * Bio-Medical Informatics Research Group
 * Date: 20/03/2012
 */

public class ExplanationGeneratorProgressDialog extends JDialog {
	private AxiomsProgressPanel panel;

	public ExplanationGeneratorProgressDialog(Frame owner, JustificationComputation computation) {
		super(owner, "Computing explanations", true);
		panel = new AxiomsProgressPanel(computation);
		setContentPane(panel);
		pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension dlgSize = getSize();
		setLocation(screenSize.width / 2 - dlgSize.width / 2, screenSize.height / 2 - dlgSize.height / 2);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}

	public void reset() {
		panel.reset();
	}


	public void setExplanationCount(int count) {
		panel.setExplanationCount(count);
	}
}
