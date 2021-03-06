package biz.waller.ad;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/*
MIT License

Copyright (c) 2017 Raphael Waller

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
@WebServlet(name = "PasswordGeneratorServlet")
public class PasswordGeneratorServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doAll(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doAll(request, response);
    }

    protected void doAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String menuOption = request.getParameter("Digits");
        System.out.println(menuOption);
        if (menuOption.equals("1")) { getPassword(response, request); }
        else if (menuOption.equals("2")) { doReport(response, request); }
        else if (menuOption.equals("3")) { repeatPassword(response, request); }
    }
protected void getPassword(HttpServletResponse response, HttpServletRequest request) throws IOException {
    int time = 0;
            System.out.println(System.currentTimeMillis());
    response.setContentType("text/xml;charset=UTF-8");
    PrintWriter writer = response.getWriter();
    writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    writer.println("<Response>");
    writer.println("<Gather timeout=\"25\" numDigits=\"1\">");
    writer.println("<Say>A new password is being generated. </Say> <Pause length=\"2\"/>");

    System.out.println(request.getParameter("CallSid"));
    String password = SessionHandler.createADPassword(request.getParameter("CallSid"));

    writer.print(passwordReaderTwiML(password));
    writer.flush();
    writer.close();
    System.out.println(System.currentTimeMillis());
    System.out.println(System.currentTimeMillis()- time);


}
protected void repeatPassword(HttpServletResponse response, HttpServletRequest request) throws IOException {
    response.setContentType("text/xml;charset=UTF-8");

    PrintWriter writer = response.getWriter();
    writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    writer.println("<Response>");
    writer.println("<Say>Your new password is. </Say> <Pause length=\"2\"/>");

    System.out.println(request.getParameter("CallSid"));
    String password = SessionHandler.getADPassword(request.getParameter("CallSid"));

    writer.print(passwordReaderTwiML(password));
    writer.flush();
    writer.close();

}
private String passwordReaderTwiML(String password) {
    String fullTwiML = "";
    for (int i=0; i<password.length(); i++) {
        char currentLetter = password.charAt(i);
        if (Character.isUpperCase(currentLetter)) {
            fullTwiML = fullTwiML + "<Say> Upper case " + currentLetter + " for " + phoneticAlphabet(currentLetter) + "</Say>";
        }
        else if (Character.isLowerCase(currentLetter)) {
            fullTwiML = fullTwiML + "<Say> Lower case " + currentLetter + " for " + phoneticAlphabet(currentLetter) + "</Say>";
        }
        else { fullTwiML = fullTwiML + "<Say>" + currentLetter + "</Say>"; }
        fullTwiML = fullTwiML + "<Pause length=\"1\"/>";
    }
    fullTwiML = fullTwiML + "<Gather timeout=\"10\" numDigits=\"1\">";
    fullTwiML = fullTwiML + "<Say>Press 1 to generate another password, or press 3 to hear it again. Otherwise hang up.</Say>";
    fullTwiML = fullTwiML + "</Gather>";
    fullTwiML = fullTwiML + "</Response>";

    return fullTwiML;
}
private void doReport(HttpServletResponse response, HttpServletRequest request) throws IOException {
    String samAccountName = SessionHandler.getSAMAccountName(request.getParameter("CallSid"));
    EmailNotifier.sendMessage(samAccountName, PropLoader.authAddress);
        //TODO Report to helpdesk feature
}
private static String phoneticAlphabet(Character inputChar) {
    HashMap<Character, String> phoneticMap= new HashMap<Character, String>();

    phoneticMap.put('a', "Alfa");
    phoneticMap.put('b', "Bravo");
    phoneticMap.put('c', "Charlie");
    phoneticMap.put('d', "Delta");
    phoneticMap.put('e', "Echo");
    phoneticMap.put('f', "Foxtrot");
    phoneticMap.put('g', "Golf");
    phoneticMap.put('h', "Hotel");
    phoneticMap.put('i', "India");
    phoneticMap.put('j', "Juliett");
    phoneticMap.put('k', "Kilo");
    phoneticMap.put('l', "Lima");
    phoneticMap.put('m', "Mike");
    phoneticMap.put('n', "November");
    phoneticMap.put('o', "Oscar");
    phoneticMap.put('p', "Papa");
    phoneticMap.put('q', "Quebec");
    phoneticMap.put('r', "Romeo");
    phoneticMap.put('s', "Sierra");
    phoneticMap.put('t', "Tango");
    phoneticMap.put('u', "Uniform");
    phoneticMap.put('v', "Victor");
    phoneticMap.put('w', "Whiskey");
    phoneticMap.put('x', "X-ray");
    phoneticMap.put('y', "Yankee");
    phoneticMap.put('z', "Zulu");

    inputChar = Character.toLowerCase(inputChar);
    return phoneticMap.get(inputChar);
}

}
