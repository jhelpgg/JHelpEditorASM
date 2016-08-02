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
package jhelp.asm.editor.renderer;

import java.awt.Component;
import java.lang.reflect.Method;

import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;

import jhelp.asm.editor.model.ClassInformation;
import jhelp.gui.JHelpLabel;
import jhelp.gui.smooth.JHelpConstantsSmooth;
import jhelp.util.text.UtilText;

/**
 * Renderer used for draw a cell in class tree
 *
 * @author JHelp <br>
 */
public class ClassesTreeCellRenderer
      extends JHelpLabel
      implements TreeCellRenderer
{

   /**
    * Create a new instance of ClassesTreeCellRenderer
    */
   public ClassesTreeCellRenderer()
   {
      this.setFont(JHelpConstantsSmooth.FONT_CAPTION.getFont());
   }

   /**
    * Update component to draw a cell <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    *
    * @param tree
    *           Tree where cell draw
    * @param value
    *           Value on cell
    * @param selected
    *           Indicates if cell is selected
    * @param expanded
    *           Indicates if cell is expanded
    * @param leaf
    *           Indicates if cell is a leaf
    * @param row
    *           Row where cell is draw
    * @param hasFocus
    *           Indicates if cell has focus
    * @return Updated component
    * @see javax.swing.tree.TreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean,
    *      boolean, int, boolean)
    */
   @Override
   public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean selected, final boolean expanded, final boolean leaf,
         final int row, final boolean hasFocus)
   {
      if(value == null)
      {
         this.setText("?null?");
      }
      else if(value instanceof String)
      {
         this.setText((String) value);
      }
      else if(value instanceof ClassInformation)
      {
         this.setText(((ClassInformation) value).getClassName());
      }
      else if(value instanceof Method)
      {
         final Method method = (Method) value;
         final StringBuilder stringBuilder = new StringBuilder(method.getName());
         stringBuilder.append("(");
         boolean first = true;

         for(final Class<?> parameter : method.getParameterTypes())
         {
            if(first == false)
            {
               stringBuilder.append(", ");
            }

            stringBuilder.append(parameter.getName());
            first = false;
         }

         stringBuilder.append(")");

         final Class<?> ret = method.getReturnType();

         if((ret != null) && (ret.equals(Void.class) == false))
         {
            stringBuilder.append(" : ");
            stringBuilder.append(ret.getName());
         }

         this.setText(stringBuilder.toString());
      }
      else
      {
         this.setText(UtilText.concatenate("?", value.getClass(), ":", value, "?"));
      }

      this.setSelected(selected);
      this.setFocused(hasFocus);
      return this;
   }
}