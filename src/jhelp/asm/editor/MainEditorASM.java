package jhelp.asm.editor;

import jhelp.asm.editor.ui.FrameEditorASM;
import jhelp.util.gui.UtilGUI;

/**
 * Launch stand alone byte code editor
 *
 * @author JHelp <br>
 */
public class MainEditorASM
{
   /**
    * Launch the stand alone byte code editor
    *
    * @param args
    *           Unused
    */
   public static void main(final String[] args)
   {
      UtilGUI.initializeGUI();
      final FrameEditorASM frameEditorASM = new FrameEditorASM();
      frameEditorASM.setVisible(true);
   }
}