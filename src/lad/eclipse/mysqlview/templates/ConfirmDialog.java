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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ConfirmDialog extends Dialog {
	private String title;
	private iDialogDelegate delegate;

	public ConfirmDialog(Shell shell, String title,
			iDialogDelegate iDialogDelegate) {
		super(shell);
		this.title = title;
		this.delegate = iDialogDelegate;
	}

	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, 0);
		Text text_1 = new Text(composite, 2048);
		text_1.setBounds(117, 20, 239, 23);

		Label lblName = new Label(composite, 131072);
		lblName.setBounds(0, 23, 97, 17);
		lblName.setText(this.title + ":");

		composite.setLayout(null);
		composite.setLayoutData(new GridData(1808));

		Button btnCancel = new Button(composite, 0);
		btnCancel.setBounds(200, 50, 80, 27);
		btnCancel.setText("OK");
		btnCancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ConfirmDialog.this.delegate.run(text_1.getText());
				ConfirmDialog.this.setReturnCode(1);
				ConfirmDialog.this.close();
			}
		});
		applyDialogFont(composite);
		return composite;
	}

	protected Point getInitialSize() {
		return new Point(400, 200);
	}

	protected Control createButtonBar(Composite parent) {
		return null;
	}
}