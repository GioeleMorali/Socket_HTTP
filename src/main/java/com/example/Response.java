package com.example;

import java.time.LocalDateTime;
import java.util.ArrayList;


public class Response {
    public String code;
    public String phrase;
    public String date;
    public ArrayList <String> data;
    public String protocol;
    public String body;
    public Response ()
    {
        this.code = "200";
        this.date = LocalDateTime.now().toString();
        this.protocol = "HTTP/1.1";
        this.phrase = "Ok";
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
        stringa = "Content-Type: text/html; charset=UTF-8" + "\n";
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

}
