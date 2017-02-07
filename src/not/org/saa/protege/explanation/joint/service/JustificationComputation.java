package not.org.saa.protege.explanation.joint.service;

import org.semanticweb.owlapi.model.OWLAxiom;

/**
 * Designed to compute and keep track of justifications
 */

public abstract class JustificationComputation {
	
	private final OWLAxiom entailment;

	/**
	 * Constructs a computation object
	 * 
	 * @param entailment	an axiom to compute justifications for
	 */
	public JustificationComputation(OWLAxiom entailment) {
		this.entailment = entailment;
	}

	/**
	 * Should initiate a computation process
	 */
	public abstract void startComputation();

	/**
	 * When called, asks the plugin to stop computation process
	 */
	public abstract void interruptComputation();
	
	/**
	 * Should return whether the computation object was notified to stop computation process
	 * 
	 * @return	whether the computation object was notified to stop computation process or not
	 */
	public abstract boolean isComputationInterrupted();

	/**
	 * Should store a listener to notify about computed justifications
	 * 
	 * @param listener	listener which should be notified about computed justifications
	 */
	public abstract void addComputationListener(JustificationComputationListener listener);

	/**
	 * Should remove the specified listener
	 * 
	 * @param listener	listener to notify no more
	 */
	public abstract void removeComputationListener(JustificationComputationListener listener);

	/**
	 * Returns the entailment
	 * 
	 * @return	an axiom to compute justifications for
	 */
	public OWLAxiom getEntailment() {
		return entailment;
	}
}