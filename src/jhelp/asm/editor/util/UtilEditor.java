/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.asm.editor.util;

/**
 * Some utilities tools used in editor
 *
 * @author JHelp <br>
 */
public class UtilEditor
{
   /**
    * Extract message inside Throwable (Exception or Error)
    *
    * @param throwable
    *           Throwable to extract message
    * @return Extracted message
    */
   public static String extractMessage(Throwable throwable)
   {
      final StringBuilder stringBuilder = new StringBuilder();
      String message;

      while(throwable != null)
      {
         message = throwable.getMessage();

         if((message != null) && (message.length() > 0))
         {
            if(stringBuilder.length() > 0)
            {
               stringBuilder.append('\n');
            }

            stringBuilder.append(message);
         }

         throwable = throwable.getCause();
      }

      return stringBuilder.toString();
   }
}