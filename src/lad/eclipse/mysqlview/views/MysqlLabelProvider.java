package lad.eclipse.mysqlview.views;

import lad.eclipse.model.Column;
import lad.eclipse.model.DataBase;
import lad.eclipse.model.Table;
import lad.eclipse.model.iDBObj;
import lad.eclipse.mysqlview.Activator;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;

public class MysqlLabelProvider extends ColumnLabelProvider implements
		IStyledLabelProvider {
	public String getText(Object element) {
		return getStyledText(element).toString();
	}

	public Image getImage(Object obj) {
		if ((obj instanceof DataBase))
			return Activator.getDefault().getImage("icons/database.gif");
		if ((obj instanceof Table))
			return Activator.getDefault().getImage("icons/prop_ps.png");
		if ((obj instanceof Column)) {
			if (((Column) obj).isKey()) {
				return Activator.getDefault().getImage("icons/key.png");
			}
			return Activator.getDefault().getImage("icons/c.gif");
		}
		return null;
	}

	public StyledString getStyledText(Object obj) {
		StyledString ss = new StyledString();
		try {
			if ((obj instanceof Column)) {
				Column column = (Column) obj;
				ss.append(column.getName() + " ");
				ss.append(column.getTypeName() + " ",
						StyledString.COUNTER_STYLER);
				ss.append(column.getComment(), StyledString.QUALIFIER_STYLER);
			} else if ((obj instanceof Table)) {
				Table table = (Table) obj;
				ss.append(table.getName() + " ");
				ss.append(new Integer(table.getChildren().length).toString()
						+ " ", StyledString.COUNTER_STYLER);
				ss.append(table.getComment(), StyledString.QUALIFIER_STYLER);
			} else if ((obj instanceof DataBase)) {
				DataBase database = (DataBase) obj;
				ss.append(database.getName() + " ");
				if (database.getChildren() != null)
					ss.append(
							new Integer(database.getChildren().length)
									.toString() + " tables ",
							StyledString.COUNTER_STYLER);
			} else {
				iDBObj dbobj = (iDBObj) obj;
				ss.append(dbobj.getName());
			}
		} catch (Exception localException) {
			ss.append(obj.toString());
		}
		return ss;
	}
}