package not.org.saa.protege.explanation.joint.service;

import org.eclipse.core.runtime.IExtension;
import org.protege.editor.core.plugin.AbstractProtegePlugin;
import org.protege.editor.core.plugin.ProtegePluginInstance;
import org.protege.editor.owl.OWLEditorKit;

public class JustificationComputationPlugin<T extends ComputationService> extends AbstractProtegePlugin<T> {
	//public static final String KEY = "not.org.saa.protege.explanation.joint";
	//public static final String ID = "JustificationService";
	public static final String NAME = "name";

	private final OWLEditorKit kit;

	public JustificationComputationPlugin(OWLEditorKit kit, IExtension extension) {
		super(extension);
		this.kit = kit;
	}

	public String getName() {
		return getPluginProperty(NAME);
	}

	@Override
	public T newInstance()
			throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		T inst = super.newInstance();
		inst = (T) inst.stp(kit);
		return inst; //!!!
	}
}
