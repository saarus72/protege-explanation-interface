package not.org.saa.protege.explanation.joint;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.OWLAxiom;

public class AxiomsCache {
	private Map<OWLAxiom, Set<Explanation<OWLAxiom>>> cache = new HashMap<>();

	public boolean contains(OWLAxiom entailment) {
		return cache.containsKey(entailment);
	}

	public Set<Explanation<OWLAxiom>> get(OWLAxiom entailment) {
		Set<Explanation<OWLAxiom>> explanations = cache.get(entailment);
		if (explanations == null) {
			return Collections.emptySet();
		}
		return new HashSet<>(explanations);
	}

	public void put(Explanation<OWLAxiom> explanation) {
		Set<Explanation<OWLAxiom>> expls = cache.get(explanation.getEntailment());
		if (expls == null) {
			expls = new HashSet<>();
			cache.put(explanation.getEntailment(), expls);
		}
		expls.add(explanation);
	}

	public void put(Set<Explanation<OWLAxiom>> explanations) {
		for (Explanation<OWLAxiom> expl : explanations) {
			put(expl);
		}
	}

	public void clear() {
		cache.clear();
	}

	public void clear(OWLAxiom entailment) {
		cache.remove(entailment);
	}

}