package validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 *
 * @author wesley
 * Contains methods for recurring validations
 */
public class GenericValidator {
    
    /**
     * Patterns that are used over again (names, addresses, zip codes etc.)
     */
    public static enum Patterns{
        REGULAR_TEXT("[a-zA-Z]{1,100}");
        
        private String pattern;
        
        Patterns(String pattern){
            this.pattern = pattern;
        }
        public String getPattern(){
            return pattern;
        }
    }
    
    /**
     * Check to see if an input matches a particular pattern
     * @param input         input to check for
     * @param p             the pattern that must be used
     * @return              true or false depending on match
     */
    public static boolean isValid(String input, Patterns p){
        Pattern pattern = Pattern.compile(p.getPattern());
        System.out.println("the item to match is: " + input);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
    
    /**
     * Check if an input is null or empty (spaces are not included)
     * @param input         input to check for
     * @return              true if empty
     */
    public static boolean isEmpty(String input){
        return input == null || input.trim().isEmpty();
    }
    
    /**
     * Checks whether an emailaddress is valid or not
     * @param emailAddress          emailaddress to check for
     * @return                      true if valid
     */
    public static boolean isValidEmailAddress(String emailAddress){
        boolean result = true;
        try{
            InternetAddress email = new InternetAddress(emailAddress);
            email.validate();
        }
        catch(AddressException ae){
            result = false;
        }
        return result;
    }
}