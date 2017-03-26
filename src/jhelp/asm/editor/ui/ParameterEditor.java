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

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import jhelp.gui.smooth.JHelpConstantsSmooth;

/**
 * Editor for a parameter.<br>
 * It chooses the component the show depends on parameter type.<br>
 * It collect the value
 *
 * @author JHelp <br>
 */
public class ParameterEditor
{
   /** Parameter type */
   enum ParameterType
   {
      /** Boolean */
      BOOLEAN,
      /** Byte */
      BYTE,
      /** Character */
      CHAR,
      /** Double */
      DOUBLE,
      /** Float */
      FLOAT,
      /** Integer */
      INT,
      /** Long */
      LONG,
      /** Generic object */
      OBJECT,
      /** Short */
      SHORT,
      /** String */
      STRING
   }

   /** List of of characters to choose in "character editor" */
   private static final Character[] CHARACTERS =
   {
         'a', 'b', 'c', 'A', 'B', 'C', '0', '5', '.', '/', '*'
   };
   /** Component to show */
   private       JComponent    component;
   /** Class of parameter to edit */
   private final Class<?>      parameterClass;
   /** Parameter type */
   private final ParameterType parameterType;

   /**
    * Create a new instance of ParameterEditor
    *
    * @param parameterClass
    *           Class of parameter to edit
    */
   public ParameterEditor(final Class<?> parameterClass)
   {
      this.parameterClass = parameterClass;

      if((boolean.class.equals(parameterClass)) || (Boolean.class.equals(parameterClass)))
      {
         this.parameterType = ParameterType.BOOLEAN;
      }
      else if((char.class.equals(parameterClass)) || (Character.class.equals(parameterClass)))
      {
         this.parameterType = ParameterType.CHAR;
      }
      else if((byte.class.equals(parameterClass)) || (Byte.class.equals(parameterClass)))
      {
         this.parameterType = ParameterType.BYTE;
      }
      else if((short.class.equals(parameterClass)) || (Short.class.equals(parameterClass)))
      {
         this.parameterType = ParameterType.SHORT;
      }
      else if((int.class.equals(parameterClass)) || (Integer.class.equals(parameterClass)))
      {
         this.parameterType = ParameterType.INT;
      }
      else if((long.class.equals(parameterClass)) || (Long.class.equals(parameterClass)))
      {
         this.parameterType = ParameterType.LONG;
      }
      else if((float.class.equals(parameterClass)) || (Float.class.equals(parameterClass)))
      {
         this.parameterType = ParameterType.FLOAT;
      }
      else if((double.class.equals(parameterClass)) || (Double.class.equals(parameterClass)))
      {
         this.parameterType = ParameterType.DOUBLE;
      }
      else if(String.class.equals(parameterClass))
      {
         this.parameterType = ParameterType.STRING;
      }
      else
      {
         this.parameterType = ParameterType.OBJECT;
      }

      switch(this.parameterType)
      {
         case BOOLEAN:
            this.component = new JCheckBox("", false);
         break;
         case BYTE:
         case DOUBLE:
         case FLOAT:
         case INT:
         case LONG:
         case SHORT:
            this.component = new JSpinner(new SpinnerNumberModel(0, -123, 123, 1));
         break;
         case CHAR:
            this.component = new JComboBox<Character>(ParameterEditor.CHARACTERS);
         break;
         case OBJECT:
            this.component = new JLabel("null", SwingConstants.CENTER);
         break;
         case STRING:
            this.component = new JTextField(16);
         break;
      }

      this.component.setFont(JHelpConstantsSmooth.FONT_CAPTION.getFont());
   }

   /**
    * Get the value for a number
    *
    * @return Value for number
    */
   private int obtainValueOfNumber()
   {
      final Object value = ((JSpinner) this.component).getValue();

      if((value == null) || (!(value instanceof Integer)))
      {
         return 0;
      }

      return (Integer) value;
   }

   /**
    * Component to show
    *
    * @return Component to show
    */
   public JComponent getComponent()
   {
      return this.component;
   }

   /**
    * Parameter class to edit
    *
    * @return Parameter class to edit
    */
   public Class<?> getParameterClass()
   {
      return this.parameterClass;
   }

   /**
    * Current parameter value
    *
    * @return Current parameter value
    */
   public Object getValue()
   {
      switch(this.parameterType)
      {
         case BOOLEAN:
            return ((JCheckBox) this.component).isSelected();
         case BYTE:
            return (byte) this.obtainValueOfNumber();
         case CHAR:
            //noinspection RedundantCast
            return (char) ((JComboBox<?>) this.component).getSelectedItem();
         case DOUBLE:
            return (double) this.obtainValueOfNumber();
         case FLOAT:
            return (float) this.obtainValueOfNumber();
         case INT:
            return this.obtainValueOfNumber();
         case LONG:
            return (long) this.obtainValueOfNumber();
         case OBJECT:
            return null;
         case SHORT:
            return (short) this.obtainValueOfNumber();
         case STRING:
            return ((JTextField) this.component).getText();
         default:
            return null;
      }
   }
}