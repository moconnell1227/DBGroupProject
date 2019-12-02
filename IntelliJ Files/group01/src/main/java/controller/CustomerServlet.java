package edu.calpoly.csc365.group01.controller;

import edu.calpoly.csc365.group01.dao.DaoManagerFactory;
import edu.calpoly.csc365.group01.dao.Dao;
import edu.calpoly.csc365.group01.dao.DaoManager;
import edu.calpoly.csc365.group01.entity.Customer;
import edu.calpoly.csc365.group01.service.AuthenticationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@WebServlet(name = "CustomerServlet", urlPatterns = "/customers")
public class CustomerServlet extends HttpServlet {

  private DaoManager dm;
  private Dao<Customer> customerDao;

  public CustomerServlet() throws Exception {
    dm = DaoManagerFactory.createDaoManager();
    customerDao = dm.getCustomerDao();
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    /*
    Cookie loginCookie = AuthenticationService.getLoginCookie(request);
    if (loginCookie == null) {
      response.sendRedirect("login");
    } else {
      response.addCookie(loginCookie);

     */
      Set<Customer> customers = customerDao.getAll();
      request.setAttribute("customers", customers);
      //request.setAttribute("message", "Hello " + loginCookie.getValue());
      request.getRequestDispatcher("customers.jsp").forward(request, response);
    }
  }
//}
