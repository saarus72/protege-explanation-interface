package not.org.saa.protege.explanation.joint.service;

import org.eclipse.core.runtime.IExtension;
import org.protege.editor.core.plugin.AbstractPluginLoader;
import org.protege.editor.owl.OWLEditorKit;

public class JustificationComputationPluginLoader<T extends ComputationService> extends AbstractPluginLoader<JustificationComputationPlugin<T>> {
	private final OWLEditorKit kit;
	//public static String KEY;
	//public static String ID;

	public JustificationComputationPluginLoader(OWLEditorKit kit, String KEY, String ID) {
		//super(JustificationComputationPlugin.KEY, JustificationComputationPlugin.ID);
		super(KEY, ID);
		//this.KEY = KEY;
		//this.ID = ID;
		this.kit = kit;
	}

	@Override
	protected JustificationComputationPlugin<T> createInstance(IExtension extension) {
		return new JustificationComputationPlugin<T>(kit, extension);
	}
}
