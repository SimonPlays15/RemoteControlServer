package org.json;

import java.io.Serializable;

public class JSONPointerException extends JSONException implements Serializable{
    private static final long serialVersionUID = 8872944667561856751L;

    public JSONPointerException(String message) {
        super(message);
    }

    public JSONPointerException(String message, Throwable cause) {
        super(message, cause);
    }

}
