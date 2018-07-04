package com.emrehmrc.argememory.rest.service;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;


public abstract interface UploadService {


    @Multipart
    @POST("/upload")
     abstract void upload(@Part("file") TypedFile paramTypedFile, Callback<Response> paramCallback);

    @Multipart
    @POST("/upload")
     abstract Response uploadSync(@Part("file") TypedFile paramTypedFile);
}
