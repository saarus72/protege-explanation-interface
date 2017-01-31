package not.org.saa.protege.explanation.joint.service;

import org.eclipse.core.runtime.IExtension;
import org.protege.editor.core.plugin.AbstractProtegePlugin;
import org.protege.editor.owl.OWLEditorKit;

public class LogicPlugin extends AbstractProtegePlugin<JustificationComputationService> {
	public static final String KEY = "not.org.saa.protege.explanation.joint";
	public static final String ID = "service";
	public static final String NAME = "name";

	private final OWLEditorKit kit;

	public LogicPlugin(OWLEditorKit kit, IExtension extension) {
		super(extension);
		this.kit = kit;
	}

	public String getName() {
		return getPluginProperty(NAME);
	}

	@Override
	public JustificationComputationService newInstance()
			throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		return super.newInstance().setup(kit);
	}
}
