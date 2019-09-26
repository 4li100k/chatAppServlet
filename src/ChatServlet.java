import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.Inet4Address;
import java.util.LinkedList;
import java.util.List;

@WebServlet(name = "ChatServlet", urlPatterns = {"/Chat"}, asyncSupported = true)
public class ChatServlet extends HttpServlet
{
    private List<AsyncContext> contexts = new LinkedList<>();
    private String status = "";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String localAddress = Inet4Address.getLocalHost().getHostAddress();
        status=request.getHeader("referer");

        String usernameFromCookie = getUsernameFromCookie(request);

        if ( (status.equals("http://"+localAddress+":9999/Chat") && usernameFromCookie==null) || status.equals("http://localhost:9999/Chat") && usernameFromCookie==null)
            status="expired";

        if (status.equals("http://"+localAddress+":9999/Chat") || status.equals("http://localhost:9999/Chat")){
            String msg = request.getParameter("msg");
            String currentMessages = (String) request.getServletContext().getAttribute("message");

            String htmlMsg = "<p><b>" + usernameFromCookie + ": </b>" + msg + "<br/></p>";

            if (currentMessages != null) {
                currentMessages = htmlMsg + currentMessages;
            } else {
                currentMessages = htmlMsg;
            }

            if (msg.equals("CLEAR"))
                currentMessages="";
            if (msg.equals("FILE"))
                downloadFile(response, "s");

            request.getServletContext().setAttribute("message", currentMessages);
            System.out.println("amount of contexts before printing: "+contexts.size());

            for (AsyncContext context : contexts) {
                try (PrintWriter writer = context.getResponse().getWriter()) {
                    writer.println(currentMessages);
                    writer.flush();
                    context.complete();
                } catch (Exception ex) {

                }
            }
            System.out.println("amount of contexts after printing: "+contexts.size());
        }
        //LOGIN
        else if (status.equals("http://"+localAddress+":9999/") || status.equals("http://localhost:9999/")){
            System.out.println("login");
            Cookie userCookie = new Cookie("username", request.getParameter("username"));
            userCookie.setMaxAge(60*5);//5 minutes
            response.addCookie(userCookie);
            request.setAttribute("username", userCookie.getValue());
            request.getRequestDispatcher("/chat.jsp").forward(request, response);
        }else if (status.equals("expired")){
            request.getRequestDispatcher("/expired.jsp").forward(request, response);
            System.out.println("your cookie is expired so no cats for you");
        }else {
            System.out.println("YOU ARE");
            System.out.println(status);
        }
    }



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("get");
        final AsyncContext asyncContext = request.startAsync(request, response);
        asyncContext.setTimeout(60*60*1000);//after 1 hour the app won't receive any more messages
        contexts.add(asyncContext);
    }









    protected String protocol(String username ,String line) {
        String[] lineAr = line.split(" ");
        switch (lineAr[0]){
            case "DATA":{
                if (lineAr.length>2 && lineAr[1].length()>0 && lineAr[2].length()>0) {
                    String source = lineAr[1].replaceAll(":","");
                    if (source.equals(username)){
                        return line;
                    } else {
                        return "J_ER 1024: wrong sourcename";
                    }
                } else {return "J_ER 944948: send actually something pls";}
            }
            default:{
                return "J_ER 666: command not recognised";
            }
        }
    }



    protected String getUsernameFromCookie(HttpServletRequest request){

        Cookie[] cookies = request.getCookies();
        if (cookies!=null){
            for (int i = 0; i<cookies.length; i++){
                if (cookies[i].getName().equals("username"))
                    return cookies[i].getValue();
            }
        }
        return null;
    }

    protected void downloadFile(HttpServletResponse response, String file) throws IOException {
        //System.out.println("info: " + getServletContext().getServerInfo());
//response.setContentType("application/msword");
        System.out.println("download happening");
        response.setContentType("application/pdf");
        ServletOutputStream os = response.getOutputStream();
        InputStream is = getServletContext().getResourceAsStream("/autoexec.cfg");

        int read = 0;
        byte[] bytes = new byte[1024];

        System.out.println("this gets printed");
        while ((read = is.read(bytes)) != -1) {
            System.out.println("trying");
            os.write(bytes, 0, read);
        }

        System.out.println("4");
        os.flush();
        os.close();
        System.out.println("download successful");
    }
}