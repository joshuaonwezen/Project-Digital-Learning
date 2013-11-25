package validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author wesley
 * Contains methods for recurring validations
 */
public class GenericValidator {
    
    /**
     * Patterns that are used over again (names, addresses, zip codes etc.)
     */
    public static enum Regex{
        USERNAME("[a-zA-Z]{1,100}"),
        REGULAR(".{1,100}"),
        //this uses another regex than on the client side because of the escape characters
        EMAIL_ADDRESS("[-0-9a-zA-Z.+_]+@[-0-9a-zA-Z.+_]+\\.[a-zA-Z]{2,4}"),
        PASSWORD(".{7,100}");
        
        private String regex;
        
        Regex(String regex){
            this.regex = regex;
        }
        public String getRegex(){
            return regex;
        }
    }
    
    /**
     * Check to see if an input matches a particular pattern
     * @param input         input to check for
     * @param regex         the pattern that must be used
     * @return              true or false depending on match
     */
    public static boolean isValid(String input, Regex regex){
        Pattern pattern = Pattern.compile(regex.getRegex());
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
}