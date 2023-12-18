package com.example;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;


public class Response {
    public String code;
    public String phrase;
    public String date;
    public ArrayList <String> data;
    public String protocol;
    public String body;
    public String contentType;
    public Response ()
    {
        this.code = "200";
        this.date = LocalDateTime.now().toString();
        this.protocol = "HTTP/1.1";
        this.phrase = "";
        this.contentType = "";
        this.data = new ArrayList<String>();
        this.body = "";
    }

    public void sendResponse(boolean x)
    {  
    }
   public void setResponseCode(String code)
   {
    switch (code) {
        case "200":
            this.phrase = "Ok";
            this.code = code;
            break;
        case "404":
            this.phrase = "Not Found";
            this.code = code;
            this.body = "The resource was not found";
            break;
        case "500":
            this.phrase = "Internal Server Error";
            this.code = code;
            this.body = "";
            break;
        default:
            break;
    }
   }
     public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }
     public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
     public ArrayList<String> getData() {
        String stringa;
        stringa = protocol + " " + code + " " + phrase + "\n";
        data.add(stringa);
        stringa = "Date: " + date + " " + "\n";
        data.add(stringa);
        stringa = "Server: Gioele" + "\n";
        data.add(stringa);
        stringa = "Content-Type: " + getContentType() + ";charset=UTF-8" + "\n";
        data.add(stringa);
        stringa = "Content-Length: " + body.length() + "\n";
        data.add(stringa);
        stringa = "\n";
        data.add(stringa);
        if (body.length() > 0) {
            data.add(body);
            stringa = "\n";
            data.add(stringa);
        }
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }
    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;

    }
    public void setContentType(File f)
    {
        String [] s = f.getName().split("\\.");
        String ext = s[s.length -1];
        switch (ext)
        {
            case "html":
            case "htm":
            this.contentType = "text/html";
            break;

            case "jpeg":
            this.contentType = "image/jpeg";
            break;
            
            case "css":
            this.contentType = "text/css";
            break;
        }
    }
    public String getContentType(){
        return this.contentType;
    }
}
