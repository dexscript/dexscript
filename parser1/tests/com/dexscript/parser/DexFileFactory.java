package com.dexscript.parser;

import com.intellij.lang.*;
import com.intellij.lang.impl.PsiBuilderFactoryImpl;
import com.intellij.mock.*;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.impl.FileDocumentManagerImpl;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.impl.ProgressManagerImpl;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.impl.PsiFileFactoryImpl;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoInitializationException;
import org.picocontainer.PicoIntrospectionException;
import org.picocontainer.defaults.AbstractComponentAdapter;

public class DexFileFactory {

    private static PsiFileFactoryImpl psiFileFactory;

    public static PsiFile createFile(String name, String text) {
        return getPsiFileFactory().createFileFromText(name, DexFileType.INSTANCE, text);
    }

    public static PsiFileFactory getPsiFileFactory() {
        synchronized (DexFileFactory.class) {
            return _getPsiFileFactory();
        }
    }

    private static PsiFileFactory _getPsiFileFactory() {
        if (psiFileFactory != null) {
            return psiFileFactory;
        }
        Disposable disposable = () -> {
        };
        final MockApplicationEx instance = new MockApplicationEx(disposable);
        ApplicationManager.setApplication(instance,
                FileTypeManager::getInstance,
                disposable);
        instance.getPicoContainer().registerComponent(new AbstractComponentAdapter(ProgressManager.class.getName(), Object.class) {
            @Override
            public Object getComponentInstance(PicoContainer container) throws PicoInitializationException, PicoIntrospectionException {
                return new ProgressManagerImpl();
            }

            @Override
            public void verify(PicoContainer container) throws PicoIntrospectionException {
            }
        });
        Extensions.registerAreaClass("IDEA_PROJECT", null);
        MockProjectEx myProject = new MockProjectEx(disposable);
        MockPsiManager myPsiManager = new MockPsiManager(myProject);

        MutablePicoContainer appContainer = instance.getPicoContainer();
        MockEditorFactory editorFactory = new MockEditorFactory();
        registerComponentInstance(appContainer, EditorFactory.class, editorFactory);
        registerComponentInstance(appContainer, FileDocumentManager.class, new MockFileDocumentManagerImpl(
                charSequence -> editorFactory.createDocument(charSequence), FileDocumentManagerImpl.HARD_REF_TO_DOCUMENT_KEY));
        registerComponentInstance(appContainer, PsiDocumentManager.class, new MockPsiDocumentManager());
        instance.registerService(PsiBuilderFactory.class, new PsiBuilderFactoryImpl());
        instance.registerService(DefaultASTFactory.class, new DefaultASTFactoryImpl());

        DexParserDefinition definition = new DexParserDefinition();
        Language myLanguage = definition.getFileNodeType().getLanguage();
        LanguageParserDefinitions.INSTANCE.addExplicitExtension(myLanguage, definition);
        registerComponentInstance(instance.getPicoContainer(), FileTypeManager.class,
                new MockFileTypeManager(new MockLanguageFileType(myLanguage, DexFileType.INSTANCE.getDefaultExtension())));
        psiFileFactory = new PsiFileFactoryImpl(myPsiManager);
        return psiFileFactory;
    }

    private static <T> T registerComponentInstance(final MutablePicoContainer container, final Class<T> key, final T implementation) {
        Object old = container.getComponentInstance(key);
        container.unregisterComponent(key);
        container.registerComponentInstance(key, implementation);
        return (T) old;
    }
}
