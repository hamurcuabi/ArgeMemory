package com.emrehmrc.argememory.soap;

import android.util.Log;

import com.emrehmrc.argememory.helper.Utils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class InsertTagSoap {

    private static final String NAMESPACE = "http://argememory.com/";
    private static final String METHODE = "InsertShareTag";
    private static final String SOAP_ACTION = "http://argememory.com/InsertShareTag";
    private static final String URL = "http://www.argememory.com/webservice/Share.asmx";
    boolean isOk = true;
    private SoapObject soapObject;
    private SoapSerializationEnvelope soapSerializationEnvelope;
    private HttpTransportSE httpsTransportSE;

    public boolean insertTag(String shareId, String tagId) {

        soapObject = new SoapObject(NAMESPACE, METHODE);
        soapObject.addProperty("shareId", shareId);
        soapObject.addProperty("tagId", tagId);
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
