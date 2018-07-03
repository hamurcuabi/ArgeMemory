package com.emrehmrc.argememory.soap;

import android.util.Log;

import com.emrehmrc.argememory.helper.Utils;
import com.emrehmrc.argememory.model.NotifCountModel;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class ShareMemberPopupSoap {

    private static final String METHODE = "GetMemberByShareId";
    private static final String SOAP_ACTION = "http://argememory.com/GetMemberByShareId";
    private static final String URL = "http://www.argememory.com/webservice/Share.asmx";
    private SoapObject soapObject;
    private ArrayList<String> memberNames;
    private SoapSerializationEnvelope soapSerializationEnvelope;
    private HttpTransportSE httpsTransportSE;

    public ArrayList<String> shareMemberName(String shareId) {

        memberNames=new ArrayList<>();
        soapObject = new SoapObject(Utils.NAMESPACE, METHODE);
        soapObject.addProperty("shareId", shareId);
        soapObject.addProperty("api", Utils.API_KEY);

        soapSerializationEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapSerializationEnvelope.dotNet = true;
        soapSerializationEnvelope.setOutputSoapObject(soapObject);

        httpsTransportSE = new HttpTransportSE(URL);
        httpsTransportSE.debug = true;

        try {

            httpsTransportSE.call(SOAP_ACTION, soapSerializationEnvelope);

            SoapObject response = (SoapObject) soapSerializationEnvelope.bodyIn;
            SoapObject responseMember2 = (SoapObject) response.getProperty(0);
            for (int i = 0; i < responseMember2.getPropertyCount(); i++) {
                SoapObject responseMember = (SoapObject) responseMember2.getProperty(i);
                String name = responseMember.getProperty("name").toString();
                memberNames.add(name);


            }
        } catch (Exception ex) {
            Log.e("ERROR", ex.getMessage());
        }
        return memberNames;
    }


}
