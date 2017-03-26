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
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.sun.org.apache.bcel.internal.classfile.ClassFormatException;
import com.sun.org.apache.bcel.internal.generic.ClassGen;

import jhelp.asm.editor.event.CompilationListener;
import jhelp.asm.editor.util.UtilEditor;
import jhelp.compiler.compil.Compiler;
import jhelp.compiler.compil.CompilerException;
import jhelp.compiler.compil.StackInfo;
import jhelp.compiler.compil.StackInspectorException;
import jhelp.compiler.decompil.Decompiler;
import jhelp.compiler.instance.ClassManager;
import jhelp.compiler.instance.ClassManagerListener;
import jhelp.gui.ConsolePrintStream;
import jhelp.gui.FileChooser;
import jhelp.gui.FoldLocation;
import jhelp.gui.JHelpFoldablePanel;
import jhelp.gui.JHelpLimitSizePanel;
import jhelp.gui.JHelpSeparator;
import jhelp.gui.action.GenericAction;
import jhelp.gui.smooth.JHelpConstantsSmooth;
import jhelp.util.debug.Debug;
import jhelp.util.filter.FileFilter;
import jhelp.util.gui.UtilGUI;
import jhelp.util.io.UtilIO;
import jhelp.util.preference.Preferences;
import jhelp.util.text.UtilText;

/**
 * Compiler editor panel : <br>
 * Show the code, open/save it, show compilation result in success or failed, show console output if when test code System.out
 * invoke by the compiled code
 *
 * @author JHelp <br>
 */
public class CompilerEditorPanel
      extends JPanel
{
   /**
    * Action that compile the code.<br>
    * Key short cut : Ctrl+Shift+C
    *
    * @author JHelp <br>
    */
   class ActionCompile
         extends GenericAction
   {
      /**
       * Create a new instance of ActionCompile
       */
      ActionCompile()
      {
         super("Compile");
         this.setShortcut(UtilGUI.createKeyStroke('C', true, false, true));
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
         CompilerEditorPanel.this.compile();
      }
   }

   /**
    * Action : Export to ".class"<br>
    * Short cut : Ctrl + E
    *
    * @author JHelp <br>
    */
   class ActionExport
         extends GenericAction
   {
      /**
       * Create a new instance of ActionExport
       */
      ActionExport()
      {
         super("Export");
         this.setShortcut(UtilGUI.createKeyStroke('E', true));
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
         CompilerEditorPanel.this.exportClass();
      }
   }

   /**
    * Action : Import form ".class"<br>
    * Short cut : Ctrl + I
    *
    * @author JHelp <br>
    */
   class ActionImport
         extends GenericAction
   {
      /**
       * Create a new instance of ActionImport
       */
      ActionImport()
      {
         super("Import");
         this.setShortcut(UtilGUI.createKeyStroke('I', true));
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
         CompilerEditorPanel.this.importClass();
      }
   }

   /**
    * Create a new project (All code delete)<br>
    * Key short cut : Ctrl + N
    *
    * @author JHelp <br>
    */
   class ActionNew
         extends GenericAction
   {
      /**
       * Create a new instance of ActionNew
       */
      ActionNew()
      {
         super("New");
         this.setShortcut(UtilGUI.createKeyStroke('N', true));
      }

      /**
       * Called when action trigger by user <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       *
       * @param actionEvent
       *           Action description
       * @see jhelp.gui.action.GenericAction#doActionPerformed(java.awt.event.ActionEvent)
       */
      @Override
      protected void doActionPerformed(final ActionEvent actionEvent)
      {
         CompilerEditorPanel.this.newFile();
      }
   }

   /**
    * Action : open a file<br>
    * Short cut : Ctrl + O
    *
    * @author JHelp <br>
    */
   class ActionOpen
         extends GenericAction
   {
      /**
       * Create a new instance of ActionOpen
       */
      ActionOpen()
      {
         super("Open");
         this.setShortcut(UtilGUI.createKeyStroke('O', true));
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
         CompilerEditorPanel.this.open();
      }
   }

   /**
    * Action that save to a file.<br>
    * Short cut : Ctrl+S (save) or Ctrl+Shift+S (save as)
    *
    * @author JHelp <br>
    */
   class ActionSave
         extends GenericAction
   {
      /** Indicates if it is save as */
      private final boolean saveAs;

      /**
       * Create a new instance of ActionSave
       *
       * @param saveAs
       *           Indicates if it is save as
       */
      ActionSave(final boolean saveAs)
      {
         super("Save" + (saveAs
               ? " as"
               : ""));
         this.saveAs = saveAs;
         this.setShortcut(UtilGUI.createKeyStroke('S', true, false, this.saveAs));
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
         CompilerEditorPanel.this.save(this.saveAs);
      }
   }

   /**
    * Event manager of compilation events
    *
    * @author JHelp <br>
    */
   class EventManager
         implements ClassManagerListener
   {
      /**
       * Create a new instance of EventManager
       */
      EventManager()
      {
      }

      /**
       * Called if compilation failed for known reason <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       *
       * @param classManager
       *           Class manager where compilation done
       * @param compilationID
       *           Compilation ID
       * @param compilerException
       *           Know reason
       * @see jhelp.compiler.instance.ClassManagerListener#compilationIssue(jhelp.compiler.instance.ClassManager, int,
       *      jhelp.compiler.compil.CompilerException)
       */
      @Override
      public void compilationIssue(final ClassManager classManager, final int compilationID, final CompilerException compilerException)
      {
         CompilerEditorPanel.this.reportCompilerException(compilerException);
      }

      /**
       * Called when compilation finished (With success or not) <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       *
       * @param classManager
       *           Class manager that compiled
       * @param compilationID
       *           Compilation ID
       * @param classesName
       *           List of success full compiled class
       * @see jhelp.compiler.instance.ClassManagerListener#compilationReady(jhelp.compiler.instance.ClassManager, int,
       *      java.util.List)
       */
      @Override
      public void compilationReady(final ClassManager classManager, final int compilationID, final List<String> classesName)
      {
         // Since, here we compile one by one if zero means fail, else one compiled
         if(classesName.size() == 0)
         {
            CompilerEditorPanel.this.endCompilation();
         }
         else
         {
            CompilerEditorPanel.this.reportCompileSucceed(classesName.get(0));
         }
      }

      /**
       * Called when a serious error happen while compile <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       *
       * @param classManager
       *           Class manager compiled
       * @param compilationID
       *           Compilation ID
       * @param error
       *           Error happen
       * @see jhelp.compiler.instance.ClassManagerListener#errorHappen(jhelp.compiler.instance.ClassManager, int,
       *      java.lang.Error)
       */
      @Override
      public void errorHappen(final ClassManager classManager, final int compilationID, final Error error)
      {
         Debug.printError(error, "Compilation failed !");
         CompilerEditorPanel.this.reportCompileFailed(UtilEditor.extractMessage(error));
      }

      /**
       * Called when an unknown issue happen <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       *
       * @param classManager
       *           Class manager that compiled
       * @param compilationID
       *           Compilation ID
       * @param exception
       *           Unknown issue
       * @see jhelp.compiler.instance.ClassManagerListener#exceptionHappen(jhelp.compiler.instance.ClassManager, int,
       *      java.lang.Exception)
       */
      @Override
      public void exceptionHappen(final ClassManager classManager, final int compilationID, final Exception exception)
      {
         Debug.printException(exception, "Compilation failed !");
         CompilerEditorPanel.this.reportCompileFailed(UtilEditor.extractMessage(exception));
      }
   }

   /** Background color give to line number where compilation failed */
   private static final Color              COLOR_ERROR_LINE          = new Color(0xFFFF8844, true);
   /** Regular expression for capture the class declaration. In group 1 : the name of the class */
   private static final Pattern            PATTERN_CLASS_DECLARATION = Pattern.compile("^\\s*class\\s+([a-zA-Z0-9._]+)", Pattern.MULTILINE);
   /** Key in preferences for store last directory */
   private static final String             PREFERENCE_LAST_DIRECTORY = "jhelp.asm.editor.ui.CompilerEditorPanel.lastDirectory";
   /** Key in preferences for store last open file */
   private static final String             PREFERENCE_LAST_FILE      = "jhelp.asm.editor.ui.CompilerEditorPanel.lastFile";

   /** Compile action */
   private final ActionCompile             actionCompile;
   /** Export file action */
   private final ActionExport              actionExport;
   /** Import file action */
   private final ActionImport              actionImport;
   /** New file action */
   private final ActionNew                 actionNew;
   /** Open file action */
   private final ActionOpen                actionOpen;
   /** Save action */
   private final ActionSave                actionSave;
   /** Save as action */
   private final ActionSave                actionSaveAs;
   /** Class manager for compile */
   private final ClassManager              classManager;
   /** Listeners of compilation result */
   private final List<CompilationListener> compilationListeners;
   /** Indicates if something is compiling */
   private final AtomicBoolean             compiling;
   /** Editor for type the code */
   private final ComponentEditor           componentEditor;
   /** Current open file */
   private File                            currentFile;
   /** Event manager */
   private final EventManager              eventManager;
   /** Dialog for choose a file */
   private final FileChooser               fileChooser;
   /** Filter on ASM files */
   private final FileFilter                fileFilterASM;
   /** Filter on ".class" files */
   private final FileFilter                fileFilterClass;
   /** Text for print information */
   private final JTextArea                 informationMessage;
   /** Panel that contains actions */
   private final JPanel                    panelActions;
   /** Panel that contains information message */
   private final JHelpFoldablePanel        panelInformationMessage;
   /** Preferences where store/read */
   private final Preferences               preferences;
   /** Title label */
   private final JLabel                    title;

   /**
    * Create a new instance of CompilerEditorPanel
    *
    * @param preferences
    *           Preferences to use. If <code>null</code> it creates default preferences and use it
    */
   public CompilerEditorPanel(Preferences preferences)
   {
      super(new BorderLayout());

      if(preferences == null)
      {
         preferences = new Preferences(UtilIO.obtainExternalFile("JHelp/compilerASM/compilerPreference.pref"));
      }

      this.preferences = preferences;
      this.compilationListeners = new ArrayList<CompilationListener>();
      this.compiling = new AtomicBoolean(false);
      this.actionNew = new ActionNew();
      this.actionOpen = new ActionOpen();
      this.actionSave = new ActionSave(false);
      this.actionSaveAs = new ActionSave(true);
      this.actionImport = new ActionImport();
      this.actionExport = new ActionExport();
      this.actionCompile = new ActionCompile();
      this.eventManager = new EventManager();
      this.classManager = new ClassManager();

      // Short cuts
      final ActionMap actionMap = this.getActionMap();
      final InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

      actionMap.put(this.actionNew.getName(), this.actionNew);
      inputMap.put(this.actionNew.getShortcut(), this.actionNew.getName());

      actionMap.put(this.actionOpen.getName(), this.actionOpen);
      inputMap.put(this.actionOpen.getShortcut(), this.actionOpen.getName());

      actionMap.put(this.actionSave.getName(), this.actionSave);
      inputMap.put(this.actionSave.getShortcut(), this.actionSave.getName());

      actionMap.put(this.actionSaveAs.getName(), this.actionSaveAs);
      inputMap.put(this.actionSaveAs.getShortcut(), this.actionSaveAs.getName());

      actionMap.put(this.actionImport.getName(), this.actionImport);
      inputMap.put(this.actionImport.getShortcut(), this.actionImport.getName());

      actionMap.put(this.actionExport.getName(), this.actionExport);
      inputMap.put(this.actionExport.getShortcut(), this.actionExport.getName());

      actionMap.put(this.actionCompile.getName(), this.actionCompile);
      inputMap.put(this.actionCompile.getShortcut(), this.actionCompile.getName());
      //

      this.fileChooser = new FileChooser();
      this.fileFilterASM = new FileFilter();
      this.fileFilterASM.addExtension("asm");
      this.fileFilterClass = new FileFilter();
      this.fileFilterClass.addExtension("class");
      this.fileChooser.setFileFilter(this.fileFilterASM);

      final File directory = this.preferences.getFileValue(CompilerEditorPanel.PREFERENCE_LAST_DIRECTORY);

      if(directory != null)
      {
         this.fileChooser.setStartDirectory(directory);
      }

      this.componentEditor = new ComponentEditor();

      this.panelActions = new JPanel(new FlowLayout(FlowLayout.LEFT));
      this.title = new JLabel("--Untitled--");
      this.panelActions.add(this.title);
      this.panelActions.add(new JHelpSeparator(false));
      this.panelActions.add(new JButton(this.actionNew));
      this.panelActions.add(new JButton(this.actionOpen));
      this.panelActions.add(new JButton(this.actionSave));
      this.panelActions.add(new JButton(this.actionSaveAs));
      this.panelActions.add(new JHelpSeparator(false));
      this.panelActions.add(new JButton(this.actionImport));
      this.panelActions.add(new JButton(this.actionExport));
      this.panelActions.add(new JHelpSeparator(false));
      this.panelActions.add(new JButton(this.actionCompile));

      this.informationMessage = new JTextArea("", 500, 16);
      this.informationMessage.setEditable(false);
      this.informationMessage.setFont(JHelpConstantsSmooth.FONT_BODY_1.getFont());
      this.informationMessage.setLineWrap(true);

      this.panelInformationMessage = new JHelpFoldablePanel("Message",
            new JHelpLimitSizePanel(new JScrollPane(this.informationMessage), 256, Integer.MAX_VALUE), FoldLocation.LEFT);
      this.panelInformationMessage.fold();

      this.add(this.panelActions, BorderLayout.NORTH);
      this.add(new JScrollPane(this.componentEditor), BorderLayout.CENTER);
      this.add(this.panelInformationMessage, BorderLayout.EAST);

      this.currentFile = this.preferences.getFileValue(CompilerEditorPanel.PREFERENCE_LAST_FILE);

      if(this.currentFile != null)
      {
         this.openFile(this.currentFile);
      }

      this.initializeConsole();
   }

   /**
    * Change the default output
    */
   private void initializeConsole()
   {
      final ConsolePrintStream consolePrintStream = new ConsolePrintStream(this.informationMessage);
      System.setOut(consolePrintStream.getPrintStream());
   }

   /**
    * Print a message
    *
    * @param text
    *           Message to print
    */
   private void printMessage(final String text)
   {
      this.informationMessage.setText(text);
      this.panelInformationMessage.unFold();
   }

   /**
    * Called when compilation finished
    */
   void endCompilation()
   {
      this.componentEditor.requestFocus();
      this.componentEditor.requestFocusInWindow();
      this.actionCompile.setEnabled(true);
      this.compiling.set(false);
   }

   void exportClass()
   {
      this.fileChooser.setFileFilter(this.fileFilterClass);
      final File file = this.fileChooser.showSaveFile();

      if(file == null)
      {
         return;
      }

      final Compiler compiler = new Compiler();
      final ByteArrayInputStream stream = new ByteArrayInputStream(this.componentEditor.getText().getBytes());

      try
      {
         final ClassGen classGen = compiler.compile(stream);
         classGen.getJavaClass().dump(new FileOutputStream(file));
      }
      catch(final CompilerException | IOException exception)
      {
         Debug.printException(exception);
      }
   }

   void importClass()
   {
      this.fileChooser.setFileFilter(this.fileFilterClass);
      final File file = this.fileChooser.showOpenFile();

      if(file == null)
      {
         return;
      }

      final Decompiler decompiler = new Decompiler();
      final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(4096);

      try
      {
         decompiler.decompile(new FileInputStream(file), file.getName(), byteArrayOutputStream);
         this.componentEditor.setText(new String(byteArrayOutputStream.toByteArray()));
      }
      catch(final ClassFormatException | IOException exception)
      {
         Debug.printException(exception);
      }
   }

   /**
    * Called if compilation failed with an unexpected reason
    *
    * @param message
    *           Message to print
    */
   void reportCompileFailed(final String message)
   {
      this.printMessage(message);
   }

   /**
    * Called when compilation with a know reason
    *
    * @param compilerException
    *           Known reason
    */
   void reportCompilerException(final CompilerException compilerException)
   {
      Debug.printException(compilerException, "Oups !");

      // Show error message and go line in code on error
      this.printMessage(UtilEditor.extractMessage(compilerException));
      this.componentEditor.scrollToLine(compilerException.getLineNumber());

      if(compilerException instanceof StackInspectorException)
      {
         // If stack inspection failed, show the path in cyan and stack state on each step
         final StackInspectorException stackInspectorException = (StackInspectorException) compilerException;
         final List<StackInfo> path = stackInspectorException.getPath();

         for(final StackInfo stackInfo : path)
         {
            this.componentEditor.changeTemporaryLineNumberBackground(stackInfo.getLineNumber(), Color.CYAN);
            this.componentEditor.addTemporaryTextInformation(stackInfo.getLineNumber(), stackInfo.getInfo());
         }
      }

      // Show the line error in red
      this.componentEditor.changeTemporaryLineNumberBackground(compilerException.getLineNumber(), CompilerEditorPanel.COLOR_ERROR_LINE);
   }

   /**
    * Called if compilation succeed
    *
    * @param className
    *           Compiled class
    */
   void reportCompileSucceed(final String className)
   {
      this.printMessage(UtilText.concatenate("Succeed to compile :\n", className, "\n\n------------\n\n"));
      this.endCompilation();
      this.fireCompilationSucceed(className);
   }

   /**
    * Signal to listeners that compilation succeed
    *
    * @param className
    *           Compiled class name
    */
   protected void fireCompilationSucceed(final String className)
   {
      synchronized(this.compilationListeners)
      {
         try
         {
            for(final CompilationListener compilationListener : this.compilationListeners)
            {
               compilationListener.compilationSucceed(className, this.classManager);
            }
         }
         catch(final Exception exception)
         {
            Debug.printException(exception, "Fail");
            this.printMessage(UtilEditor.extractMessage(exception));
         }
         catch(final Error error)
         {
            Debug.printError(error, "Fail");
            this.printMessage(UtilEditor.extractMessage(error));
         }
      }
   }

   /**
    * Launch compilation on current code.<br>
    * The compilation will not launched if something already compiling or no class declaration inside current code
    *
    * @return <code>true</code> if compilation launched
    */
   public boolean compile()
   {
      if(this.compiling.get())
      {
         return false;
      }

      this.componentEditor.removeAllTemporaryModification();

      final String text = this.componentEditor.getText();
      final Matcher matcher = CompilerEditorPanel.PATTERN_CLASS_DECLARATION.matcher(text);

      if(!matcher.find())
      {
         this.printMessage("No class declaration !");
         return false;
      }

      final String className = matcher.group(1);

      if(this.classManager.isResolved(className))
      {
         this.classManager.newClassLoader();
      }

      this.compiling.set(true);
      this.actionCompile.setEnabled(false);
      this.printMessage(className);
      this.informationMessage.append("\ncompiling ...");
      final byte[] data = text.getBytes();
      final ByteArrayInputStream stream = new ByteArrayInputStream(data);
      this.classManager.compileASMs(this.eventManager, stream);
      return true;
   }

   /**
    * Indicates if compiling something
    *
    * @return <code>true</code> if compiling something
    */
   public boolean compiling()
   {
      return this.compiling.get();
   }

   /**
    * Class manager use for compile
    *
    * @return Class manager use for compile
    */
   public ClassManager getClassManager()
   {
      return this.classManager;
   }

   /**
    * create a new file (All current code deleted)
    */
   public void newFile()
   {
      this.componentEditor.setText("");
      this.currentFile = null;
      this.preferences.removePreference(CompilerEditorPanel.PREFERENCE_LAST_FILE);
      this.title.setText("--Untitled--");
   }

   /**
    * Launch file browser and open the selected file
    *
    * @return <code>true</code> if file open
    */
   public boolean open()
   {
      this.fileChooser.setFileFilter(this.fileFilterASM);
      final File file = this.fileChooser.showOpenFile();

      if(file == null)
      {
         return false;
      }

      return this.openFile(file);
   }

   /**
    * Open a file and then compile it
    *
    * @param file
    *           File to open and compile
    * @return <code>true</code> if file open and compilation launched
    */
   public boolean openAndCompile(final File file)
   {
      if(!this.openFile(file))
      {
         return false;
      }

      return this.compile();
   }

   /**
    * Open a specific file
    *
    * @param file
    *           File to open
    * @return <code>true</code> if file open
    */
   public boolean openFile(final File file)
   {
      BufferedReader bufferedReader = null;

      try
      {
         bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
         final StringBuilder stringBuilder = new StringBuilder();
         String line = bufferedReader.readLine();

         while(line != null)
         {
            stringBuilder.append(line);
            line = bufferedReader.readLine();

            if(line != null)
            {
               stringBuilder.append('\n');
            }
         }

         this.componentEditor.setText(stringBuilder.toString());
         this.currentFile = file;
         this.preferences.setValue(CompilerEditorPanel.PREFERENCE_LAST_FILE, this.currentFile);
         this.preferences.setValue(CompilerEditorPanel.PREFERENCE_LAST_DIRECTORY, this.currentFile.getParentFile());
         this.fileChooser.setStartDirectory(this.currentFile.getParentFile());
         this.title.setText(UtilText.concatenate("--", this.currentFile.getAbsolutePath(), "--"));
         return true;
      }
      catch(final Exception exception)
      {
         Debug.printException(exception, "Failed to load : " + file.getAbsolutePath());
         this.printMessage(UtilText.concatenate("Failed to load file at :\n", file.getAbsolutePath(), "\nBecause :\n", UtilEditor.extractMessage(exception)));
         return false;
      }
      finally
      {
         if(bufferedReader != null)
         {
            try
            {
               bufferedReader.close();
            }
            catch(final Exception ignored)
            {
            }
         }
      }
   }

   /**
    * Register listener to compilation events
    *
    * @param compilationListener
    *           Listener to register
    */
   public void registerCompilationListener(final CompilationListener compilationListener)
   {
      if(compilationListener == null)
      {
         return;
      }

      synchronized(this.compilationListeners)
      {
         if(!this.compilationListeners.contains(compilationListener))
         {
            this.compilationListeners.add(compilationListener);
         }
      }
   }

   /**
    * Save current code
    *
    * @param saveAs
    *           If <code>true</code> it force to open file explorer to choose where save the file
    * @return <code>true</code> if code saved
    */
   public boolean save(final boolean saveAs)
   {
      final File previousFile = this.currentFile;

      if((this.currentFile == null) || (saveAs))
      {
         this.fileChooser.setFileFilter(this.fileFilterASM);
         final File file = this.fileChooser.showSaveFile();

         if(file == null)
         {
            return false;
         }

         this.currentFile = file;
      }

      BufferedWriter bufferedWriter = null;

      try
      {
         bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.currentFile)));
         final String text = this.componentEditor.getText();
         bufferedWriter.write(text);
         this.preferences.setValue(CompilerEditorPanel.PREFERENCE_LAST_FILE, this.currentFile);
         this.preferences.setValue(CompilerEditorPanel.PREFERENCE_LAST_DIRECTORY, this.currentFile.getParentFile());
         this.fileChooser.setStartDirectory(this.currentFile.getParentFile());
         this.title.setText(UtilText.concatenate("--", this.currentFile.getAbsolutePath(), "--"));
         return true;
      }
      catch(final Exception exception)
      {
         Debug.printException(exception, "Failed to save at : " + this.currentFile.getAbsolutePath());
         this.printMessage(
               UtilText.concatenate("Failed to save file at :\n", this.currentFile.getAbsolutePath(), "\nBecause :\n", UtilEditor.extractMessage(exception)));
         this.currentFile = previousFile;
         return false;
      }
      finally
      {
         if(bufferedWriter != null)
         {
            try
            {
               bufferedWriter.flush();
            }
            catch(final Exception ignored)
            {
            }

            try
            {
               bufferedWriter.close();
            }
            catch(final Exception ignored)
            {
            }
         }
      }
   }

   /**
    * Unregister listener form compilation events
    *
    * @param compilationListener
    *           Listener to unregister
    */
   public void unregisterCompilationListener(final CompilationListener compilationListener)
   {
      synchronized(this.compilationListeners)
      {
         this.compilationListeners.remove(compilationListener);
      }
   }
}