package com.sales.tracking.salestracking.Utility;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class GSONRequest<T> extends Request<T> {

    private Gson mGson = new Gson();
    private Class<T> clazz;
    private Map<String, String> headers;
    private Map<String, String> params;
    private Response.Listener<T> listener;

    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url   URL of the request to make
     * @param clazz Relevant class object, for Gson's reflection
     */
    public GSONRequest(String url, Class<T> clazz, Map<String, String> headers,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super (Method.GET, url, errorListener);
        this.clazz = clazz;
        this.headers = headers;
        this.listener = listener;
    }

    /**
     * Make a POST request and return a parsed object from JSON.
     * @param url      URL of the request to make
     * @param clazz    Relevant class object, for Gson's reflection
     * @param listener
     */
    public GSONRequest(int method,
                       String url,
                       Class<T> clazz,
                       Map<String, String> params,
                       Response.Listener<T> listener,
                       Response.ErrorListener errorListener) {

        super (method, url, errorListener);
        this.clazz = (Class<T>) clazz;
        this.params = params;
        this.listener = (Response.Listener<T>) listener;
        this.headers = params;
        mGson = new Gson ();
    }



    public static Cache.Entry parseIgnoreCacheHeaders (NetworkResponse response) {
        long now = System.currentTimeMillis ();

        Map<String, String> headers = response.headers;
        long serverDate = 0;
        String serverEtag = null;
        String headerValue;

        headerValue = headers.get ("Date");
        if (headerValue != null) {
            serverDate = HttpHeaderParser.parseDateAsEpoch (headerValue);
        }

        serverEtag = headers.get ("ETag");

        final long cacheHitButRefreshed = 10;//10 * 60 * 1000; // in 10 minutes cache will be
        // hit, but also refreshed on background
        final long cacheExpired = 10;//500 * 60 * 60 * 1000; // in 500 hours this cache entry
        // expires completely
        final long softExpire = now + cacheHitButRefreshed;
        final long ttl = now + cacheExpired;

        Cache.Entry entry = new Cache.Entry ();
        entry.data = response.data;
        entry.etag = serverEtag;
        entry.softTtl = softExpire;
        entry.ttl = ttl;
        entry.serverDate = serverDate;
        entry.responseHeaders = headers;

        return entry;
    }

    @Override
    public Map<String, String> getHeaders () throws AuthFailureError {
        if (params != null) {
            return params;
        } else {
            return headers != null ? headers : super.getHeaders ();
        }
    }

    @Override
    protected Map<String, String> getParams () throws AuthFailureError {
        return params;
    }

    @Override
    protected void deliverResponse (T response) {
        listener.onResponse (response);
    }

    @Override
    protected Response<T> parseNetworkResponse (NetworkResponse response) {
        try {
            String json = new String(
                    response.data, HttpHeaderParser.parseCharset (response.headers));
            return Response.success(
                    mGson.fromJson (json, clazz), parseIgnoreCacheHeaders (response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }catch (JsonSyntaxException e) {
            return Response.error (new ParseError (e));
        }
    }

}
