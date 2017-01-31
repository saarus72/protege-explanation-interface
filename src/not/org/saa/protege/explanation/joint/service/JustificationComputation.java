package not.org.saa.protege.explanation.joint.service;

import org.semanticweb.owlapi.model.OWLAxiom;

public abstract class JustificationComputation {
	private final OWLAxiom entailment;

	public JustificationComputation(OWLAxiom entailment) {
		this.entailment = entailment;
	}

	public abstract void startComputation();

	public abstract void interruptComputation();
	
	public abstract boolean isComputationInterrupted();

	public abstract void addComputationListener(JustificationComputationListener listener);

	public abstract void removeComputationListener(JustificationComputationListener listener);

	public OWLAxiom getEntailment() {
		return entailment;
	}
}