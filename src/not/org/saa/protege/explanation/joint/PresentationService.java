package not.org.saa.protege.explanation.joint;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.explanation.ExplanationResult;
import org.protege.editor.owl.ui.explanation.ExplanationService;
import org.semanticweb.owlapi.model.OWLAxiom;

import not.org.saa.protege.explanation.joint.service.LogicService;

public class PresentationService extends ExplanationService {

	private LogicServiceManager manager;

	@Override
	public void initialise() throws Exception {
		OWLEditorKit kit = getOWLEditorKit();
		manager = new LogicServiceManager(kit);
	}

	@Override
	public boolean hasExplanation(OWLAxiom axiom) {
		for (LogicService service : manager.getServices())
			if (service.hasAxioms(axiom))
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
