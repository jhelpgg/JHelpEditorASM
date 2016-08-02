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
package jhelp.asm.editor.event;

import jhelp.compiler.instance.ClassManager;

/**
 * Listener of compilation events
 *
 * @author JHelp <br>
 */
public interface CompilationListener
{
   /**
    * Called when compilation succeed
    *
    * @param className
    *           Class compiled
    * @param classManager
    *           Class manager that compiled the class
    */
   public void compilationSucceed(String className, ClassManager classManager);
}