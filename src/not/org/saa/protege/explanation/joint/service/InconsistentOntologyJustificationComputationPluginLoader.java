package not.org.saa.protege.explanation.joint.service;

import org.eclipse.core.runtime.IExtension;
import org.protege.editor.core.plugin.AbstractPluginLoader;
import org.protege.editor.owl.OWLEditorKit;

public class InconsistentOntologyJustificationComputationPluginLoader extends AbstractPluginLoader<InconsistentOntologyJustificationComputationPlugin> {
	private final OWLEditorKit kit;

	public InconsistentOntologyJustificationComputationPluginLoader(OWLEditorKit kit) {
		super(InconsistentOntologyJustificationComputationPlugin.KEY, InconsistentOntologyJustificationComputationPlugin.ID);
		this.kit = kit;
	}

	@Override
	protected InconsistentOntologyJustificationComputationPlugin createInstance(IExtension extension) {
		return new InconsistentOntologyJustificationComputationPlugin(kit, extension);
	}
}
