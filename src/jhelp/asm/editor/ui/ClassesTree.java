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

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Method;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import jhelp.asm.editor.event.CompilationListener;
import jhelp.asm.editor.model.ClassInformation;
import jhelp.asm.editor.model.ClassesTreeModel;
import jhelp.asm.editor.renderer.ClassesTreeCellRenderer;
import jhelp.compiler.instance.ClassManager;
import jhelp.util.debug.Debug;

/**
 * Tree that shows the list of compiled class and theire methods.<br>
 * A double click on method show a dialog for make a test
 *
 * @author JHelp <br>
 */
public class ClassesTree
      extends JTree
      implements CompilationListener, MouseListener
{
   /** Model of the tree */
   private final ClassesTreeModel classesTreeModel;
   /** Class manager used for compile */
   private final ClassManager     classManager;

   /**
    * Create a new instance of ClassesTree
    *
    * @param compilerEditorPanel
    *           Editor panel to attach (For receive compiled class)
    */
   public ClassesTree(final CompilerEditorPanel compilerEditorPanel)
   {
      this.classesTreeModel = new ClassesTreeModel();
      this.setModel(this.classesTreeModel);
      this.setCellRenderer(new ClassesTreeCellRenderer());
      this.setRootVisible(true);
      this.classManager = compilerEditorPanel.getClassManager();
      compilerEditorPanel.registerCompilationListener(this);
      this.addMouseListener(this);
   }

   /**
    * Open dialog for test a method
    *
    * @param classInformation
    *           Class description
    * @param method
    *           Method to test
    */
   private void openDialogFor(final ClassInformation classInformation, final Method method)
   {
      DialogLaunchMethod.showDialog(this, this.classManager, classInformation.getClassName(), method);
   }

   /**
    * Called when a compilation succeed <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    *
    * @param className
    *           Class compiled
    * @param classManager
    *           Class manager that compiled
    * @see jhelp.asm.editor.event.CompilationListener#compilationSucceed(java.lang.String, jhelp.compiler.instance.ClassManager)
    */
   @Override
   public void compilationSucceed(final String className, final ClassManager classManager)
   {
      try
      {
         final ClassInformation classInformation = new ClassInformation(className, classManager);
         this.classesTreeModel.insertReplace(classInformation);
      }
      catch(final Exception exception)
      {
         Debug.printException(exception, "Failed to add ", className, " inside the tree");
      }
   }

   /**
    * Called when mouse clicked inside the tree <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    *
    * @param mouseEvent
    *           Event description
    * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
    */
   @Override
   public void mouseClicked(final MouseEvent mouseEvent)
   {
      if(mouseEvent.getClickCount() > 1)
      {
         final TreePath path = this.getPathForLocation(mouseEvent.getX(), mouseEvent.getY());

         if(path != null)
         {
            final Object object = path.getLastPathComponent();

            if((object != null) && ((object instanceof Method) == true))
            {
               this.openDialogFor((ClassInformation) path.getParentPath().getLastPathComponent(), (Method) object);
            }
         }
      }
   }

   /**
    * Called when mouse entered inside the tree <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    *
    * @param mouseEvent
    *           Event description
    * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
    */
   @Override
   public void mouseEntered(final MouseEvent mouseEvent)
   {
      // Nothing to do
   }

   /**
    * Called when mouse exit from the tree <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    *
    * @param mouseEvent
    *           Event description
    * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
    */
   @Override
   public void mouseExited(final MouseEvent mouseEvent)
   {
      // Nothing to do
   }

   /**
    * Called when mouse pressed inside the tree <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    *
    * @param mouseEvent
    *           Event description
    * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
    */
   @Override
   public void mousePressed(final MouseEvent mouseEvent)
   {
      // Nothing to do
   }

   /**
    * Called when mouse released inside the tree <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    *
    * @param mouseEvent
    *           Event description
    * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
    */
   @Override
   public void mouseReleased(final MouseEvent mouseEvent)
   {
      // Nothing to do
   }
}