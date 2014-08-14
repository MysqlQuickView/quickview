package lad.eclipse.mysqlview.templates;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class TemplateDialog extends Dialog {
	private iTemplateDialogDelegate delegate;
	private String _TemplateName = "";
	private String _TemplateComment = "";
	private String _TemplateContent = "";
	private Listener oklistener;

	public TemplateDialog(Shell shell, iTemplateDialogDelegate delegate) {
		super(shell);
		this.delegate = delegate;
	}

	public TemplateDialog(Shell shell, Template template,
			iTemplateDialogDelegate delegate) {
		super(shell);
		this.delegate = delegate;
		this._TemplateName = template.getTitle();
		this._TemplateComment = template.getDescription();
		this._TemplateContent = template.getContent();
	}

	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, 0);

		Text text_1 = new Text(composite, 2048);
		text_1.setBounds(147, 20, 239, 23);
		text_1.setText(this._TemplateName);

		Label lblName = new Label(composite, 131072);
		lblName.setBounds(33, 23, 97, 17);
		lblName.setText("template name:");

		Label lblDescription = new Label(composite, 131072);
		lblDescription.setBounds(52, 63, 78, 17);
		lblDescription.setText("description:");

		Text text_2 = new Text(composite, 2048);
		text_2.setBounds(147, 60, 239, 23);
		text_2.setText(this._TemplateComment);

		Label txtTemplate = new Label(composite, 0);
		txtTemplate.setText("function:");
		txtTemplate.setBounds(57, 104, 73, 23);

		Text text_3 = new Text(composite, 2818);
		text_3.setBounds(147, 104, 372, 238);
		text_3.setText(this._TemplateContent);

		Button btnFinish = new Button(composite, 0);
		btnFinish.setBounds(348, 358, 80, 27);
		btnFinish.setText("finish");
		btnFinish.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				TemplateDialog.this.delegate.run(text_1.getText(),
						text_2.getText(), text_3.getText());
				TemplateDialog.this.setReturnCode(0);
				TemplateDialog.this.close();
			}
		});
		Button btnCancel = new Button(composite, 0);
		btnCancel.setBounds(439, 358, 80, 27);
		btnCancel.setText("cancel");
		btnCancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				TemplateDialog.this.setReturnCode(1);
				TemplateDialog.this.close();
			}
		});
		composite.setLayout(null);
		composite.setLayoutData(new GridData(1808));

		applyDialogFont(composite);
		return composite;
	}

	public void addOKListener(Listener listener) {
		if (this.oklistener != null)
			return;
		this.oklistener = listener;
		getButton(0).addListener(0, this.oklistener);
	}

	protected Control createButtonBar(Composite parent) {
		return null;
	}

	protected Point getInitialSize() {
		return new Point(600, 460);
	}
}