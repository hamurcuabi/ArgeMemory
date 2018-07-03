package com.emrehmrc.argememory.soap;

import android.util.Log;

import com.emrehmrc.argememory.helper.Utils;
import com.emrehmrc.argememory.model.MemberModel;
import com.emrehmrc.argememory.model.NotifCountModel;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class NotifCountSoap {


    private static final String NAMESPACE = "http://argememory.com/";
    private static final String METHODE = "CountNotification";
    private static final String SOAP_ACTION = "http://argememory.com/CountNotification";
    private static final String URL = "http://www.argememory.com/webservice/SystemNotification.asmx";
    private NotifCountModel notifCountModel;
    private SoapObject soapObject;
    private SoapSerializationEnvelope soapSerializationEnvelope;
    private HttpTransportSE httpsTransportSE;

    public NotifCountModel countNotif(String memberId) {

        notifCountModel = new NotifCountModel();
        soapObject = new SoapObject(NAMESPACE, METHODE);
        soapObject.addProperty("memberId", memberId);
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
                String count = responseMember.getProperty("countNotif").toString();
                notifCountModel = new NotifCountModel(count);

            }
        } catch (Exception ex) {
            Log.e("ERROR", ex.getMessage());
        }
        return notifCountModel;
    }

    public String countNotifString(String memberId) {

        String count="0";
        soapObject = new SoapObject(NAMESPACE, METHODE);
        soapObject.addProperty("memberId", memberId);
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
                 count = responseMember.getProperty("countNotif").toString();

            }
        } catch (Exception ex) {
            Log.e("ERROR", ex.getMessage());
        }
        return count;
    }


}
