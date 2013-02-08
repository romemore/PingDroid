/*
 * Created by IntelliJ IDEA.
 * User: rme
 * Date: 06/02/13
 * Time: 16:57
 */
package com.nox.pingdroid.async;

public class MRequest {

    final String url;

    public MRequest(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}