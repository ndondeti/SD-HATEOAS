/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.asu.cse564.appealsserver.utilities;

import java.net.URI;

/**
 *
 * @author Vivek
 */
public class UriHelper {
    public static String getBaseUri(URI uri){
        String uriString = uri.toString();
        String baseURI   = uriString.substring(0, uriString.lastIndexOf("CSE564/")+"CSE564".length());        
        return baseURI;
    }
}
