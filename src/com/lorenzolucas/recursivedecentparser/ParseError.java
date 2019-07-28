package com.lorenzolucas.recursivedecentparser;

/**
 * Creates an exception class for incorrect parser syntax
 *
 *
 * @author Lorenzo Lucas
 * @version 1.0
 * @since 2019-06-15
 */
public class ParseError extends Exception{
    ParseError(String message){
        super(message);
    }

}
