package lad.eclipse.model;

public abstract interface iDBObj{
	public abstract String getName();

	public abstract iDBObj[] getChildren();

	public abstract iDBObj getParent();
}