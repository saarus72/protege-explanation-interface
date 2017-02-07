package not.org.saa.protege.explanation.joint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.protege.editor.core.Disposable;
import org.semanticweb.owlapi.model.OWLAxiom;

/**
 * Author: Matthew Horridge
 * The University Of Manchester
 * Information Management Group
 * Date: 09-Oct-2008
 */

public class AxiomSelectionModelImpl implements AxiomSelectionModel, Disposable {

	private Set<OWLAxiom> selectedAxioms;

	private List<AxiomSelectionListener> listeners;

	public AxiomSelectionModelImpl() {
		selectedAxioms = new HashSet<>();
		listeners = new ArrayList<>();
	}

	public void setAxiomSelected(OWLAxiom axiom, boolean b) {
		if (b) {
			if (!selectedAxioms.contains(axiom)) {
				selectedAxioms.add(axiom);
				fireEvent(axiom, true);
			}
		} else {
			if (selectedAxioms.contains(axiom)) {
				selectedAxioms.remove(axiom);
				fireEvent(axiom, false);
			}
		}
	}

	protected void fireEvent(OWLAxiom axiom, boolean added) {
		for (AxiomSelectionListener lsnr : new ArrayList<>(listeners)) {
			if (added) {
				lsnr.axiomAdded(this, axiom);
			} else {
				lsnr.axiomRemoved(this, axiom);
			}
		}
	}

	public Set<OWLAxiom> getSelectedAxioms() {
		return Collections.unmodifiableSet(selectedAxioms);
	}

	public void addAxiomSelectionListener(AxiomSelectionListener lsnr) {
		listeners.add(lsnr);
	}

	public void removeAxiomSelectionListener(AxiomSelectionListener lsnr) {
		listeners.remove(lsnr);
	}

	@Override
	public void dispose() {
		while (listeners.size() != 0)
			removeAxiomSelectionListener(listeners.get(0));
	}
}
