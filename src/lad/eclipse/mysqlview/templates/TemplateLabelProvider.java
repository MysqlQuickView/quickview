package lad.eclipse.mysqlview.templates;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;

public class TemplateLabelProvider extends ColumnLabelProvider implements
		IStyledLabelProvider {
	public String getText(Object element) {
		try {
			Template e = (Template) element;
			return e.getTitle();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return element.toString();
	}

	public Image getImage(Object obj) {
		return null;
	}

	public StyledString getStyledText(Object obj) {
		StyledString ss = new StyledString();
		Template element = (Template) obj;
		try {
			String description = element.getDescription();
			ss.append(element.getTitle() + " ");
			if ((description != null) && (!description.isEmpty()))
				ss.append(description, StyledString.QUALIFIER_STYLER);
		} catch (Exception localException) {
			ss.append(obj.toString());
		}
		return ss;
	}
}