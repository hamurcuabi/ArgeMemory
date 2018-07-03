package com.emrehmrc.argememory.soap;

import android.util.Log;

import com.emrehmrc.argememory.helper.Utils;
import com.emrehmrc.argememory.model.MemberModel;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class MemberLoginSoap {


    private static final String METHODE = "MemberLogin";
    private static final String SOAP_ACTION = "http://argememory.com/MemberLogin";
    private static final String URL = "http://www.argememory.com/webservice/member.asmx";
    public MemberModel memberModel;
    private SoapObject soapObject;
    private SoapSerializationEnvelope soapSerializationEnvelope;
    private HttpTransportSE httpsTransportSE;

    public MemberModel loginMember(String email, String pass) {

        memberModel = new MemberModel();
        soapObject = new SoapObject(Utils.NAMESPACE, METHODE);
        soapObject.addProperty("email", email);
        soapObject.addProperty("pass", pass);
        soapObject.addProperty("api", Utils.API_KEY);

        soapSerializationEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapSerializationEnvelope.dotNet = true;
        soapSerializationEnvelope.setOutputSoapObject(soapObject);

        httpsTransportSE = new HttpTransportSE(URL);
        httpsTransportSE.debug = true;

        try {

            httpsTransportSE.call(SOAP_ACTION, soapSerializationEnvelope);

            SoapObject response = (SoapObject) soapSerializationEnvelope.bodyIn;
            for (int i = 0; i < response.getPropertyCount(); i++) {
                SoapObject responseMember = (SoapObject) response.getProperty(i);
                String nameR = responseMember.getProperty("name").toString();
                String idR = responseMember.getProperty("id").toString();
                String emailR = responseMember.getProperty("email").toString();
                String compIdR = responseMember.getProperty("compID").toString();
                String imageR = responseMember.getProperty("image").toString();
                memberModel = new MemberModel(nameR, idR, emailR, compIdR, imageR);

            }
        } catch (Exception ex) {
            Log.e("ERROR", ex.getMessage());
        }
        return memberModel;
    }


}
