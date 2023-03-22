package com.arenatiket.android.model;

import java.util.List;

/**
 * Created by kahfi on 02/04/16.
 */

public class Response {

    private int status;
    private List results;

    public Response(int status, List results) {
        this.status = status;
        this.results = results;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List getResults() {
        return results;
    }

    public void setResults(List results) {
        this.results = results;
    }
}
