package not.org.saa.protege.explanation.joint;

import java.util.Set;

import javax.annotation.Nonnull;

import org.semanticweb.owlapi.model.OWLAxiom;

import uk.ac.manchester.cs.owl.explanation.ordering.ExplanationTree;

public interface ExplanationOrderer {

	@Nonnull
	ExplanationTree getOrderedExplanation(@Nonnull OWLAxiom entailment, @Nonnull Set<OWLAxiom> axioms);
}
