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

import javax.swing.JScrollPane;

import jhelp.gui.FoldLocation;
import jhelp.gui.JHelpFoldablePanel;
import jhelp.gui.JHelpFrame;
import jhelp.gui.JHelpLimitSizePanel;

/**
 * Frame that show editor and list of compiled class.<br>
 * Minimum code here, the aim is the editor not linked to a frame to be easy to use in other projects
 *
 * @author JHelp <br>
 */
public class FrameEditorASM
      extends JHelpFrame
{
   /** Tree of compiled classes */
   private ClassesTree         classesTree;
   /** Byte code editor */
   private CompilerEditorPanel compilerEditorPanel;

   /**
    * Create a new instance of FrameEditorASM
    */
   public FrameEditorASM()
   {
      super("ASM editor", true, true);

   }

   /**
    * Add listeners <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    *
    * @see jhelp.gui.JHelpFrame#addListeners()
    */
   @Override
   protected void addListeners()
   {
      // Nothing to do
   }

   /**
    * Create components <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    *
    * @see jhelp.gui.JHelpFrame#createComponents()
    */
   @Override
   protected void createComponents()
   {
      this.compilerEditorPanel = new CompilerEditorPanel(null);
      this.classesTree = new ClassesTree(this.compilerEditorPanel);
   }

   /**
    * Layout components <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    *
    * @see jhelp.gui.JHelpFrame#layoutComponents()
    */
   @Override
   protected void layoutComponents()
   {
      this.setLayout(new BorderLayout());
      this.add(this.compilerEditorPanel, BorderLayout.CENTER);
      this.add(new JHelpFoldablePanel("Classes", new JHelpLimitSizePanel(new JScrollPane(this.classesTree), 256, Integer.MAX_VALUE), FoldLocation.RIGHT),
            BorderLayout.WEST);
   }
}