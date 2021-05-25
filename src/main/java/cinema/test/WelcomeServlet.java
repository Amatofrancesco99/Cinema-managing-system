package cinema.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WelcomeServlet extends HttpServlet {

	private ArrayList<Order> orders = new ArrayList<>();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		try {
			Scanner index = new Scanner(new File("index.html"));
			while (index.hasNextLine()) {
				String line = index.nextLine();
				resp.getWriter().write(line);
			}
			index.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		orders.add(new Order(req.getParameter("firstName"), req.getParameter("lastName"), req.getParameter("time"),
				req.getParameter("pizzas")));
		resp.sendRedirect("/");

	}

}
