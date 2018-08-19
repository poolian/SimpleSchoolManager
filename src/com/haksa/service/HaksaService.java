package com.haksa.service;

import com.haksa.data.HaksaDTO;

public interface HaksaService {
	
	// TODO 1
	// public static void ?
	// public default void ?
	
	public default void menu() {}
	public default void registerMenu() {}
	public default void register(HaksaDTO hdto) {}
	public default void findNameMenu() {}
	public default void findName(String name) {}
	public default void editMenu() {}
	public default void edit(HaksaDTO hdto) {}
	public default void deleteMenu() {}
	public default void delete(HaksaDTO hdto) {}
	public default void selectAll() {}
	public default void processExit() {}
	void con();	
}
