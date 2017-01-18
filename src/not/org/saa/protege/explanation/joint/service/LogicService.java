package not.org.saa.protege.explanation.joint.service;

import java.util.ArrayList;
import java.util.List;

import org.protege.editor.core.plugin.ProtegePluginInstance;
import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.OWLAxiom;

public abstract class LogicService implements ProtegePluginInstance {
	
	public abstract List<? extends List<OWLAxiom>> getAxioms(OWLAxiom entailment);
	//public abstract int getComputedExplanationsCount(OWLAxiom entailment);
	public abstract boolean hasAxioms(OWLAxiom entailment);

	private OWLEditorKit kit;

	private boolean isCancelled = false;
	
	public LogicService setup(OWLEditorKit kit) {
		this.kit = kit;
		return this;
	}
	
	public OWLEditorKit getOWLEditorKit() {
		return kit;
	}
	
    /**
     * Gets whether user cancelled the process
     * @return The explanation.
     */
	public boolean isCancelled()
	{
		return isCancelled;
	}

	private void cancel()
	{
		isCancelled = true;
	}
	
	@Override
	public abstract void dispose();
}