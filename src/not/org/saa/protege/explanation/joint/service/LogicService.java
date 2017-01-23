package not.org.saa.protege.explanation.joint.service;

import java.util.List;
import java.util.Set;

import org.protege.editor.core.plugin.ProtegePluginInstance;
import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owlapi.model.OWLAxiom;

public abstract class LogicService implements ProtegePluginInstance {

	//public abstract Set<? extends List<OWLAxiom>> getAxioms(OWLAxiom entailment, AxiomsProgressMonitor<OWLAxiom> monitor);
	public abstract void startComputation(OWLAxiom entailment, AxiomsProgressMonitor<OWLAxiom> monitor);

	public abstract boolean hasAxioms(OWLAxiom entailment);

	private OWLEditorKit kit;

	protected OWLEditorKit getOWLEditorKit() {
		return kit;
	}
	
	protected LogicService setup(OWLEditorKit kit) {
		this.kit = kit;
		return this;
	}

	@Override
	public abstract void dispose();
}