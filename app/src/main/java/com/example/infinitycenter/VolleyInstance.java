package com.example.infinitycenter;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyInstance {

    private static VolleyInstance volleyInstance ;
    private RequestQueue mRequestQueue;
    private Context mContext;

    private VolleyInstance(Context context) {
        mContext = context ;
        mRequestQueue = getmRequestQueue();
    }

    public static synchronized VolleyInstance getInstance(Context context){
        if(volleyInstance == null) {
            volleyInstance = new VolleyInstance(context);
        }
        return volleyInstance;
    }

    public RequestQueue getmRequestQueue(){
        if(this.mRequestQueue == null ) {
            this.mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return this.mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getmRequestQueue().add(request);
    }

}
