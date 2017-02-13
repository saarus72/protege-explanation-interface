package not.org.saa.protege.explanation.joint;

import org.protege.editor.owl.ui.explanation.io.InconsistentOntologyPluginInstance;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.OWLModelManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Author: Matthew Horridge
 * Stanford University
 * Bio-Medical Informatics Research Group
 * Date: 18/03/2012
 */

public class InconsistentOntologyPresentationService implements InconsistentOntologyPluginInstance {

	private OWLEditorKit editorKit;
	private JustificationComputationServiceManager manager;

	@Override
	public void setup(OWLEditorKit editorKit) {
		this.editorKit = editorKit;
	}

	@Override
	public void initialise() throws Exception {
		manager = new JustificationComputationServiceManager(editorKit, "not.org.saa.protege.explanation.joint", "JustificationService");
	}

	@Override
	public void dispose() throws Exception {
	}

	@Override
	public void explain(OWLOntology ontology) {
		OWLDataFactory df = editorKit.getOWLModelManager().getOWLDataFactory();
		OWLSubClassOfAxiom entailment = df.getOWLSubClassOfAxiom(df.getOWLThing(), df.getOWLNothing());
		PresentationPanel panel = new PresentationPanel(manager, entailment);

		JOptionPane op = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION);
		JDialog dlg = op.createDialog("Inconsistent ontology explanation");
		dlg.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentHidden(ComponentEvent e) {
				panel.dispose();
				dlg.dispose();
			}
		});
		dlg.setModal(false);
		dlg.setResizable(true);
		dlg.setVisible(true);
	}
}
