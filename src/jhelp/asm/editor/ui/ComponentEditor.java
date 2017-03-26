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
package jhelp.asm.editor.ui;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

import jhelp.asm.editor.resources.EditorResources;
import jhelp.compiler.compil.CompilerConstants;
import jhelp.compiler.compil.OpcodeConstants;
import jhelp.gui.JHelpAutoStyledTextArea;
import jhelp.gui.JHelpSuggestion;
import jhelp.util.debug.Debug;

/**
 * Component that edit the byte code it self.<br>
 * Type byte code, put color on key words, show line number and stack trace error
 *
 * @author JHelp <br>
 */
public class ComponentEditor
      extends JHelpAutoStyledTextArea
{
   /** White not too bright for eyes saving */
   private static final Color WHITE = new Color(0xFFEEEEEE, true);

   /**
    * Create a new instance of ComponentEditor
    */
   public ComponentEditor()
   {
      // All we have to do here is too set the styles and list of key words
      // Other functionality are pilots/manage by CompilerEditorPane
      this.setBackground(ComponentEditor.WHITE);

      this.changeStyle(JHelpAutoStyledTextArea.DEFAULT_STYLE, "Arial", 24, false, false, false, Color.BLACK, ComponentEditor.WHITE);
      this.createStyle("KeyWord", "Arial", 24, true, false, false, new Color(0xFF30AE30, true), ComponentEditor.WHITE);
      this.createStyle("OpCode", "Arial", 24, true, false, false, new Color(0xFFAE3030, true), ComponentEditor.WHITE);
      this.createStyle("PrimitiveSymbol", "Arial", 24, true, false, false, new Color(0xFF3030AE, true), ComponentEditor.WHITE);
      this.createStyle("Comment", "Arial", 24, false, true, false, new Color(0xFF444444, true), ComponentEditor.WHITE);
      this.createStyle("String", "Arial", 24, false, true, false, new Color(0xFF224488, true), ComponentEditor.WHITE);
      final JHelpSuggestion<String> suggestion = new JHelpSuggestion<String>(this);

      this.associate("Comment", Pattern.compile("//.*"), 0);
      this.associate("Comment", Pattern.compile("/\\*([^*]|\\*[^/]|\\n)*\\*/"), 0);
      this.associate("Comment", Pattern.compile(";.*"), 0);
      this.associate("String", Pattern.compile("\".*\""), 0);
      this.associate("String", Pattern.compile("'.*'"), 0);

      this.setSymbolStyle("PrimitiveSymbol");
      this.associate("PrimitiveSymbol", "boolean", "char", "byte", "short", "int", "long", "float", "double");

      try
      {
         String word;

         for(final Field field : CompilerConstants.class.getDeclaredFields())
         {
            if(field.getName()
                    .startsWith("ACCES"))
            {
               continue;
            }

            word = (String) field.get(null);
            EditorResources.associate(word, suggestion);
            this.associate("KeyWord", word);
         }

         suggestion.addSuggestion("boolean", "primitive", "<html>Mapped as int on read/write.<br>Mapped as byte in memory</html>");
         suggestion.addSuggestion("char", "primitive", "<html>Mapped as int on read/write.<br>Unsigned expand on read<br>Truncated on write</html>");
         suggestion.addSuggestion("byte", "primitive", "<html>Mapped as int on read/write.<br>Signed expand on read<br>Truncated on write</html>");
         suggestion.addSuggestion("short", "primitive", "<html>Mapped as int on read/write.<br>Signed expand on read<br>Truncated on write</html>");
         suggestion.addSuggestion("int", "primitive", "");
         suggestion.addSuggestion("long", "primitive", "");
         suggestion.addSuggestion("float", "primitive", "");
         suggestion.addSuggestion("double", "primitive", "");

         suggestion.addSuggestion("this", "Auto reference", "Reference to this current object");
         this.associate("KeyWord", "this");
         this.associate("KeyWord", Pattern.compile("\\<init\\>"), 0);

         for(final Field field : OpcodeConstants.class.getDeclaredFields())
         {
            word = (String) field.get(null);
            EditorResources.associate(word, suggestion);
            this.associate("OpCode", word);
         }
      }
      catch(final Exception exception)
      {
         Debug.printException(exception, "Failed to get word list");
      }
   }
}