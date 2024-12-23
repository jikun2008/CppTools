// Copyright 2006-2012 AdvancedTools. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.advancedtools.cpp.actions;

import com.advancedtools.cpp.communicator.Communicator;
import com.advancedtools.cpp.psi.CppFile;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;

/**
 * @author: maxim
 * Date: Sep 25, 2006
 * Time: 2:00:35 AM
 */
public abstract class BaseEditorAction extends AnAction {
    public void update(AnActionEvent anActionEvent) {
        final DataContext dataContext = anActionEvent.getDataContext();
        final PsiFile psiFile = findFileFromDataContext(dataContext);
        Editor editor = findEditorFromDataContext(dataContext);

        anActionEvent.getPresentation().setVisible(
                psiFile instanceof CppFile &&
                        editor != null &&
                        acceptableState(editor, psiFile) &&
                        Communicator.getInstance(psiFile.getProject()).isServerUpAndRunning()
        );
    }

    public void actionPerformed(AnActionEvent e) {
        final DataContext dataContext = e.getDataContext();
        final Editor editor = findEditorFromDataContext(dataContext);
        PsiFile file = findFileFromDataContext(dataContext);
        execute(editor, file, (Project) dataContext.getData(CommonDataKeys.PROJECT.getName()));
    }

    protected void execute(Editor editor, PsiFile file, Project project) {
    }

    protected abstract boolean acceptableState(Editor editor, PsiFile file);

    static Editor findEditorFromDataContext(DataContext context) {
        Editor editor = (Editor) context.getData(CommonDataKeys.EDITOR.getName());
        final Project project = (Project) context.getData(CommonDataKeys.PROJECT.getName());

        if (editor == null && project != null) {
            editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        }

        return editor;
    }

    static PsiFile findFileFromDataContext(DataContext context) {
        PsiFile file = (PsiFile) context.getData(CommonDataKeys.PSI_FILE.getName());
        final Project project = (Project) context.getData(CommonDataKeys.PROJECT.getName());
        final Editor editor = findEditorFromDataContext(context);

        if (file == null && editor != null) {
            file = PsiDocumentManager.getInstance(project).getPsiFile(
                    editor.getDocument()
            );
        }
        return file;
    }
}
