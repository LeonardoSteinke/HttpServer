/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package http.server.socket.Requests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author leona
 */
public class Forbidden implements ResponseStatus {

    @Override
    public void printFile(File file, OutputStream out) throws IOException {
        byte[] fileData = readFileData(file);

        out.write("HTTP/1.1 400 BAD REQUEST\r\n".getBytes());
        out.write(("Content-Length: " + (int) file.length() + "\r\n").getBytes());
        out.write(("Content-Type: " + getContentType(file.toString()) + "\r\n").getBytes());
        out.write("\r\n".getBytes());
        out.flush();

        out.write(fileData);
        out.flush();
    }

    @Override
    public byte[] readFileData(File file) throws IOException {
        FileInputStream fileIn = null;
        byte[] fileData = new byte[(int) file.length()];

        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        } finally {
            if (fileIn != null) {
                fileIn.close();
            }
        }
        return fileData;
    }

    @Override
    public String getContentType(String fileRequested) {
        if (fileRequested.endsWith(".htm") || fileRequested.endsWith(".html")) {
            return "text/html";
        } else if (fileRequested.endsWith(".mp4") || fileRequested.endsWith(".avi")) {
            return "video/mp4";
        } else if (fileRequested.endsWith(".mp3")) {
            return "audio/mpeg";
        } else {
            return "text/plain";
        }
    }

}
