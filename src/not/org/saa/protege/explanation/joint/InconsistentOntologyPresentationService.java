package not.org.saa.protege.explanation.joint;

import org.protege.editor.owl.ui.explanation.io.InconsistentOntologyPluginInstance;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.OWLModelManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import not.org.saa.protege.explanation.joint.service.InconsistentOntologyJustificationComputationService;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class InconsistentOntologyPresentationService implements InconsistentOntologyPluginInstance {

	private OWLEditorKit editorKit;
	private JustificationComputationServiceManager<InconsistentOntologyJustificationComputationService> manager;

	@Override
	public void setup(OWLEditorKit editorKit) {
		this.editorKit = editorKit;
	}

	@Override
	public void initialise() throws Exception {
		manager = new JustificationComputationServiceManager<InconsistentOntologyJustificationComputationService>(
				editorKit, "not.org.saa.protege.explanation.joint", "InconsistentOntologyJustificationService");
		// !!! does kit exist at this point? mb move the line to setup()
	}

	@Override
	public void dispose() throws Exception {
	}

	@Override
	public void explain(OWLOntology ontology) {
		OWLModelManager owlModelManager = editorKit.getOWLModelManager();
		OWLDataFactory df = owlModelManager.getOWLDataFactory();
		OWLSubClassOfAxiom entailment = df.getOWLSubClassOfAxiom(df.getOWLThing(), df.getOWLNothing());
		PresentationPanel<InconsistentOntologyJustificationComputationService> panel = new PresentationPanel<InconsistentOntologyJustificationComputationService>(
				manager, entailment);

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
