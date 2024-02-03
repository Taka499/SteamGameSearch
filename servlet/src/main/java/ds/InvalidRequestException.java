/**
 * Author: George Saito (takatoms)
 * Last Modified: November 28th, 2022
 *
 * One of the exception class to handle the errors
 */
package ds;

public class InvalidRequestException extends RuntimeException{
    public InvalidRequestException (String errorMessage) {
        super(errorMessage);
    }
}
