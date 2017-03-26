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

import jhelp.compiler.instance.ClassManager;

/**
 * Describe a class
 *
 * @author JHelp <br>
 */
public class ClassInformation
{
   /** Class name */
   private final String   className;
   /** Methods of class */
   private final Method[] methods;

   /**
    * Create a new instance of ClassInformation
    *
    * @param className
    *           Class name
    * @param classManager
    *           Class manager to use
    * @throws ClassNotFoundException
    *            If class name to found inside class manager
    */
   public ClassInformation(final String className, final ClassManager classManager)
         throws ClassNotFoundException
   {
      this.className = className;
      this.methods = classManager.listOfMethod(className);
   }

   /**
    * Indicates if given object equals to this class information <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    *
    * @param object
    *           Object to compare with
    * @return <code>true</code> if given object equals to this class information
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals(final Object object)
   {
      if(this == object)
      {
         return true;
      }

      if(null == object)
      {
         return false;
      }

      if(!object.getClass()
                .equals(ClassInformation.class))
      {
         return false;
      }

      return this.className.equals(((ClassInformation) object).className);
   }

   /**
    * Class name
    *
    * @return Class name
    */
   public String getClassName()
   {
      return this.className;
   }

   /**
    * Obtain a method
    *
    * @param index
    *           Method index
    * @return Method asked
    */
   public Method getMethod(final int index)
   {
      return this.methods[index];
   }

   /**
    * Search index of method
    *
    * @param method
    *           Method to have its index
    * @return Method index OR -1 if method not found
    */
   public int indexOf(final Method method)
   {
      for(int index = this.methods.length - 1; index >= 0; index--)
      {
         if(this.methods[index].equals(method))
         {
            return index;
         }
      }

      return -1;
   }

   /**
    * Number of methods
    *
    * @return Number of methods
    */
   public int numberOfMethods()
   {
      return this.methods.length;
   }
}