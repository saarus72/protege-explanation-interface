package not.org.saa.protege.explanation.joint.service;

import org.eclipse.core.runtime.IExtension;
import org.protege.editor.core.plugin.AbstractProtegePlugin;
import org.protege.editor.core.plugin.ProtegePluginInstance;
import org.protege.editor.owl.OWLEditorKit;

/**
 * Instantiates the plugin which is based on specified computation service
 * 
 * @param <T>	service which is based on ComputationService
 */
public class JustificationComputationPlugin<T extends ComputationService> extends AbstractProtegePlugin<T> {
	private final OWLEditorKit kit;

	/**
	 * Constructs plugin object
	 * 
	 * @param kit	OWLEditorKit for the service
	 * @param extension	plugin extension
	 */
	public JustificationComputationPlugin(OWLEditorKit kit, IExtension extension) {
		super(extension);
		this.kit = kit;
	}

	@Override
	public T newInstance()
			throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		return (T) super.newInstance().stp(kit);
	}
}
