package controllers;


import dao.UserDAO;


public class UserController {
    private UserDAO userDAO;

    public UserController() {
        userDAO = new UserDAO();
    }

    public boolean authenticateUser(String username, String password) {
        return userDAO.validateUser(username, password);
    }
}
