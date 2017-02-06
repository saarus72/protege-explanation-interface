package not.org.saa.protege.explanation.joint;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.explanation.ExplanationResult;
import org.protege.editor.owl.ui.explanation.ExplanationService;
import org.semanticweb.owlapi.model.OWLAxiom;

import not.org.saa.protege.explanation.joint.service.JustificationComputationService;

public class PresentationService extends ExplanationService {

	private JustificationComputationServiceManager<JustificationComputationService> manager;

	@Override
	public void initialise() throws Exception {
		OWLEditorKit kit = getOWLEditorKit();
		manager = new JustificationComputationServiceManager<JustificationComputationService>(kit, "not.org.saa.protege.explanation.joint", "JustificationService");
	}

	@Override
	public boolean hasExplanation(OWLAxiom axiom) {
		for (JustificationComputationService service : manager.getServices())
			if (service.hasAxioms(axiom))
				return true;
		return false;
	}

	@Override
	public ExplanationResult explain(OWLAxiom axiom) {
		PresentationPanel<JustificationComputationService> panel = new PresentationPanel<JustificationComputationService>(manager, axiom);
		return new PresentationPanelResult(panel);
	}

	@Override
	public void dispose() throws Exception {
	}
}
