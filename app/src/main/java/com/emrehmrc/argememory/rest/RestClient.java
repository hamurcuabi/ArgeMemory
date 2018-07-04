package com.emrehmrc.argememory.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.emrehmrc.argememory.rest.service.UploadService;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by Tan on 2/26/2016.
 */
public class RestClient {
    private UploadService  uploadService;
    private String URL ="http://fileupload.argememory.com/api/";

    public RestClient(){
            Gson localGson = new GsonBuilder().create();

            this.uploadService = ((UploadService)new RestAdapter.Builder()
            .setEndpoint(URL)
            .setConverter(new GsonConverter(localGson))
            .setRequestInterceptor(new RequestInterceptor()
            {
                public void intercept(RequestInterceptor.RequestFacade requestFacade)
                {
                    if (URL.contains("fileupload.argememory.com")) {
                        requestFacade.addHeader("Host", "fileupload.argememory.com");
                    }
                }
            })
            .build().create(UploadService.class));

    }



    public UploadService getService()
    {
        return this.uploadService;
    }


}
