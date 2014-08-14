package lad.eclipse.mysqlview.preferences;

import lad.eclipse.mysqlview.templates.ConfirmDialog;
import lad.eclipse.mysqlview.templates.DocumentDll;
import lad.eclipse.mysqlview.templates.Template;
import lad.eclipse.mysqlview.templates.TemplateContentProvider;
import lad.eclipse.mysqlview.templates.TemplateDialog;
import lad.eclipse.mysqlview.templates.TemplateLabelProvider;
import lad.eclipse.mysqlview.templates.iDialogDelegate;
import lad.eclipse.mysqlview.templates.iTemplateDialogDelegate;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class TemplatePreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {
	private Text text;
	private Composite fieldEditorParent;

	protected Control createContents(Composite parent) {
		this.fieldEditorParent = new Composite(parent, 0);
		this.fieldEditorParent.setLayout(null);

		TreeViewer treeViewer = new TreeViewer(this.fieldEditorParent, 2818);

		treeViewer.setContentProvider(new TemplateContentProvider());
		treeViewer.setLabelProvider(new DelegatingStyledCellLabelProvider(
				new TemplateLabelProvider()));
		treeViewer.setInput(parent);
		treeViewer.setAutoExpandLevel(3);

		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection selection = treeViewer.getSelection();
				try {
					Template e = (Template) ((IStructuredSelection) selection).getFirstElement();
					if(!e.isCategory()){
						TemplatePreferencePage.this.text.setText(e.getContent());
					}
					
				} catch (NullPointerException localNullPointerException) {
				}
			}
		});
		Tree tree = treeViewer.getTree();
		tree.setBounds(0, 0, 324, 230);

		Button btnNewCategory = new Button(this.fieldEditorParent, 0);
		btnNewCategory.setBounds(338, 22, 90, 27);
		btnNewCategory.setText("new category");

		Shell shell = parent.getShell();

		btnNewCategory.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ConfirmDialog dialog = new ConfirmDialog(shell,
						"category name", new iDialogDelegate() {
							public void run(String text) {
								DocumentDll.addcategore(text);
								treeViewer.refresh();
							}
						});
				dialog.open();
			}
		});
		this.text = new Text(this.fieldEditorParent, 2570);
		this.text.setBounds(0, 235, 324, 162);

		Button btnNewButton = new Button(this.fieldEditorParent, 0);
		btnNewButton.setBounds(338, 55, 90, 27);
		btnNewButton.setText("new template");
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ISelection selection = treeViewer.getSelection();
				Template template = (Template) ((IStructuredSelection) selection).getFirstElement();
				if (template.isCategory()) {
					TemplateDialog dialog = new TemplateDialog(shell,
							new iTemplateDialogDelegate() {
								public void run(String name, String comment, String content) {
									Template newTemplate = new Template();
									newTemplate.setTitle(name);
									newTemplate.setDescription(comment);
									newTemplate.setContent(content);
									newTemplate.setFid(template.getId());
									DocumentDll.addtemplate(newTemplate);
									treeViewer.refresh();
								}
							});
					dialog.open();
				}
			}
		});
		Button btnUp = new Button(this.fieldEditorParent, 0);
		btnUp.setBounds(338, 106, 90, 27);
		btnUp.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ISelection selection = treeViewer.getSelection();
				Template template = (Template) ((IStructuredSelection) selection).getFirstElement();
				
				if (template != null) {
					//往上一格
					DocumentDll.up(template);
					treeViewer.refresh();
				}
			}
		});
		btnUp.setText("UP");

		Button btnNewButton_1 = new Button(this.fieldEditorParent, 0);
		btnNewButton_1.setBounds(338, 139, 90, 27);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ISelection selection = treeViewer.getSelection();
				Template template = (Template) ((IStructuredSelection) selection).getFirstElement();
				
				if (template != null) {
					// 往下一格
					DocumentDll.down(template);
					treeViewer.refresh();
				}
			}
		});
		btnNewButton_1.setText("DOWN");

		Button btnNewButton_2 = new Button(this.fieldEditorParent, 0);
		btnNewButton_2.setBounds(338, 190, 90, 27);
		btnNewButton_2.setText("edit");
		btnNewButton_2.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ISelection selection = treeViewer.getSelection();
				Template template = (Template) ((IStructuredSelection) selection).getFirstElement();
				
				if (!template.isCategory()) {
					TemplateDialog dialog = new TemplateDialog(shell, template,
							new iTemplateDialogDelegate() {
								public void run(String name, String comment,
										String content) {
									template.setTitle(name);
									template.setDescription(comment);
									template.setContent(content);
									
									DocumentDll.edittemplate(template);
									treeViewer.refresh();
								}
							});
					dialog.open();
				}
			}
		});
		Button btnNewButton_3 = new Button(this.fieldEditorParent, 0);
		btnNewButton_3.setBounds(338, 223, 90, 27);
		btnNewButton_3.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ISelection selection = treeViewer.getSelection();
				Template template = (Template) ((IStructuredSelection) selection).getFirstElement();
				DocumentDll.removeTemplate(template);
				treeViewer.refresh();
			}
		});
		btnNewButton_3.setText("remove");
		Label label = new Label(this.fieldEditorParent, 0);
		label.setBounds(102, 237, 0, 17);

		this.fieldEditorParent.setLayout(null);
		this.fieldEditorParent.setLayoutData(new GridData(1808));

		return this.fieldEditorParent;
	}

	public void init(IWorkbench workbench) {
	}
}