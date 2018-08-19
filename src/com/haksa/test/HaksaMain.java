package com.haksa.test;

import com.haksa.service.HaksaServiceImpl;

public class HaksaMain {
	
	static HaksaServiceImpl service = new HaksaServiceImpl();
	
	public static void main(String[] args) {
		
		while(true) {service.menu();}

	}

}
