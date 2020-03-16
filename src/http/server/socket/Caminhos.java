/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package http.server.socket;

/**
 *
 * @author leona
 */
public enum Caminhos {

    ROOT("public_html"),
    INDEX("index.html"),
    BAD_REQUEST("400.html"),
    FORBIDDEN("403.html"),
    FILE_NOT_FOUND("404.html"),
    TEAPOT("418.html"),
    NOT_IMPLEMENTED("501.html"),
    UNAVAILABLE("503.html");

    private String caminho;

    private Caminhos(String path) {
        this.caminho = path;
    }

    @Override
    public String toString() {
        return this.caminho;
    }

    public String getPath(String file) {
        return ROOT.toString() + file;
    }
}
