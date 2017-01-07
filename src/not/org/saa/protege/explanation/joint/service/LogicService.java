package not.org.saa.protege.explanation.joint.service;

import java.util.ArrayList;
import java.util.List;

import org.protege.editor.core.plugin.ProtegePluginInstance;
import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.OWLAxiom;

public abstract class LogicService implements ProtegePluginInstance {
	public abstract List<? extends List<OWLAxiom>> getAxioms(OWLAxiom entailment);
	public abstract boolean hasAxioms(OWLAxiom entailment);

	private OWLEditorKit kit;

	public LogicService setup(OWLEditorKit kit) {
		this.kit = kit;
		return this;
	}
	
	public OWLEditorKit getOWLEditorKit() {
		return kit;
	}

	@Override
	public abstract void dispose();
}