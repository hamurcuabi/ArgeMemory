package com.emrehmrc.argememory.soap;

import android.util.Log;

import com.emrehmrc.argememory.helper.Utils;
import com.emrehmrc.argememory.model.MainTaskModel;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class AddTagPopupInsertSoap {

    private static final String NAMESPACE = "http://argememory.com/";
    private static final String METHODE = "AddTag";
    private static final String SOAP_ACTION = "http://argememory.com/AddTag";
    private static final String URL = "http://www.argememory.com/webservice/Tag.asmx";
    private SoapObject soapObject;
    private SoapSerializationEnvelope soapSerializationEnvelope;
    private HttpTransportSE httpsTransportSE;

    public boolean insertTag(String compId,String name) {


        boolean isOk=false;
        soapObject = new SoapObject(NAMESPACE, METHODE);
        soapObject.addProperty("compId", compId);
        soapObject.addProperty("name", name);
        soapObject.addProperty("api", Utils.API_KEY);

        soapSerializationEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapSerializationEnvelope.dotNet = true;
        soapSerializationEnvelope.setOutputSoapObject(soapObject);

        httpsTransportSE = new HttpTransportSE(URL);
        httpsTransportSE.debug = true;

        try {

            httpsTransportSE.call(SOAP_ACTION, soapSerializationEnvelope);

            SoapObject response = (SoapObject) soapSerializationEnvelope.bodyIn;
            SoapObject responseTask = (SoapObject) response.getProperty(0);
            for (int i = 0; i < responseTask.getPropertyCount(); i++) {
                String ok = responseTask.getProperty(0).toString();
                if(ok=="true")isOk=true;

            }

        } catch (Exception ex) {
            isOk=false;
            Log.e("ERROR", ex.getMessage());
        }
        return isOk;
    }

}
