package com.emrehmrc.argememory.soap;

import android.util.Log;

import com.emrehmrc.argememory.helper.Utils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class InsertShareSoap {


    private static final String METHODE = "InsertShare";
    private static final String SOAP_ACTION = "http://argememory.com/InsertShare";
    private static final String URL = "http://www.argememory.com/webservice/Share.asmx";
    private SoapObject soapObject;
    private SoapSerializationEnvelope soapSerializationEnvelope;
    private HttpTransportSE httpsTransportSE;
    boolean isOk=true;

    public boolean insertShare(String compId, String memberId, String name, String day, String
            clock,String uuid) {

        soapObject = new SoapObject(Utils.NAMESPACE, METHODE);
        soapObject.addProperty("compId", compId);
        soapObject.addProperty("memberId", memberId);
        soapObject.addProperty("day", day);
        soapObject.addProperty("clock", clock);
        soapObject.addProperty("name", name);
        soapObject.addProperty("uuid", uuid);
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
                if(ok=="true"){
                    isOk=true;
                }



        } catch (Exception ex) {
            Log.e("ERROR", ex.getMessage());
        }
        return isOk;
    }

}
