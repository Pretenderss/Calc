package servlet;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


//@WebServlet(name = "Calc", urlPatterns = "/calculate.html")
public class Calculate extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(Calculate.class.getName());

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html; charset=UTF-8");
		PrintWriter writer = new PrintWriter(response.getWriter(), true);
		String t = getHTML();
		try {
			writer.print(t);
		} catch (Exception ex) {
			log.error("Error when trying to send a response"+ex);
		} finally {
			writer.close();
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		boolean check1 = false;
		boolean check2 = false;
		boolean check3 = false;
		double operand1, operand2;
		double result = 0.0D;

		if (!isDigit(request.getParameter("op1"))) {
			check1 = true;
			log.warn("1-st operand, is not valid value");
		}
		if (!isDigit(request.getParameter("op2"))) {
			check2 = true;
			log.warn("2-st operand, is not valid value");
		}
		if (!isOperator(request.getParameter("operator"))) {
			check3 = true;
			log.warn("Operator, is not valid value");
		}
		if ((check1 || check2 || check3)) {
			if (request.getParameter("operator").matches("sqrt")) {
				result = Math.sqrt(Double.parseDouble(request
						.getParameter("op1")));
				check2 = false;
			} 
		} else {
			operand1 = Double.parseDouble(request.getParameter("op1"));
			operand2 = Double.parseDouble(request.getParameter("op2"));

			switch (request.getParameter("operator")) {
				case ("sqrt"): {
					result = Math.sqrt(operand1);
					break;
				}
				case ("+"): {
					result = operand1 + operand2;
					break;
				}
				case ("-"): {
					result = operand1 - operand2;
					break;
				}
				case ("*"): {
					result = operand1 * operand2;
					break;
				}
				case ("/"): {
					result = operand1 / operand2;
					break;
				}
			}
		}
		String res = "{"
				+ "\"result\":\"" + String.valueOf(result) + "\",\""
				+ "errors\":{" + "\"op1\":\"" + check1 + "\"," 
							   + "\"op2\":\""	+ check2 + "\","
							   + "\"operator\":\"" + check3 + "\"" 
							   + "}"
				+ "}";

		response.setContentType("application/json; charset=UTF-8");
		PrintWriter writer = new PrintWriter(response.getWriter(), true);

		try {
			writer.print(res);
		} catch (Exception ex) {
			log.error("error when trying to send a response"+ex);
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	public String getHTML() {

		String text;
		StringBuffer sb = new StringBuffer("");
		try {
			InputStreamReader isr = new InputStreamReader((getServletContext().getResourceAsStream("/WEB-INF/index.html")),Charset.forName("utf-8"));
			BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			isr.close();
			br.close();
		} catch (IOException | NullPointerException ex) {
			log.error("Can't read index.html, message error: "+ex);
		}

		text = sb.toString();
		return text;
	}

	public boolean isDigit(String string) {
		if (string == null)
			return false;
		return string.matches("[0-9]+|[0-9]+[\\.|\\,][0-9]+");
	}

	public boolean isOperator(String string) {
		if (string == null)
			return false;
		return string.matches("\\+|\\-|\\*|\\/|sqrt");
	}

	public boolean isDouble(String string) {
		if (string == null)
			return false;
		return string.matches("[0-9]+[\\.|\\,][0-9]+");
	}
}
