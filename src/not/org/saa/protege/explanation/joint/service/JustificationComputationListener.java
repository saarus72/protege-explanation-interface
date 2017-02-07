package not.org.saa.protege.explanation.joint.service;

import java.util.Collection;
import java.util.List;

import org.semanticweb.owlapi.model.OWLAxiom;

/**
 * Listener should be created to be provided with computed justifications
 */

public interface JustificationComputationListener {

	/**
	 * Should be called when another justification is computed
	 * 
	 * @param justification	another justification for the axiom
	 */
	void foundJustification(Collection<OWLAxiom> justification);
}
