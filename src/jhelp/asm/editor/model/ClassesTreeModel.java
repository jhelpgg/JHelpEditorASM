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
package jhelp.asm.editor.model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * Classes tree model
 *
 * @author JHelp <br>
 */
public class ClassesTreeModel
      implements TreeModel
{
   /** Root element */
   private static final String           ROOT = "List of classes";
   /** List of classes */
   private final List<ClassInformation>  classInformations;
   /** Listeners of model changes */
   private final List<TreeModelListener> treeModelListeners;

   /**
    * Create a new instance of ClassesTreeModel
    */
   public ClassesTreeModel()
   {
      this.treeModelListeners = new ArrayList<TreeModelListener>();
      this.classInformations = new ArrayList<ClassInformation>();
   }

   /**
    * Signal to listeners a class was just added
    */
   protected void fireClassAdded()
   {
      final int index = this.classInformations.size() - 1;
      final Object[] path =
      {
            ClassesTreeModel.ROOT
      };
      final int[] indexes =
      {
            index
      };
      final Object[] children =
      {
            this.classInformations.get(index)
      };
      final TreeModelEvent treeModelEvent = new TreeModelEvent(this, path, indexes, children);

      synchronized(this.treeModelListeners)
      {
         for(final TreeModelListener treeModelListener : this.treeModelListeners)
         {
            treeModelListener.treeNodesInserted(treeModelEvent);
         }
      }
   }

   /**
    * Signal to listeners that a class just changed
    *
    * @param index
    *           Class index
    */
   protected void fireClassChanged(final int index)
   {
      final Object[] path =
      {
            ClassesTreeModel.ROOT, this.classInformations.get(index)
      };

      final TreeModelEvent treeModelEvent = new TreeModelEvent(this, path, null, null);

      synchronized(this.treeModelListeners)
      {
         for(final TreeModelListener treeModelListener : this.treeModelListeners)
         {
            treeModelListener.treeStructureChanged(treeModelEvent);
         }
      }
   }

   /**
    * Register a listener to model events <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    *
    * @param treeModelListener
    *           Listener to register
    * @see javax.swing.tree.TreeModel#addTreeModelListener(javax.swing.event.TreeModelListener)
    */
   @Override
   public void addTreeModelListener(final TreeModelListener treeModelListener)
   {
      if(treeModelListener == null)
      {
         return;
      }

      synchronized(this.treeModelListeners)
      {
         if(this.treeModelListeners.contains(treeModelListener) == false)
         {
            this.treeModelListeners.add(treeModelListener);
         }
      }
   }

   /**
    * Obtain a child of an element <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    *
    * @param parent
    *           Element to have its child
    * @param index
    *           Child index to get
    * @return The child or null if element have no children
    * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
    */
   @Override
   public Object getChild(final Object parent, final int index)
   {
      if(parent == ClassesTreeModel.ROOT)
      {
         return this.classInformations.get(index);
      }

      if(parent instanceof ClassInformation)
      {
         return ((ClassInformation) parent).getMethod(index);
      }

      return null;
   }

   /**
    * Number of child for an element <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    *
    * @param parent
    *           Element to know the number of child
    * @return Children number
    * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
    */
   @Override
   public int getChildCount(final Object parent)
   {
      if(parent == ClassesTreeModel.ROOT)
      {
         return this.classInformations.size();
      }

      if(parent instanceof ClassInformation)
      {
         return ((ClassInformation) parent).numberOfMethods();
      }

      return 0;
   }

   /**
    * Index of a child inside a element <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    *
    * @param parent
    *           Element where search the child
    * @param child
    *           Child search
    * @return Child index OR -1 if not found
    * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object, java.lang.Object)
    */
   @Override
   public int getIndexOfChild(final Object parent, final Object child)
   {
      if((parent == null) || (child == null))
      {
         return -1;
      }

      if(parent == ClassesTreeModel.ROOT)
      {
         if(child instanceof ClassInformation)
         {
            return this.classInformations.indexOf(child);
         }
      }
      else if(parent instanceof ClassInformation)
      {
         if(child instanceof Method)
         {
            return ((ClassInformation) parent).indexOf((Method) child);
         }
      }

      return -1;
   }

   /**
    * The root <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    *
    * @return The root
    * @see javax.swing.tree.TreeModel#getRoot()
    */
   @Override
   public Object getRoot()
   {
      return ClassesTreeModel.ROOT;
   }

   /**
    * Insert or replace a class information.<br>
    * If a class with exact same complete name we replace it, else it is just an addition
    *
    * @param classInformation
    *           Class to add or replace
    */
   public void insertReplace(final ClassInformation classInformation)
   {
      if(classInformation == null)
      {
         throw new NullPointerException("classInformation musn't be null");
      }

      int index;

      synchronized(this.classInformations)
      {
         index = this.classInformations.indexOf(classInformation);

         if(index < 0)
         {
            this.classInformations.add(classInformation);
         }
         else
         {
            this.classInformations.set(index, classInformation);
         }
      }

      if(index < 0)
      {
         this.fireClassAdded();
      }
      else
      {
         this.fireClassChanged(index);
      }
   }

   /**
    * Indicates if node is a leaf <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    *
    * @param node
    *           Node tested
    * @return <code>true</code> if node is a leaf
    * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
    */
   @Override
   public boolean isLeaf(final Object node)
   {
      return(node instanceof Method);
   }

   /**
    * Unregister a listener from model events <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    *
    * @param treeModelListener
    *           Listener to unregister
    * @see javax.swing.tree.TreeModel#removeTreeModelListener(javax.swing.event.TreeModelListener)
    */
   @Override
   public void removeTreeModelListener(final TreeModelListener treeModelListener)
   {
      synchronized(this.treeModelListeners)
      {
         this.treeModelListeners.add(treeModelListener);
      }
   }

   /**
    * Called if tree edited by user <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    *
    * @param path
    *           Path where change happen
    * @param newValue
    *           New value a position indicates by the path
    * @see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath, java.lang.Object)
    */
   @Override
   public void valueForPathChanged(final TreePath path, final Object newValue)
   {
      // Nothing to do, because tree not editable by user
   }
}