package not.org.saa.protege.explanation.joint;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.explanation.ExplanationResult;
import org.protege.editor.owl.ui.explanation.ExplanationService;
import org.semanticweb.owlapi.model.OWLAxiom;

import not.org.saa.protege.explanation.joint.service.ComputationService;

public class PresentationService extends ExplanationService {

	private JustificationComputationServiceManager manager;

	@Override
	public void initialise() throws Exception {
		OWLEditorKit kit = getOWLEditorKit();
		manager = new JustificationComputationServiceManager(kit, "not.org.saa.protege.explanation.joint", "JustificationService");
	}

	@Override
	public boolean hasExplanation(OWLAxiom axiom) {
		for (ComputationService service : manager.getServices())
			if (service.canComputeJustification(axiom))
				return true;
		return false;
	}

	@Override
	public ExplanationResult explain(OWLAxiom axiom) {
		PresentationPanel panel = new PresentationPanel(manager, axiom);
		return new PresentationPanelResult(panel);
	}

	@Override
	public void dispose() throws Exception {
	}
}
