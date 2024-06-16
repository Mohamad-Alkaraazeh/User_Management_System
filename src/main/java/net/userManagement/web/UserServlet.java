package net.userManagement.web;

import java.io.IOException;
import java.security.KeyStore.PrivateKeyEntry;
import java.sql.SQLException;
import java.text.Normalizer.Form;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.midi.VoiceStatus;
import javax.sql.rowset.JoinRowSet;

import net.userManagement.Dao.UserDao;
import net.userManagement.model.User;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private UserDao userDao;

    public UserServlet() {
    	this.userDao = new UserDao();
    }


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String action = request.getServletPath();
		
		switch (action) {
		case "/new":
			showNewform(request, response);
			break;
			
		case "/insert":
			try {
				insertUser(request, response);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
			
		case "/delete":
			try {
				deleteUser(request, response);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
			
		case "/edit":
			try {
				showEditUser(request, response);
			} catch (SQLException e) {
				e.printStackTrace();
			} 
			break;
			
		case "/update":
			try {
				updateUser(request, response);
			} catch (SQLException e) {
				e.printStackTrace();
			} 
			break;
			
		default:
			try {
				listUser(request, response);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		}
	}

	
	//new case 
	private void showNewform(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");
		dispatcher.forward(request, response);
	}
	
	
	//insert case
	private void insertUser(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException{
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String country = request.getParameter("country");
		User newUser = new User(name, email, country);
		userDao.insertUser(newUser);
		response.sendRedirect("list");
	}
	
	
	//delete case 
	private void deleteUser(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException{
		int id = Integer.parseInt(request.getParameter("id"));
		userDao.deleteUser(id);
		response.sendRedirect("list");
	}

	
	//edit(Select_User_By_Id) request
	private void showEditUser(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException, ServletException{
		int id = Integer.parseInt(request.getParameter("id"));
		User existingUser = userDao.selectUser(id);
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");
		request.setAttribute("user", existingUser);
		dispatcher.forward(request, response);
	}
	
	
	//update case(save button)
	private void updateUser(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException{
		int id  =Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String country = request.getParameter("country");
		
		User user = new User(id, name, email, country);
		userDao.updateUser(user);
		response.sendRedirect("list");
	}
	
	//default case
	private void listUser(HttpServletRequest request, HttpServletResponse response) 
	throws SQLException, IOException, ServletException{
		List<User> listUser = userDao.selectAllUsers();
		request.setAttribute("listUser", listUser);
		RequestDispatcher dispatcher = request.getRequestDispatcher("user-list.jsp");
		dispatcher.forward(request, response);
 	}
}
