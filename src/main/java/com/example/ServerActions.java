package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ServerActions {
    String arrayString [];
    DataOutputStream out;
    String stringaRicevuta;
    File f;
    
    ServerSocket server;
    BufferedReader in;
    Socket s;
    public ServerActions (){
        try {
            server = new ServerSocket(8080);
            System.out.println("Server in ascolto sulla porta 8080");
         } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Errore nella creazione del server");
        }
    }

    public void start()
    {
        Boolean exit = false;
        String ANSI_GREEN = "\033[1;32m";
        String ANSI_RESET = "\u001B[0m";
        try {
            while (!exit) {
                System.out.println(ANSI_GREEN + "Server Started" + ANSI_RESET);
                this.s = server.accept();
                this.in = new BufferedReader(new InputStreamReader(this.s.getInputStream()));
                this.out= new DataOutputStream(new DataOutputStream(s.getOutputStream()));
                try {
                    stringaRicevuta = in.readLine();
                    System.out.println(stringaRicevuta);
                    if (stringaRicevuta != null && !stringaRicevuta.isEmpty()) {
                        this.arrayString = stringaRicevuta.split(" ");
                        if (arrayString.length == 3 && arrayString[2].contains("HTTP")) {
                            f = new File("htdocs/" + arrayString[1]);
                            if (f.isDirectory()) {
                                f = new File("htdocs/file.html");
                            }
                            Response response = new Response();
                            if (f.exists()) {
                                response.setContentType(f);
                                sendResponse(response, f);
                            }else{
                                if (f.getPath().equals("htdocs/classe.json")) {
                                    System.out.println("SERIALIZZAZIONE");
                                    seriaDeseria(response);
                                }
                                else{
                                    File fileErrore = new File("404.html");
                                    response.setCode("404");
                                    response.setContentType(fileErrore);
                                    sendResponse(response, fileErrore);
                                    System.out.println("Errore: file non trovato");
                                }
                            }
                         }
                        } 
                    }catch (Exception e) {
                    System.out.println("Errore generico " + e.getMessage());
                    s.close();
                }
            }
        } catch (IOException e) {
            System.out.println("Errore generico " + e.getMessage());
        }
        try {
            s.close();
        } catch (IOException e) {
            System.out.println("Errore nella chiusura del socket");
        }
    }
    public void readFile(File f) throws IOException{
        InputStream input = new FileInputStream(f);
        byte[] buf = new byte[8192];
        int n;
        while ((n = input.read(buf)) != -1) {
            out.write(buf,0,n);
        }
        input.close();   
    }
    public void sendResponse(Response r, File f)
    {
        ArrayList<String> d = r.getData(f);
        for(int i = 0; i < d.size(); i++)
        {
            try {
                System.out.println(d.get(i));
                out.writeBytes(d.get(i));
            } catch (IOException e) {
                System.out.println("Errore risposta");
            }
        }
        try {
            readFile(f);
            out.writeBytes("" + "\n");
        } catch (Exception e) {
           System.out.println("Errore lettura file");
        }
        System.out.println("Risposta inviata");
    }
    public void close()
    {
        try {
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void seriaDeseria(Response response) throws StreamWriteException, DatabindException, IOException{
        Alunno a = new Alunno("Gioele", "Pannetto", new Date(2005, 10, 10));
        Alunno b = new Alunno("Lorenzo", "ABUBU", new Date(2005, 10, 10));
        Alunno c = new Alunno("Enrico", "ahaah", new Date(2005, 10, 10));
        Alunno d = new Alunno("Yang", "ezeze", new Date(2005, 10, 10));

        ArrayList<Alunno> alunni = new ArrayList<Alunno>();
        alunni.add(a);
        alunni.add(b);
        alunni.add(c);
        alunni.add(d);

        Classe c1 = new Classe(5, "A", "Aula 1", alunni);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File("/home/informatica/Socket_HTTP/htdocs/classe.json"), c1);
        response.setContentType(new File("/home/informatica/Socket_HTTP/htdocs/classe.json"));
        sendResponse(response, new File("/home/informatica/Socket_HTTP/htdocs/classe.json"));
        }
}
