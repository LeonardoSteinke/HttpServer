/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package http.server.socket.Requests;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author leona
 */
public interface ResponseStatus {

    void printFile(File file, OutputStream out) throws IOException;
    byte[] readFileData(File file) throws IOException;
    String getContentType(String fileRequested);

}
