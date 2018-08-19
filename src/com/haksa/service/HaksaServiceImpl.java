package com.haksa.service;

import java.sql.*;
import java.util.*;

import com.haksa.data.HaksaDTO;

public class HaksaServiceImpl implements HaksaService{
	
//	String url = "jdbc:oracle:thin:@localhost:1521:xe";
//	String user = "hr";
//	String pass = "hr";
	
	String jdbcURL = "jdbc:mysql://localhost:3306/sys?serverTimezone=UTC&characterEncoding=utf8&verifyServerCertificate=false&useSSL=true";
	String jdbcID = "root";
	String jdbcPW = "9999";
	
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	Scanner sc = new Scanner(System.in);
	int key = 0;
	int dupName = 0;
	String[] key_name = {"null", "Student", "Teacher", "Worker"};
	String[] key_value = {"null", "stu_id", "subject", "dept"};
	
	ArrayList <HaksaDTO> findList = new ArrayList<HaksaDTO>(); 
	
	// TODO 2
	
	@Override
	public void menu() {
		System.out.println("----------------------");
		System.out.println("\n[School Manager Main]\n");
		System.out.println("1. Registration");
		System.out.println("2. Find");
		System.out.println("3. Edit");
		System.out.println("4. Delete");
		System.out.println("5. List of All");
		System.out.println("0. Exit\n");
		System.out.println("----------------------");
		int menu = sc.nextInt();
		switch(menu) {
		case 1: registerMenu(); break;
		case 2: findNameMenu(); break;
		case 3: editMenu(); break;
		case 4: deleteMenu(); break;
		case 5: selectAll(); break;
		case 0: processExit(); break;
		default : break;
		}	
	}

	@Override
	public void registerMenu() {
		System.out.println("[Registration] 1.Student 2.Teacher 3.Worker (0.back)");
		int menu = sc.nextInt();
		if(0<menu && menu<4) {
			//input name and create id
			dupName=0;
			key=menu;
			System.out.println("["+key_name[key] +" Info]");
			System.out.println("name:");
			String name = sc.next();
			String id = name+"00";
			findName(name);
			if(dupName>0 && dupName<10) {
				id=name+"0"+dupName;
			}else if(dupName>10) {
				id=name+""+dupName;
			}
			//input age
			System.out.println("age:");
			int age = sc.nextInt();
			//input value {student:number, teacher:subject, worker:dept}
			System.out.println(key_value[key]+":");
			String value = sc.next();
			
			HaksaDTO hdto = new HaksaDTO(id, name, age, key, value);
			register(hdto);
		}
	}
	
	@Override
	public void register(HaksaDTO hdto) {
		con();
		String sql = "insert into school_member values(?,?,?,?,?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, hdto.getId());
			pstmt.setString(2, hdto.getName());
			pstmt.setInt(3, hdto.getAge());
			pstmt.setInt(4, hdto.getVkey());
			pstmt.setString(5, hdto.getValue());
			
			System.out.println("name:"+hdto.getName()+" | age:"+hdto.getAge()+" | "+key_value[key]+":"+hdto.getValue());
			System.out.println("Do you want to register it? (y|n)");
			String yn = sc.next();
			if(yn.equals("y")||yn.equals("Y")) {
				System.out.println("Success!");
				pstmt.executeUpdate();
			}
			else System.out.println("Cancel!");
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			discon();
		}
	}

	@Override
	public void findNameMenu() {
		findList = new ArrayList<HaksaDTO>(); 
		System.out.println("[Find People]");
		System.out.print("name:");
		String name = sc.next();
		int num=1;
		findName(name);
		for(HaksaDTO find:findList) {
			System.out.println(num+". "+find.toString());
			num++;
		}
		if(dupName==0) {
			System.out.println("There is no results");
		}
	}
	
	@Override
	public void findName(String name) {
		findList = new ArrayList<HaksaDTO>(); 
		con();
		String nameLike = name + "%";
		String sql = "select * from school_member where name like ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, nameLike);
			rs=pstmt.executeQuery();
			while(rs.next()) {
				HaksaDTO hdto = new HaksaDTO(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getInt(4),rs.getString(5));
				findList.add(hdto);
				dupName = findList.size();
			}
		}catch(Exception e) {
			e.printStackTrace();
			discon();
		}
		discon();
	}
	
	@Override
	public void editMenu() {
		findList = new ArrayList<HaksaDTO>();
		System.out.println("[Edit Information]");
		System.out.print("name:");
		String name = sc.next();
		findName(name);
		int num=1;
		for(HaksaDTO find:findList) {
			System.out.println(num+". "+find.toString()); num++;
		}
		System.out.print("select:");
		int getIdx = sc.nextInt()-1;
		if(0<=getIdx && getIdx<findList.size()) {
			HaksaDTO hdto = findList.get(getIdx);
			boolean flag = true;
			while(flag) {
			System.out.println("\n * name:"+hdto.getName()+" | age:"+hdto.getAge()+" | "+key_value[hdto.getVkey()]+":"+hdto.getValue());
			System.out.println("\nWhich field would you like to change?");
			System.out.println("1.name 2.age 3."+key_value[hdto.getVkey()]+" (0.back)");
			int menu = sc.nextInt();
				switch(menu) {
				case 1:
					System.out.print(hdto.getName()+"-> :");
					hdto.setName(sc.next());
					break;
				case 2:
					System.out.print(hdto.getAge()+"-> :");
					hdto.setAge(sc.nextInt());
					break;
				case 3:
					System.out.println(hdto.getValue()+"-> :");
					hdto.setValue(sc.next());
					break;
				default : flag=false; break;
				}
				if(flag) {
					System.out.println("\nname:"+hdto.getName()+" | age:"+hdto.getAge()+" | "+key_value[hdto.getVkey()]+":"+hdto.getValue());
					System.out.println("Do you want to register it? (y|n)");
					String yn = sc.next();
					if(yn.equals("y")||yn.equals("Y")) {
						edit(hdto);
					}
					else{
						System.out.println("Cancel!");
						flag=false;
					}
				}
			}
		}
	}
	
	@Override
	public void edit(HaksaDTO hdto) {
		con();
		String sql = "update school_member set name=?,age=?,value=? where id=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, hdto.getName());
			pstmt.setInt(2, hdto.getAge());
			pstmt.setString(3, hdto.getValue());
			pstmt.setString(4, hdto.getId());
			pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}finally{
			discon();
		}
	}

	@Override
	public void deleteMenu() {
		System.out.println("[Delete People]");
		System.out.print("name:");
		String name = sc.next();
		int num=1;
		findName(name);
		for(HaksaDTO find:findList) {
			System.out.println(num+". "+find.toString());
			num++;
		}
		System.out.print("select:");
		int getIdx = (sc.nextInt()-1);
		if(0<=getIdx && getIdx<findList.size()) {
			System.out.println("\n * "+findList.get(getIdx).toString());
			System.out.println("\nDo you want to delete it? (y|n)");
			String yn = sc.next();
			if(yn.equals("y")||yn.equals("Y")) {
				delete(findList.get(getIdx));
				System.out.println("Success!");
			}
			else System.out.println("Cancel!");
		}
	}
	
	@Override
	public void delete(HaksaDTO hdto) {
		con();
		String sql = "delete from school_member where id=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, hdto.getId());
			pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			discon();
		}
	}

	@Override
	public void selectAll() {
		con();
		System.out.println("\n[School Member List]\n");
		String sql = "select * from school_member";
		try {
			pstmt = conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			while(rs.next()) {
				System.out.println(key_name[rs.getInt("vkey")].substring(0, 1)+" | name:"+rs.getString("name")+" | age:"+rs.getInt("age")+" | "+rs.getString("value"));
			}
		}catch(Exception e) {
			e.printStackTrace();
			
		}finally {
			discon();
		}
	}

	@Override
	public void processExit() {
		System.out.println("[Close School Manager]");
		System.exit(0);
	}

	public void con() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcURL, jdbcID, jdbcPW);
		}catch(ClassNotFoundException e) {
			System.out.println("Failed to Finding Driver");
			e.printStackTrace();
		}catch(SQLException e) {
			System.out.println("Failed to Connect");
			e.printStackTrace();
		}
	}
		
	public void discon() {
		try{
			if(rs!=null) {
				rs.close();
			}
			if(pstmt!=null) {
				pstmt.close();
			}
			conn.close();
		}catch(Exception e){
			System.out.println("Failed to Close Connection");
			e.printStackTrace();
		}
	}

}
