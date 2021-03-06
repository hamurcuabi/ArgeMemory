package com.emrehmrc.argememory.soap;

import android.util.Log;

import com.emrehmrc.argememory.helper.Utils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class InsertPersSoap {

    private static final String METHODE = "InsertShareMember";
    private static final String SOAP_ACTION = "http://argememory.com/InsertShareMember";
    private static final String URL = "http://www.argememory.com/webservice/Share.asmx";
    boolean isOk = true;
    private SoapObject soapObject;
    private SoapSerializationEnvelope soapSerializationEnvelope;
    private HttpTransportSE httpsTransportSE;

    public boolean insertPers(String shareId, String memberId) {

        soapObject = new SoapObject(Utils.NAMESPACE, METHODE);
        soapObject.addProperty("shareId", shareId);
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

            String ok = response.getProperty(0).toString();
            if (ok == "true") {
                isOk = true;
            }


        } catch (Exception ex) {
            Log.e("ERROR", ex.getMessage());
        }
        return isOk;
    }

}
