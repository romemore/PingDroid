/*
 * Created by IntelliJ IDEA.
 * User: rme
 * Date: 07/02/13
 * Time: 11:39
 */
package com.nox.pingdroid.async.event;

public class MTaskResult {
    private byte[] result;
    private Exception error;


    public byte[] getResult() {
        return result;
    }

    public Exception getError() {
        return error;
    }


    public MTaskResult(byte[] result) {
        super();
        this.result = result;
    }


    public MTaskResult(Exception error) {
        super();
        this.error = error;
    }

    public boolean isError() {
        return error != null;
    }
}