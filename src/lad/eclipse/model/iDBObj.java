package lad.eclipse.model;

import java.io.Serializable;

public abstract interface iDBObj{
	public abstract String getName();

	public abstract iDBObj[] getChildren();

	public abstract iDBObj getParent();
}