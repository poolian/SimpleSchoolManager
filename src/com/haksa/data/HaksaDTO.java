package com.haksa.data;

public class HaksaDTO {
	
	//TODO 3
	String id;
	String name;
	int age;
	int vkey;
	String value;
	
	public HaksaDTO(String id, String name, int age, int vkey, String value) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.vkey = vkey;
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getVkey() {
		return vkey;
	}

	public void setVkey(int vkey) {
		this.vkey = vkey;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "name:" + name + " | age:" + age + " | " + value +"";
	}

}
