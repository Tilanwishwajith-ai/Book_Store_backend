/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.csacoursework;

import com.mycompany.csacoursework.ExceptionMappers.AuthorNotFoundExceptionMapper;
import com.mycompany.csacoursework.ExceptionMappers.BookNotFoundExceptionMapper;
import com.mycompany.csacoursework.ExceptionMappers.CartNotFoundExceptionMapper;
import com.mycompany.csacoursework.ExceptionMappers.CustomerNotFoundExceptionMapper;
import com.mycompany.csacoursework.ExceptionMappers.InvalidInputExceptionMapper;
import com.mycompany.csacoursework.ExceptionMappers.OutOfStockExceptionMapper;
import com.mycompany.csacoursework.Resource.AuthorResource;
import com.mycompany.csacoursework.Resource.BookResource;
import com.mycompany.csacoursework.Resource.CartResource;
import com.mycompany.csacoursework.Resource.CustomerResource;
import com.mycompany.csacoursework.Resource.OrderResource;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author Katana
 */


@ApplicationPath("/api")
public class BookstoreApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        
        // Resources
        classes.add(BookResource.class);
        classes.add(AuthorResource.class);
        classes.add(CustomerResource.class);
        classes.add(CartResource.class);
        classes.add(OrderResource.class);
        
        // Exception mappers
        classes.add(AuthorNotFoundExceptionMapper.class);
        classes.add(BookNotFoundExceptionMapper.class);
        classes.add(CartNotFoundExceptionMapper.class);
        classes.add(CustomerNotFoundExceptionMapper.class);
        classes.add(InvalidInputExceptionMapper.class);
        classes.add(OutOfStockExceptionMapper.class);
        
        return classes;
    }
}