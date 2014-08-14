package lad.eclipse.mysqlview.templates;

import java.util.List;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class TemplateContentProvider implements IStructuredContentProvider,
		ITreeContentProvider {
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}

	public void dispose() {
	}

	public Object[] getElements(Object parent) {
		try {
			List<Template> lt = DocumentDll.getElements(parent);
			return lt.toArray(new Object[lt.size()]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Object[0];
	}

	public Object getParent(Object child) {
		Template element = (Template) child;
		return element.getParent();
	}

	public Object[] getChildren(Object parent) {
		Template element = (Template) parent;
		List<Template> elementlist = element.getChildren();
		return elementlist.toArray(new Object[elementlist.size()]);
	}

	public boolean hasChildren(Object parent) {
		Template element = (Template) parent;
		return element.hasChildren();
	}
}