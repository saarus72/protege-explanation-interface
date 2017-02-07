package not.org.saa.protege.explanation.joint;

import org.semanticweb.owlapi.model.OWLAxiom;

/**
 * Author: Matthew Horridge
 * The University Of Manchester
 * Information Management Group
 * Date: 04-Oct-2008
 */

public interface ExplanationManagerListener {
	void explanationLimitChanged(PresentationManager presentationManager);

	void explanationsComputed(OWLAxiom entailment);
}
