package not.org.saa.protege.explanation.joint.service;

import org.semanticweb.owlapi.model.OWLAxiom;

/**
 * A skeleton for a plugin that can provide justification based explanation for
 * OWL axioms
 */

public abstract class JustificationComputationService extends ComputationService {

	/**
	 * To define whether the plugin can provide us with a justification for the axiom
	 * 
	 * @param entailment	an entailment to compute justification for
	 * @return	whether the plugin able to compute a justification for the axiom or not
	 */
	public abstract boolean hasAxioms(OWLAxiom entailment); ///!!!

	@Override
	public abstract void dispose();
}