package org.justserve.cli;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.BeanContext;
import io.micronaut.core.annotation.TypeHint;
import picocli.CommandLine;
import picocli.CommandLine.IFactory;

import java.util.Optional;

import static io.micronaut.core.annotation.TypeHint.AccessType.ALL_DECLARED_CONSTRUCTORS;
import static io.micronaut.core.annotation.TypeHint.AccessType.ALL_DECLARED_FIELDS;

/**
 * Picocli factory implementation that uses a Micronaut BeanContext to obtain bean instances.
 */
@TypeHint(typeNames = {
        "picocli.CommandLine$AutoHelpMixin",
        "picocli.CommandLine$Model$CommandSpec"
}, accessType = {ALL_DECLARED_CONSTRUCTORS, ALL_DECLARED_FIELDS})
public class JustServeFactory implements IFactory {

    private final IFactory defaultFactory = CommandLine.defaultFactory();
    private final BeanContext beanContext;

    public JustServeFactory() {
        this(ApplicationContext.run());
    }

    public JustServeFactory(BeanContext beanContext) {
        this.beanContext = beanContext;
    }

    @Override
    public <K> K create(Class<K> cls) throws Exception {
        Optional<K> bean = beanContext.findOrInstantiateBean(cls);
        return bean.isPresent() ? bean.get() : defaultFactory.create(cls);
    }
}