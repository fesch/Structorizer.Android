package lu.fisch.structorizer.helpers;

import java.io.Serializable;

public class Item implements Serializable{
	private String id;
	private String name;
	private String description;
	private String xml;
	
	public Item(String id, String name, String description, String xml)  
	{
		this.id=id;
		this.name=name;
		this.description=description;
		this.xml=xml;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getXml() {
		return xml;
	}
	public void setXml(String xml) {
		this.xml = xml;
	}
}
