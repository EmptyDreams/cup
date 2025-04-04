
package java_cup;
import java_cup.runtime.Symbol;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
public class ErrorManager{
    private static ErrorManager errorManager;
    private int errors = 0;
    private int warnings = 0;
    private int fatals = 0;
    public int getFatalCount() { return fatals; }
    public int getErrorCount() { return errors; }
    public int getWarningCount() { return warnings; }
    public static void clear() {
        errorManager = new ErrorManager();
    }
    public static ErrorManager getManager() { return errorManager; }
    private ErrorManager(){
    }

    //TODO: migrate to java.util.logging
    /**
     * Error message format: 
     * ERRORLEVEL at (LINE/COLUMN)@SYMBOL: MESSAGE
     * ERRORLEVEL : MESSAGE
     **/
    public void emit_fatal(String message){
        System.err.println("Fatal : "+message);
        fatals++;
    }
    public void emit_fatal(String message, Symbol sym){
        //System.err.println("Fatal at ("+sym.left+"/"+sym.right+")@"+convSymbol(sym)+" : "+message);
        System.err.println("Fatal: "+message+" @ "+sym);
        fatals++;
    }
    public void emit_warning(String message){
        System.err.println("Warning : " + message);
        warnings++;	
    }
    public void emit_warning(String message, Symbol sym){
//        System.err.println("Warning at ("+sym.left+"/"+sym.right+")@"+convSymbol(sym)+" : "+message);
        System.err.println("Fatal: "+message+" @ "+sym);
        warnings++;
    }
    public void emit_error(String message){
        System.err.println("Error : " + message);
        errors++;
    }
    public void emit_error(String message, Symbol sym){
//        System.err.println("Error at ("+sym.left+"/"+sym.right+")@"+convSymbol(sym)+" : "+message);
        System.err.println("Error: "+message+" @ "+sym);
        errors++;
    }
    private static String convSymbol(Symbol symbol){
        String result = (symbol.value == null)? "" : " (\""+ symbol.value +"\")";
        Field [] fields = sym.class.getFields();
        for (Field field : fields) {
            if (!Modifier.isPublic(field.getModifiers())) continue;
            try {
                if (field.getInt(null) == symbol.sym) return field.getName() + result;
            } catch (Exception ignored) {
            }
        }
        return symbol.toString()+result;
    }
    
}