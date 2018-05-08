package org.insight.cd.models;

public class D3Node {
	private int id;
	private int group;
	
	public D3Node() {
		
	}
	
	public D3Node(int id, int group) {
		super();
		this.id = id;
		this.group = group;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}
	
	
	
}
