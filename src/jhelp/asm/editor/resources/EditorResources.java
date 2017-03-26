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
package jhelp.asm.editor.resources;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import jhelp.gui.JHelpSuggestion;
import jhelp.util.debug.Debug;
import jhelp.util.list.Pair;
import jhelp.util.resources.Resources;

/**
 * Access to resources for Editor
 *
 * @author JHelp <br>
 */
public class EditorResources
{
   /** Map of key word, their information and their details. Used for suggestion */
   private static final Map<String, Pair<String, String>> KEY_WORDS;
   /** Access to resources */
   public static final Resources                          RESOURCES;

   static
   {
      RESOURCES = new Resources(EditorResources.class);
      final Map<String, Pair<String, String>> keyWords = new HashMap<String, Pair<String, String>>();
      BufferedReader bufferedReader = null;

      try
      {
         bufferedReader = new BufferedReader(new InputStreamReader(EditorResources.RESOURCES.obtainResourceStream("keywords.txt")));
         int index1, index2;
         String line = bufferedReader.readLine();

         while(line != null)
         {
            line = line.trim();
            index1 = line.indexOf('|');

            if(index1 > 0)
            {
               index2 = line.indexOf('|', index1 + 1);

               if(index2 > index1)
               {
                  keyWords.put(line.substring(0, index1), new Pair<String, String>(line.substring(index1 + 1, index2), line.substring(index2 + 1)));
               }
            }

            line = bufferedReader.readLine();
         }
      }
      catch(final Exception exception)
      {
         Debug.printException(exception, "Failed to read key words");
      }
      finally
      {
         if(bufferedReader != null)
         {
            try
            {
               bufferedReader.close();
            }
            catch(final Exception ignored)
            {
            }
         }
      }

      KEY_WORDS = Collections.unmodifiableMap(keyWords);
   }

   /**
    * Fill a suggestion for a key word
    *
    * @param keyWord
    *           Keyword to link to suggestion
    * @param suggestion
    *           List of suggestion where add the suggestion for key word
    */
   public static void associate(final String keyWord, final JHelpSuggestion<String> suggestion)
   {
      final Pair<String, String> association = EditorResources.KEY_WORDS.get(keyWord);

      if(association == null)
      {
         suggestion.addSuggestion(keyWord);
      }
      else
      {
         suggestion.addSuggestion(keyWord, association.element1, association.element2);
      }
   }
}