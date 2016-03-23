package com.example.exerciseapp.volley.toolbox;
import com.example.exerciseapp.volley.Response;

public interface ResponseListener<T> extends Response.ErrorListener,Response.Listener<T> {

}
