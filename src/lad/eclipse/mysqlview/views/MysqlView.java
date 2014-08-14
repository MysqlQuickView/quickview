package lad.eclipse.mysqlview.views;

import java.util.ArrayList;
import java.util.List;

import lad.eclipse.action.EditorUtils;
import lad.eclipse.model.Column;
import lad.eclipse.model.iDBObj;
import lad.eclipse.mysqlview.Activator;
import lad.eclipse.mysqlview.preferences.MysqlPreferencePage;
import lad.eclipse.mysqlview.templates.DocumentDll;
import lad.eclipse.mysqlview.templates.Template;
import lad.eclipse.mysqlview.templates.TemplateContent;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;

public class MysqlView extends ViewPart {
	public static final String ID = "lad.eclipse.mysqlview.views.mysqlView";
	private CheckboxTreeViewer viewer;
	private DrillDownAdapter drillDownAdapter;
	private Action listAction;
	private Action arrayAction;
	private Action arrayAndCommentAction;
	private Action refreshAction;
	private Action settingAction;

	public void createPartControl(Composite parent) {
		this.viewer = new CheckboxTreeViewer(parent, 770);
		this.drillDownAdapter = new DrillDownAdapter(this.viewer);
		this.viewer.setContentProvider(new MysqlContentProvider());
		this.viewer.setLabelProvider(new DelegatingStyledCellLabelProvider(
				new MysqlLabelProvider()));

		this.viewer.setInput(getViewSite());
		this.viewer.setAutoExpandLevel(3);
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		hookCheckClickAction();
		contributeToActionBars();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				MysqlView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(this.viewer.getControl());
		this.viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, this.viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(this.refreshAction);
		manager.add(this.settingAction);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(this.listAction);
		manager.add(this.arrayAction);
		manager.add(this.arrayAndCommentAction);
		manager.add(new Separator());

		List<Template> lt = DocumentDll.getElements(null);

		for (Template t:lt) {
			MenuManager menumanager = new MenuManager(t.getTitle());
			for (Template t1:t.getChildren()) {

				Action action = new Action() {
					public void run() {
						String content = TemplateContent.getContent(
								MysqlView.this.getCheckedColumn(),
								EditorUtils.getEmptyString(),
								t1.getContent());
						EditorUtils.insert(content);
					}
				};
				action.setText(t1.getTitle());
				menumanager.add(action);
			}
			manager.add(menumanager);
		}

		manager.add(new Separator());
		manager.add(this.refreshAction);
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(this.refreshAction);
		manager.add(new Separator());
		this.drillDownAdapter.addNavigationActions(manager);
	}

	private List<iDBObj> getCheckedColumn() {
		ISelection selection = this.viewer.getSelection();
		iDBObj obj = (iDBObj) ((IStructuredSelection) selection)
				.getFirstElement();
		List checkObj = new ArrayList();

		if ((obj.getChildren() != null) && (obj.getChildren().length > 0)
				&& ((obj.getChildren()[0] instanceof Column))) {
			for (iDBObj ido : obj.getChildren()) {
				for (Object object : this.viewer.getCheckedElements()) {
					if (ido.equals(object)) {
						checkObj.add(ido);
					}
				}
			}
			return checkObj;
		}

		if ((obj instanceof Column)) {
			for (Object object : this.viewer.getCheckedElements()) {
				if ((object instanceof Column)) {
					checkObj.add((iDBObj) object);
				}
			}
			return checkObj;
		}
		return (List<iDBObj>) null;
	}

	private void makeActions() {
		this.listAction = new Action() {
			public void run() {
				List<iDBObj> list = MysqlView.this.getCheckedColumn();
				String[] namelist = new String[list.size()];
				int i = 0;
				for (iDBObj obj : list) {
					namelist[(i++)] = obj.getName();
				}

				StringBuilder sb = new StringBuilder();
				boolean first = true;
				for (String name : namelist) {
					if (first)
						first = false;
					else
						sb.append(",");
					sb.append(name);
				}

				EditorUtils.insert(sb.toString());
			}
		};
		this.listAction.setText("add list");
		this.listAction.setImageDescriptor(Activator
				.getImageDescriptor("icons/toa.gif"));

		this.arrayAction = new Action() {
			public void run() {
				List<iDBObj> list = MysqlView.this.getCheckedColumn();

				String emptyStr = EditorUtils.getEmptyString();
				StringBuilder sb = new StringBuilder();
				boolean first = true;
				for (iDBObj o : list) {
					if (first)
						first = false;
					else
						sb.append(emptyStr);
					sb.append("\"" + o.getName() + "\"\t=> ,\n");
				}

				EditorUtils.insert(sb.toString() + emptyStr);
			}
		};
		this.arrayAction.setText("add php array");
		this.arrayAction.setToolTipText("Action 2 tooltip");
		this.arrayAction.setImageDescriptor(Activator
				.getImageDescriptor("icons/list.png"));

		this.arrayAndCommentAction = new Action() {
			public void run() {
				List<iDBObj> list = MysqlView.this.getCheckedColumn();

				String emptyStr = EditorUtils.getEmptyString();
				StringBuilder sb = new StringBuilder();
				boolean first = true;
				for (iDBObj o : list) {
					Column column = (Column) o;
					if (first)
						first = false;
					else
						sb.append(emptyStr);
					sb.append("\"" + column.getName() + "\"\t=> ,\t//"
							+ column.getComment() + "\n");
				}

				EditorUtils.insert(sb.toString() + emptyStr);
			}
		};
		this.arrayAndCommentAction.setText("add php array &&comment");
		this.arrayAndCommentAction.setImageDescriptor(Activator
				.getImageDescriptor("icons/list.png"));

		this.refreshAction = new Action() {
			public void run() {
				MysqlView.this.viewer.refresh();
			}
		};
		this.refreshAction.setText("refresh");
		this.refreshAction.setImageDescriptor(Activator
				.getImageDescriptor("icons/refresh.gif"));

		this.settingAction = new Action() {
			public void run() {
				IPreferencePage page = new MysqlPreferencePage();
				PreferenceManager mgr = new PreferenceManager();
				page.setTitle("mysql setting");
				IPreferenceNode node = new PreferenceNode("mysql", page);
				mgr.addToRoot(node);
				PreferenceDialog dialog = new PreferenceDialog(
						MysqlView.this.viewer.getControl().getShell(), mgr);
				dialog.create();
				dialog.setMessage(page.getTitle());
				dialog.open();
			}
		};
		this.settingAction.setText("setting");
		this.settingAction.setImageDescriptor(Activator
				.getImageDescriptor("icons/setting.gif"));
	}

	private void hookDoubleClickAction() {
		this.viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				ISelection selection = MysqlView.this.viewer.getSelection();
				Object obj = ((IStructuredSelection) selection)
						.getFirstElement();
				String name = ((iDBObj) obj).getName();
				EditorUtils.insert(name);
			}
		});
	}

	private void hookCheckClickAction() {
		this.viewer.addCheckStateListener(new ICheckStateListener() {
			public void checkStateChanged(CheckStateChangedEvent event) {
				MysqlView.this.viewer.setSubtreeChecked(event.getElement(),
						event.getChecked());
			}
		});
	}

	public void setFocus() {
		this.viewer.getControl().setFocus();
	}
}