package com.emrehmrc.argememory.soap;

import android.util.Log;

import com.emrehmrc.argememory.helper.Utils;
import com.emrehmrc.argememory.model.ShareCommentModel;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CommentPopupFillCommentSoap {

    private static final String METHODE = "GetComments";
    private static final String METHODE_INSERT = "AddComment";
    private static final String SOAP_ACTION = "http://argememory.com/GetComments";
    private static final String SOAP_ACTION_INSERT= "http://argememory.com/AddComment";
    private static final String URL = "http://www.argememory.com/webservice/Share.asmx";
    public ShareCommentModel shareCommentModel;
    public ArrayList<ShareCommentModel> shareCommentModelArrayList;
    private SoapObject soapObject;
    private SoapSerializationEnvelope soapSerializationEnvelope;
    private HttpTransportSE httpsTransportSE;
    private boolean isOk;

    public ArrayList<ShareCommentModel> getComment(String parentId) {

        shareCommentModel = new ShareCommentModel();
        shareCommentModelArrayList = new ArrayList<>();
        soapObject = new SoapObject(Utils.NAMESPACE, METHODE);
        soapObject.addProperty("parentId", parentId);
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
                SoapObject responseTask2 = (SoapObject) responseTask.getProperty(i);
                String commenter = responseTask2.getProperty("commenter").toString();
                String date = responseTask2.getProperty("date").toString();
                String clock = responseTask2.getProperty("clock").toString();
                String comment = responseTask2.getProperty("comment").toString();

                DateFormat formatter = new SimpleDateFormat("d.M.yyyy HH:mm:ss");
                Date datee = formatter.parse(date);

                formatter = new SimpleDateFormat("yyyy-MM-dd");
                String dt=formatter.format(datee);

                shareCommentModelArrayList.add(new ShareCommentModel(commenter, dt +" "+ clock,
                        comment));
            }

        } catch (Exception ex) {
            Log.e("ERROR", ex.getMessage());
        }
        return shareCommentModelArrayList;
    }

    public boolean insertComment(String compId, String parentId, String memberId, String name,
                                 String date, String clock) {

        soapObject = new SoapObject(Utils.NAMESPACE, METHODE_INSERT);
        soapObject.addProperty("compId", compId);
        soapObject.addProperty("parentId", parentId);
        soapObject.addProperty("memberId", memberId);
        soapObject.addProperty("name", name);
        soapObject.addProperty("date", date);
        soapObject.addProperty("clock", clock);
        soapObject.addProperty("api", Utils.API_KEY);

        soapSerializationEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapSerializationEnvelope.dotNet = true;
        soapSerializationEnvelope.setOutputSoapObject(soapObject);

        httpsTransportSE = new HttpTransportSE(URL);
        httpsTransportSE.debug = true;

        try {
            httpsTransportSE.call(SOAP_ACTION_INSERT, soapSerializationEnvelope);

            SoapObject response = (SoapObject) soapSerializationEnvelope.bodyIn;
            for (int i = 0; i < response.getPropertyCount(); i++) {
                String req = response.getProperty(0).toString();
                if(req.equals("true"))isOk=true;
                else isOk=false;

            }

        } catch (Exception ex) {

            Log.e("ERROR", ex.getMessage());
        }
        return isOk;


    }

}
