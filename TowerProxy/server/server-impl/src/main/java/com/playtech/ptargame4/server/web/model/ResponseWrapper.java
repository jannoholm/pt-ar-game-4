package com.playtech.ptargame4.server.web.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.playtech.ptargame4.server.exception.SystemException;

import java.io.IOException;

public class ResponseWrapper {
    private static ObjectWriter writer = new ObjectMapper().writer();
    private Object data;

    public ResponseWrapper() {
        this(null);
    }

    public ResponseWrapper( Object data ) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public byte[] toBytes() {
        try {
            return writer.writeValueAsBytes(this);
        } catch (IOException e) {
            throw new SystemException("Unable to serialize object: " + data, e);
        }
    }

    @Override
    public String toString() {
        return "ResponseWrapper{" +
                "data=" + data +
                '}';
    }
}
