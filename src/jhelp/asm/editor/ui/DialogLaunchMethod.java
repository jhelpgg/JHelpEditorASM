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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import jhelp.asm.editor.util.UtilEditor;
import jhelp.compiler.instance.ClassManager;
import jhelp.gui.action.GenericAction;
import jhelp.gui.smooth.JHelpConstantsSmooth;
import jhelp.util.debug.Debug;
import jhelp.util.gui.UtilGUI;

/**
 * Dialog for test a method
 *
 * @author JHelp <br>
 */
public class DialogLaunchMethod
      extends JDialog
{
   /**
    * Exit dialog action
    *
    * @author JHelp <br>
    */
   class ActionExit
         extends GenericAction
   {
      /**
       * Create a new instance of ActionExit
       */
      ActionExit()
      {
         super("Exit");
      }

      /**
       * Called when action trigger by user <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       *
       * @param actionEvent
       *           Event description
       * @see jhelp.gui.action.GenericAction#doActionPerformed(java.awt.event.ActionEvent)
       */
      @Override
      protected void doActionPerformed(final ActionEvent actionEvent)
      {
         DialogLaunchMethod.this.setVisible(false);
      }
   }

   /**
    * Action for launch the test
    *
    * @author JHelp <br>
    */
   class ActionTest
         extends GenericAction
   {
      /**
       * Create a new instance of ActionTest
       */
      ActionTest()
      {
         super("Test");
      }

      /**
       * Called when action trigger by user <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       *
       * @param actionEvent
       *           Event description
       * @see jhelp.gui.action.GenericAction#doActionPerformed(java.awt.event.ActionEvent)
       */
      @Override
      protected void doActionPerformed(final ActionEvent actionEvent)
      {
         DialogLaunchMethod.this.test();
      }
   }

   /**
    * Thread that make the dialog visible.<br>
    * We use a thread to not block UI if show called from UI thread
    *
    * @author JHelp <br>
    */
   static class ThreadSetVisible
         extends Thread
   {
      /**
       * Create a new instance of ThreadSetVisible
       */
      ThreadSetVisible()
      {
      }

      /**
       * Show the dialog <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       *
       * @see java.lang.Thread#run()
       */
      @Override
      public void run()
      {
         DialogLaunchMethod.dialogLaunchMethod.setVisible(true);
      }
   }

   /** Dialog title */
   private static final String TITLE = "Launch a method";
   /** Dialog instance */
   static DialogLaunchMethod   dialogLaunchMethod;

   /**
    * Show the dialog.<br>
    * If first time the dialog is created, else just show.<br>
    * The given component must be already attached to a frame, dialog or window
    *
    * @param component
    *           Component reference (The frame, dialog or window is attached will be searched and use)
    * @param classManager
    *           Class manager for find the class
    * @param className
    *           Class name to show
    * @param method
    *           Method on class to test
    */
   public static void showDialog(Component component, final ClassManager classManager, final String className, final Method method)
   {
      if(DialogLaunchMethod.dialogLaunchMethod == null)
      {
         // First time, search the attached frame, dialog or window to use appropriate constructor
         while(component != null)
         {
            // We search in priority frame and dialog, window is here just in case we don't find better match
            if(component instanceof Frame)
            {
               DialogLaunchMethod.dialogLaunchMethod = new DialogLaunchMethod((Frame) component, DialogLaunchMethod.TITLE, true);
               break;
            }

            if(component instanceof Dialog)
            {
               DialogLaunchMethod.dialogLaunchMethod = new DialogLaunchMethod((Dialog) component, DialogLaunchMethod.TITLE, true);
               break;
            }

            if(component instanceof Window)
            {
               DialogLaunchMethod.dialogLaunchMethod = new DialogLaunchMethod((Window) component, DialogLaunchMethod.TITLE);
               break;
            }

            component = component.getParent();
         }

         if(DialogLaunchMethod.dialogLaunchMethod == null)
         {
            throw new IllegalArgumentException("Given component not attached to frame, dialog or window !");
         }
      }

      // Initialize the dialog
      DialogLaunchMethod.dialogLaunchMethod.initializeDialog(classManager, className, method);
      // Show it
      final ThreadSetVisible threadSetVisible = new ThreadSetVisible();
      threadSetVisible.start();
   }

   /** Class manager to use */
   private ClassManager          classManager;
   /** Class name */
   private String                className;
   /** Label for title */
   private JLabel                labelTitle;
   /** Main panel */
   private JPanel                mainPanel;
   /** Tested method */
   private Method                method;
   /** Parameters' editor */
   private List<ParameterEditor> parameterEditors;
   /** panel parameters */
   private JPanel                parametersPanel;
   /** Result area */
   private JTextArea             result;

   /**
    * Create a new instance of DialogLaunchMethod with dialog owner
    *
    * @param owner
    *           Dialog owner
    * @param title
    *           Dialog title
    * @param modal
    *           Modal status
    */
   private DialogLaunchMethod(final Dialog owner, final String title, final boolean modal)
   {
      super(owner, title, modal);
   }

   /**
    * Create a new instance of DialogLaunchMethod with frame owner
    *
    * @param owner
    *           Frame owner
    * @param title
    *           Dialog title
    * @param modal
    *           Modal status
    */
   private DialogLaunchMethod(final Frame owner, final String title, final boolean modal)
   {
      super(owner, title, modal);
   }

   /**
    * Create a new instance of DialogLaunchMethod with window owner
    *
    * @param owner
    *           Window owner
    * @param title
    *           Dialog title
    */
   private DialogLaunchMethod(final Window owner, final String title)
   {
      super(owner, title);
   }

   /**
    * Initialize the dialog
    *
    * @param classManager
    *           Class manager
    * @param className
    *           Class name
    * @param method
    *           Method to test
    */
   void initializeDialog(final ClassManager classManager, final String className, final Method method)
   {
      this.classManager = classManager;
      this.className = className;
      this.method = method;

      if(this.mainPanel == null)
      {
         // First time, create fix components
         this.mainPanel = new JPanel(new BorderLayout());
         this.labelTitle = new JLabel("", SwingConstants.CENTER);
         this.labelTitle.setFont(JHelpConstantsSmooth.FONT_CAPTION.getFont());
         this.mainPanel.add(this.labelTitle, BorderLayout.NORTH);
         this.parameterEditors = new ArrayList<ParameterEditor>();
         this.parametersPanel = new JPanel();
         this.mainPanel.add(this.parametersPanel, BorderLayout.CENTER);
         this.result = new JTextArea("", 3, 32);
         this.result.setFont(JHelpConstantsSmooth.FONT_CAPTION.getFont());
         this.result.setEditable(false);
         final JPanel south = new JPanel(new BorderLayout());
         JButton button = new JButton(new ActionTest());
         button.setFont(JHelpConstantsSmooth.FONT_CAPTION.getFont());
         south.add(button, BorderLayout.WEST);
         south.add(this.result, BorderLayout.CENTER);
         button = new JButton(new ActionExit());
         button.setFont(JHelpConstantsSmooth.FONT_CAPTION.getFont());
         south.add(button, BorderLayout.EAST);
         this.mainPanel.add(south, BorderLayout.SOUTH);
         this.setContentPane(this.mainPanel);
      }

      // Clear previous launch (if any)
      this.labelTitle.setText(this.method.toString());
      this.result.setText("");
      this.parametersPanel.removeAll();
      this.parameterEditors.clear();

      // Create the parameters' editors
      final Class<?>[] parameters = method.getParameterTypes();
      final int length = parameters.length;

      if(length > 0)
      {
         final int numberColumn = (int) Math.ceil(length / Math.floor(Math.sqrt(length)));
         this.parametersPanel.setLayout(new GridLayout(0, numberColumn));
         ParameterEditor parameterEditor;

         for(final Class<?> parameter : parameters)
         {
            parameterEditor = new ParameterEditor(parameter);
            this.parameterEditors.add(parameterEditor);
            this.parametersPanel.add(parameterEditor.getComponent());
         }
      }

      // Refresh
      this.parametersPanel.invalidate();
      this.parametersPanel.repaint();
      this.parametersPanel.revalidate();

      // Resize and place the dialog
      UtilGUI.packedSize(this);
      UtilGUI.centerOnScreen(this);
   }

   /**
    * Launch the test with current parameters value
    */
   void test()
   {
      // Collect values
      final int size = this.parameterEditors.size();
      final Object[] values = new Object[size];

      for(int i = 0; i < size; i++)
      {
         values[i] = this.parameterEditors.get(i).getValue();
      }

      try
      {
         // Launch the method test
         final Object instance = this.classManager.newInstance(this.className);
         final Object result = this.classManager.invoke(instance, this.method.getName(), values);

         if(this.method.getReturnType().equals(Void.class) == true)
         {
            this.result.setText("Execution succeed !");
         }
         else if(result == null)
         {
            this.result.setText("null");
         }
         else
         {
            this.result.setText(result.toString());
         }
      }
      catch(final Exception exception)
      {
         Debug.printException(exception, "Failed to execute ", this.method);
         this.result.setText(UtilEditor.extractMessage(exception));
      }
      catch(final Error error)
      {
         Debug.printError(error, "Failed to execute ", this.method);
         this.result.setText(UtilEditor.extractMessage(error));
      }
   }
}