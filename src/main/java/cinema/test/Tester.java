package cinema.test;

public class Tester {
    public static void main(String[] argv) throws Exception {
        new ApplicationServer(8080, new WelcomeServlet()).start();
    }
}
