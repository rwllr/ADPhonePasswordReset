package biz.waller.ad;


import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.type.PhoneNumber;

import java.net.URI;

/**
 * Created by Raphael on 21/02/2017.
 */
public class MakeCall {

    // Find your Account Sid and Auth Token at twilio.com/console
    public static final String ACCOUNT_SID = PropLoader.twlio_Account_SID;
    public static final String AUTH_TOKEN = PropLoader.twilio_Auth_Token;

    public static String dial(String toNumber) {

        TwilioRestClient client = new TwilioRestClient.Builder(ACCOUNT_SID, AUTH_TOKEN).build();

        PhoneNumber to = new PhoneNumber(toNumber); // Load the number from AD you want to call with
        PhoneNumber from = new PhoneNumber(PropLoader.fromNumber); // Load a number you have permissions to call with Twilio from
        URI uri = URI.create(PropLoader.hostname + "/callxml.do");

        // Make the call
        Call call = Call.creator(to, from, uri).create(client);
        // Print the call SID (a 32 digit hex like CA123..)
        System.out.println(call.getSid());
        return call.getSid();
    }
}