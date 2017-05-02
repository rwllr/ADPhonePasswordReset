package biz.waller.ad;


import org.joda.time.DateTime;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

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
public class EmailNotifier {

    public static void sendMessage(String samAccountName, String toAddress) {
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", PropLoader.smtpServer);
        Session session = Session.getDefaultInstance(properties);
        try{
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(PropLoader.fromAddress));
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(toAddress));
            message.setSubject("Potential password abuse!");

            StringBuffer sb = new StringBuffer();
            sb.append("Hi there! \n");
            sb.append("We have a reported fraudulent request for a password reset. \n");
            sb.append("User: " + samAccountName+"\n");
            sb.append("Date: " + DateTime.now()+"\n");
            message.setText(sb.toString());

            // Send message
            Transport.send(message);
            System.out.println("message sent successfully....");

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException mex)
        {mex.printStackTrace();}

    }
}
