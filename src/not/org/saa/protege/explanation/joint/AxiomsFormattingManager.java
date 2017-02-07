package not.org.saa.protege.explanation.joint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;

import uk.ac.manchester.cs.owl.explanation.ordering.ExplanationOrderer;
import uk.ac.manchester.cs.owl.explanation.ordering.ExplanationTree;
import uk.ac.manchester.cs.owl.explanation.ordering.Tree;

/**
 * Author: Matthew Horridge
 * The University Of Manchester
 * Information Management Group
 * Date: 23-Oct-2008
 */

public class AxiomsFormattingManager {
	static private AxiomsFormattingManager manager;

	private Map<Explanation<?>, Map<OWLAxiom, Integer>> indentMap;

	private Map<Explanation<?>, List<OWLAxiom>> orderingMap;

	private AxiomsFormattingManager() {
		indentMap = new HashMap<>();
		orderingMap = new HashMap<>();
	}

	public static synchronized AxiomsFormattingManager getManager() {
		if (manager == null) {
			manager = new AxiomsFormattingManager();
		}
		return manager;
	}

	private void init(Explanation<?> explanation) {
		ExplanationOrderer orderer = new ProtegeExplanationOrderer(OWLManager.createOWLOntologyManager());
		ExplanationTree tree = orderer.getOrderedExplanation((OWLAxiom) explanation.getEntailment(),
				explanation.getAxioms());
		List<OWLAxiom> ordering = new ArrayList<>();
		Map<OWLAxiom, Integer> im = new HashMap<>();
		fill(tree, ordering, im);
		indentMap.put(explanation, im);
		orderingMap.put(explanation, ordering);

	}

	private static void fill(Tree<OWLAxiom> tree, List<OWLAxiom> ordering, Map<OWLAxiom, Integer> indentMap) {
		if (!tree.isRoot()) {
			ordering.add(tree.getUserObject());
			indentMap.put(tree.getUserObject(), tree.getPathToRoot().size() - 2);
		}
		for (Tree<OWLAxiom> child : tree.getChildren()) {
			fill(child, ordering, indentMap);
		}
	}

	private void initIfNecessary(Explanation<?> explanation) {
		if (!indentMap.containsKey(explanation)) {
			init(explanation);
		}
	}

	public int getIndentation(Explanation<?> explanation, OWLAxiom axiom) {
		if (!explanation.getAxioms().contains(axiom) && !explanation.getEntailment().equals(axiom)) {
			throw new IllegalArgumentException(
					"The explanation does not contain the specified axiom: " + axiom + "  " + explanation);
		}
		initIfNecessary(explanation);
		Integer i = indentMap.get(explanation).get(axiom);
		if (i != null) {
			return i;
		} else {
			return 0;
		}
	}

	public void setIndentation(Explanation<?> explanation, OWLAxiom axiom, int indentation) {
		initIfNecessary(explanation);
		indentMap.get(explanation).put(axiom, indentation);
	}

	public void increaseIndentation(Explanation<?> explanation, OWLAxiom axiom) {
		initIfNecessary(explanation);
		Integer indent = getIndentation(explanation, axiom);
		setIndentation(explanation, axiom, indent + 1);
	}

	public void decreaseIndentation(Explanation<?> explanation, OWLAxiom axiom) {
		initIfNecessary(explanation);
		Integer indent = getIndentation(explanation, axiom);
		indent = indent - 1;
		if (indent < 0) {
			indent = 0;
		}
		setIndentation(explanation, axiom, indent);
	}

	public int moveAxiomUp(Explanation<?> explanation, OWLAxiom axiom) {
		initIfNecessary(explanation);
		List<OWLAxiom> ordering = orderingMap.get(explanation);
		// Lowest index is 1 - the entailment is held in position 0
		int index = ordering.indexOf(axiom);
		if (index > 0) {
			index--;
		}
		ordering.remove(axiom);
		ordering.add(index, axiom);
		return index;
	}

	public int moveAxiomDown(Explanation<?> explanation, OWLAxiom axiom) {
		initIfNecessary(explanation);
		List<OWLAxiom> ordering = orderingMap.get(explanation);
		// Lowest index is 1 - the entailment is held in position 0
		int index = ordering.indexOf(axiom);
		if (index < ordering.size() - 1) {
			index++;
		}
		ordering.remove(axiom);
		ordering.add(index, axiom);
		return index;
	}

	public List<OWLAxiom> getOrdering(Explanation<?> explanation) {
		initIfNecessary(explanation);
		return Collections.unmodifiableList(orderingMap.get(explanation));
	}

	public void clearFormatting(Explanation<?> explanation) {
		indentMap.remove(explanation);
		orderingMap.remove(explanation);
	}
}
