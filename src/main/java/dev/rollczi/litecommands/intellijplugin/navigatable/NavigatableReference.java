package dev.rollczi.litecommands.intellijplugin.navigatable;

import com.intellij.platform.backend.navigation.NavigationRequest;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import dev.rollczi.litecommands.intellijplugin.util.HighlightUtil;
import java.util.Optional;
import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;

public class NavigatableReference implements Navigatable {

    private final Supplier<Optional<Navigatable>> source;
    private final Supplier<Optional<PsiElement>> psiElementSource;


    NavigatableReference(Supplier<Navigatable> source) {
        this(source, () -> null);
    }

    NavigatableReference(Supplier<Navigatable> source, Supplier<PsiElement> psiElementSource) {
        this.source = () -> Optional.ofNullable(source.get());
        this.psiElementSource = () -> Optional.ofNullable(psiElementSource.get());
    }

    @Override
    public @Nullable NavigationRequest navigationRequest() {
        return source.get()
            .map(Navigatable::navigationRequest)
            .orElse(null);

    }

    @Override
    public void navigate(boolean requestFocus) {
        source.get()
            .ifPresent(navigatable -> navigatable.navigate(requestFocus));
    }

    @Override
    public boolean canNavigate() {
        return source.get()
            .map(Navigatable::canNavigate)
            .orElse(false);
    }

    @Override
    public boolean canNavigateToSource() {
        return source.get()
            .map(Navigatable::canNavigateToSource)
            .orElse(false);
    }

    public Optional<PsiElement> getPsiElement() {
        return psiElementSource.get();
    }

    public static NavigatableReference of(Supplier<Navigatable> source) {
        return new NavigatableReference(source, () -> {
            Navigatable navigatable = source.get();

            if (navigatable instanceof PsiElement) {
                return (PsiElement) navigatable;
            }

            return null;
        });
    }

    public static NavigatableReference empty() {
        return new NavigatableReference(() -> null);
    }

    public static NavigatableReference ofPsiElement(Supplier<PsiElement> source) {
        return new NavigatableReference(() -> {
            PsiElement psiElement = source.get();

            if (psiElement instanceof Navigatable) {
                return (Navigatable) psiElement;
            }

            return null;
        }, source);
    }

    public void highlight() {
        psiElementSource.get().ifPresent(psiElement -> {
            HighlightUtil.highlight(psiElement);
        });

        source.get().ifPresent(navigatable -> {
            navigatable.navigate(true);
        });
    }
}
