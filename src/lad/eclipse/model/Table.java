package lad.eclipse.model;

import java.io.Serializable;

public class Table implements iDBObj , Serializable{
	private static final long serialVersionUID = 1L;
	private String name;
	private String comment;
	private Column[] children;
	private Column key;
	private Column foreignKey;
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

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Column[] getChildren() {
		return this.children;
	}

	public void setChildren(Column[] children) {
		this.children = children;
	}

	public Column getKey() {
		return this.key;
	}

	public void setKey(Column key) {
		this.key = key;
	}

	public Column getForeignKey() {
		return this.foreignKey;
	}

	public void setForeignKey(Column foreignKey) {
		this.foreignKey = foreignKey;
	}
}