package not.org.saa.protege.explanation.joint.service;

import org.eclipse.core.runtime.IExtension;
import org.protege.editor.core.plugin.AbstractPluginLoader;
import org.protege.editor.owl.OWLEditorKit;

/**
 * Load the available specified {@link ComputationService} plugins
 * 
 * @param <T>	service which is based on ComputationService
 */

public class JustificationComputationPluginLoader<T extends ComputationService> extends AbstractPluginLoader<JustificationComputationPlugin<T>> {
	
	private final OWLEditorKit kit;

	/**
	 * Constructs JustificationComputationPluginLoader
	 * 
	 * @param kit	OWLEditorKit which is necessary to instantiate a JustificationComputationPlugin
	 * @param KEY	A string to specify the extension point to find plugins for
	 * @param ID	A string to specify the extension point to find plugins for
	 */
	public JustificationComputationPluginLoader(OWLEditorKit kit, String KEY, String ID) {
		super(KEY, ID);
		this.kit = kit;
	}

	@Override
	protected JustificationComputationPlugin<T> createInstance(IExtension extension) {
		return new JustificationComputationPlugin<T>(kit, extension);
	}
}
