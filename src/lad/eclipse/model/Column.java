package lad.eclipse.model;

import java.io.Serializable;

public class Column implements iDBObj, Serializable {
	private static final long serialVersionUID = 1L;
	public static int TYPE_INTEGER = 9;
	public static int TYPE_FLOAT = 1;
	public static int TYPE_DOUBLE = 2;
	public static int TYPE_DECIMAL = 3;
	public static int TYPE_BOOLEAN = 4;
	public static int TYPE_TIME = 5;
	public static int TYPE_TEXT = 6;
	public static int TYPE_BLOB = 7;
	public static int TYPE_CHAR = 8;
	public static int TYPE_OTHER = 0;
	private String name;
	private boolean key = false;
	private String comment;
	private int type;
	private iDBObj parent;
	private String typename;
	private String objName;

	public String getObjName(boolean isLower) {
		if (this.objName != null) {
			return this.objName;
		}
		char[] colNameChars = this.name.toCharArray();
		for (int i = 0; i < colNameChars.length; i++) {
			if ((isLower) && (i == 0) && (colNameChars[(i + 1)] <= 'z')
					&& (colNameChars[(i + 1)] >= 'a')) {
				colNameChars[0] = (char) (colNameChars[0] - ' ');
			}
			if ((colNameChars[i] != '_') || (i + 1 >= colNameChars.length)
					|| (colNameChars[(i + 1)] > 'z')
					|| (colNameChars[(i + 1)] < 'a'))
				continue;
			colNameChars[(i + 1)] = (char) (colNameChars[(i + 1)] - ' ');
		}

		this.objName = new String(colNameChars).replace("_", "");
		return this.objName;
	}

	public String getObjName() {
		return getObjName(false);
	}

	public String getTypeName() {
		return this.typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

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

	public boolean isKey() {
		return this.key;
	}

	public void setKey(boolean key) {
		this.key = key;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getType() {
		return this.type;
	}

	public void setType(int i) {
		this.type = i;
	}

	public iDBObj[] getChildren() {
		return null;
	}

	public String getTypeNameToStr() {
		if ((getType() == TYPE_CHAR) || (getType() == TYPE_TEXT)
				|| (getType() == TYPE_BLOB))
			return "String";
		if ((getType() == TYPE_INTEGER) || (getType() == TYPE_BOOLEAN))
			return "int";
		if ((getType() == TYPE_DOUBLE) || (getType() == TYPE_DECIMAL))
			return "double";
		if (getType() == TYPE_FLOAT)
			return "float";
		if (getType() == TYPE_TIME) {
			return "int";
		}
		return "Object";
	}
}