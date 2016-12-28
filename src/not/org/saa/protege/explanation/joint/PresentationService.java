package not.org.saa.protege.explanation.joint;

import org.protege.editor.owl.ui.explanation.ExplanationResult;
import org.protege.editor.owl.ui.explanation.ExplanationService;
import org.semanticweb.owlapi.model.OWLAxiom;

public class PresentationService extends ExplanationService {

	@Override
	public void dispose() throws Exception {
	}

	@Override
	public void initialise() throws Exception {
	}

	@Override
	public boolean hasExplanation(OWLAxiom axiom) {
		return true;
	}

	@Override
	public ExplanationResult explain(OWLAxiom axiom) {
		return null;
	}

}
