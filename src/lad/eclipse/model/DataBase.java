package lad.eclipse.model;

import java.io.Serializable;

public class DataBase implements iDBObj, Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private Table[] children;
	private iDBObj parent;

	public iDBObj getParent() {
		return this.parent;
	}

	public void setParent(iDBObj parent) {
		this.parent = parent;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Table[] getChildren() {
		return this.children;
	}

	public void setChildren(Table[] children) {
		this.children = children;
	}

	public String toString() {
		String result = this.name + "{";
		for (Table table : this.children) {
			result = result + "\n\t" + table.getName() + ":";
			for (Column column : table.getChildren()) {
				result = result + "\n\t\t" + column.getName() + ","
						+ column.getType() + "," + column.getComment();
			}
		}
		result = result + "\n}";
		return result;
	}
}