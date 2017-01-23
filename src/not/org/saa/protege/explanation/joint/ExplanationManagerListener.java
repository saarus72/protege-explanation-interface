package not.org.saa.protege.explanation.joint;

import org.semanticweb.owlapi.model.OWLAxiom;

public interface ExplanationManagerListener {
	void explanationLimitChanged(PresentationManager presentationManager);

	void explanationsComputed(OWLAxiom entailment);
}
