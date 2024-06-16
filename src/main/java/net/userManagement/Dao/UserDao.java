package net.userManagement.Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.xdevapi.PreparableStatement;
import com.mysql.cj.xdevapi.Result;

import net.userManagement.model.User;

//provides CRUD database operations for the table in the database

public class UserDao {
	
	private String jdbcURL = "jdbc:mysql://localhost:3306/demo?useSSL=false";
	private String jdbcUsername = "root";
	private String jdbcPassword = "12345";
	
	
	//SQL Statements
	private static final String INSERT_USER_SQL = "INSERT INTO users" + "(name, email, country) VALUES " + "(?, ?, ?);" ;
		
	private static final String SELECT_USER_BY_ID = "select id, name, email, country from users where id =?";
	
	private static final String SELECT_ALL_USERS = "select * from users"; 
	
	private static final String DELETE_USERS_SQL = "DELETE FROM users WHERE id = ?; "; 
	
	private static final String UPDATE_USERS_SQL = "update users set name = ?, email =?, country =? where id = ?;"; 

	
	protected Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	
	//insert new user
	public void insertUser(User user) throws SQLException {
		//Establishing a connection
		try(Connection connection = getConnection();
				//create statement using connection Object
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL)){
			
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setString(3, user.getCountry());
			//Execute the Query
			preparedStatement.executeUpdate();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//update user
	public boolean updateUser(User user) throws SQLException{
		
		boolean rowUpdated = false;
		try(Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL)){
			
			statement.setString(1, user.getName());
			statement.setString(2, user.getEmail());
			statement.setString(3, user.getCountry());
			statement.setInt(4, user.getId());
			
			rowUpdated = statement.executeUpdate() > 0;
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return rowUpdated;
	}
	
	
	//select user by id
	public User selectUser(int id) {
		User user = null;
	
		try (Connection connection= getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);){
			
			preparedStatement.setInt(1, id);
			System.out.println(preparedStatement);
			
			ResultSet rs = preparedStatement.executeQuery();
			
			while (rs.next()) {
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				user = new User(id, name, email, country);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return user;
	}
	
	
	//select users
	public List<User> selectAllUsers() {
		List<User> users = new ArrayList<>();
	
		try (Connection connection= getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);){

			System.out.println(preparedStatement);
			
			ResultSet rs = preparedStatement.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				users.add(new User(id, name, email, country));
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return users;
	}
	
	//delete user
	public boolean deleteUser(int id) throws SQLException{
		boolean rowDeleted;
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USERS_SQL);){
			preparedStatement.setInt(1, id);
			rowDeleted = preparedStatement.executeUpdate() > 0;
		} 
		return rowDeleted;
	}
	
	
	
	
	 private void printSQLException(SQLException ex) {
	        for (Throwable e: ex) {
	            if (e instanceof SQLException) {
	                e.printStackTrace(System.err);
	                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
	                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
	                System.err.println("Message: " + e.getMessage());
	                Throwable t = ex.getCause();
	                while (t != null) {
	                    System.out.println("Cause: " + t);
	                    t = t.getCause();
	                }
	            }
	        }
	        
	 }
}



