package lad.eclipse.mysqlview.templates;

import java.util.ArrayList;
import java.util.List;

public class Template {
	private String title;
	private String description;
	private String content;
	private Template parent;
	private List<Template> children = new ArrayList<Template>();;
	private int id;
	private int fid;
	
	public List<Template> getChildren() {
		return children;
	}
	public void addChildren(Template children) {
		children.setParent(this);
		children.setFid(id);
		this.children.add(children);
	}
	public void setChildren(List<Template> children) {
		this.children=children;
	}
	public boolean hasChildren() {
		return this.children.size()>0;
	}
	
	public Template getParent() {
		return parent;
	}
	public void setParent(Template parent) {
		this.fid = parent.getId();
		this.parent = parent;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getFid() {
		return fid;
	}
	public void setFid(int fid) {
		this.fid = fid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public boolean isCategory(){
		return this.fid == 0;
	}
	
	public void initArgs(){
		this.title = this.title==null?"":this.title.replace("'", "''");
		this.description = this.description==null?"":this.description.replace("'", "''");
		this.content = this.content==null?"":this.content.replace("'", "''");
	}

}
