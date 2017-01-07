package not.org.saa.protege.explanation.joint;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.frame.AbstractOWLFrame;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.OWLAxiom;

public class AxiomsFrame extends AbstractOWLFrame<Explanation<OWLAxiom>> {

    public AxiomsFrame(OWLEditorKit editorKit) {
        super(editorKit.getOWLModelManager().getOWLOntologyManager());
        addSection(new AxiomsFrameSection(editorKit, this));
    }
}