package not.org.saa.protege.explanation.joint;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JList;
import javax.swing.KeyStroke;
import javax.swing.border.Border;

import org.protege.editor.core.ui.list.MListButton;
import org.protege.editor.core.ui.list.MListItem;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.frame.OWLFrame;
import org.protege.editor.owl.ui.framelist.ExplainButton;
import org.protege.editor.owl.ui.framelist.OWLFrameList;
import org.protege.editor.owl.ui.framelist.OWLFrameListPopupMenuAction;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.OWLAxiom;

public class AxiomsFrameList extends OWLFrameList<Explanation<OWLAxiom>> {

	public static final Color SINGLE_POPULARITY_COLOR = new Color(170, 70, 15);

	public static final Color MULTI_POPULARITY_COLOR = new Color(10, 75, 175);

	public static final Color ALL_POPULARITY_COLOR = new Color(6, 133, 19);

	private int buttonRunWidth = 0;

	// private AxiomSelectionModel axiomSelectionModel;

	private PresentationManager manager;

	public AxiomsFrameList(/*, AxiomSelectionModel axiomSelectionModel,*/
			PresentationManager manager, OWLFrame<Explanation<OWLAxiom>> explanationOWLFrame) {
		super(manager.getOWLEditorKit(), explanationOWLFrame);
		OWLEditorKit kit = manager.getOWLEditorKit();
		this.manager = manager;
		// this.axiomSelectionModel = axiomSelectionModel;
		setWrap(false);
		setCellRenderer(new AxiomsFrameListRenderer(kit));

		Action moveUpAction = new AbstractAction("Move up") {
			public void actionPerformed(ActionEvent e) {
				handleMoveUp();
			}
		};
		getActionMap().put(moveUpAction.getValue(Action.NAME), moveUpAction);
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.CTRL_MASK),
				moveUpAction.getValue(Action.NAME));

		Action moveDownAction = new AbstractAction("Move down") {
			public void actionPerformed(ActionEvent e) {
				handleMoveDown();
			}
		};
		getActionMap().put(moveDownAction.getValue(Action.NAME), moveDownAction);
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.CTRL_MASK),
				moveDownAction.getValue(Action.NAME));

		Action increaseIndentation = new AbstractAction("Increase indentation") {
			public void actionPerformed(ActionEvent e) {
				handleIncreaseIndentation();
			}
		};
		getActionMap().put(increaseIndentation.getValue(Action.NAME), increaseIndentation);
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.CTRL_MASK),
				increaseIndentation.getValue(Action.NAME));

		Action decreaseIndentation = new AbstractAction("decrease indentation") {
			public void actionPerformed(ActionEvent e) {
				handleDecreaseIndentation();
			}
		};
		getActionMap().put(decreaseIndentation.getValue(Action.NAME), decreaseIndentation);
		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.CTRL_MASK),
				decreaseIndentation.getValue(Action.NAME));

	}

	private void handleMoveUp() {
		OWLAxiom selectedAxiom = getSelectedAxiom();
		if (selectedAxiom == null) {
			return;
		}
		AxiomsFormattingManager formattingManager = AxiomsFormattingManager.getManager();
		int newIndex = formattingManager.moveAxiomUp(getRootObject(), selectedAxiom);
		getFrame().setRootObject(getRootObject());
		setSelectedIndex(newIndex + 1);
	}

	private void handleMoveDown() {
		OWLAxiom selectedAxiom = getSelectedAxiom();
		if (selectedAxiom == null) {
			return;
		}
		AxiomsFormattingManager formattingManager = AxiomsFormattingManager.getManager();
		int newIndex = formattingManager.moveAxiomDown(getRootObject(), selectedAxiom);
		getFrame().setRootObject(getRootObject());
		setSelectedIndex(newIndex + 1);
	}

	private void handleIncreaseIndentation() {
		OWLAxiom selectedAxiom = getSelectedAxiom();
		if (selectedAxiom == null) {
			return;
		}
		AxiomsFormattingManager formattingManager = AxiomsFormattingManager.getManager();
		formattingManager.increaseIndentation(getRootObject(), selectedAxiom);
		int selIndex = getSelectedIndex();
		getFrame().setRootObject(getRootObject());
		setSelectedIndex(selIndex);
	}

	private void handleDecreaseIndentation() {
		OWLAxiom selectedAxiom = getSelectedAxiom();
		if (selectedAxiom == null) {
			return;
		}
		AxiomsFormattingManager formattingManager = AxiomsFormattingManager.getManager();
		formattingManager.decreaseIndentation(getRootObject(), selectedAxiom);
		int selIndex = getSelectedIndex();
		getFrame().setRootObject(getRootObject());
		setSelectedIndex(selIndex);
	}

	private OWLAxiom getSelectedAxiom() {
		int selectedIndex = getSelectedIndex();
		if (selectedIndex == -1) {
			return null;
		}
		Object element = getModel().getElementAt(selectedIndex);
		if (!(element instanceof AxiomsFrameSectionRow)) {
			return null;
		}
		return ((AxiomsFrameSectionRow) element).getAxiom();
	}

	@Override
	protected List<MListButton> getButtons(Object value) {
		if (value instanceof AxiomsFrameSectionRow) {
			List<MListButton> buttons = Arrays.<MListButton>asList(new ExplainButton(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					invokeExplanationHandler();
				}
			}));
			buttonRunWidth = buttons.size() * (getButtonDimension() + 2) + 20;
			return buttons;
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public void addToPopupMenu(
			OWLFrameListPopupMenuAction<Explanation<OWLAxiom>> explanationOWLFrameListPopupMenuAction) {
		// NO MENU FOR US
	}

	@Override
	protected Color getItemBackgroundColor(MListItem item) {
//		if (item instanceof AxiomsFrameSectionRow) {
//			AxiomsFrameSectionRow row = (AxiomsFrameSectionRow) item;
//			OWLAxiom axiom = row.getAxiom();
//			int rowIndex = row.getFrameSection().getRowIndex(row) + 1;
//			if (!isSelectedIndex(rowIndex)) {
//				if (axiomSelectionModel.getSelectedAxioms().contains(axiom)) {
//					return Color.YELLOW;
//				} else {
//					boolean inAll = true;
//					for (Explanation<?> expl : workbenchManager.getJustifications(getRootObject().getEntailment())) {
//						if (!expl.contains(axiom)) {
//							inAll = false;
//							break;
//						}
//					}
//					if (inAll) {
//						return new Color(245, 255, 235);
//					}
//
//				}
//			}
//		}
		return super.getItemBackgroundColor(item);
	}

	@Override
	protected List<MListButton> getListItemButtons(MListItem item) {
		return Collections.emptyList();
	}

	@Override
	protected Border createListItemBorder(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		return super.createListItemBorder(list, value, index, isSelected, cellHasFocus);
	}

	@Override
	protected Border createPaddingBorder(JList list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		return super.createPaddingBorder(list, value, index, isSelected, cellHasFocus);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}
