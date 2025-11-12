/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.csacoursework.ExceptionMappers;

/**
 *
 * @author Katana
 */



import com.mycompany.csacoursework.Exception.OutOfStockException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class OutOfStockExceptionMapper implements ExceptionMapper<OutOfStockException> {
    @Override
    public Response toResponse(OutOfStockException exception) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Out of Stock");
        error.put("message", exception.getMessage());
        
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
