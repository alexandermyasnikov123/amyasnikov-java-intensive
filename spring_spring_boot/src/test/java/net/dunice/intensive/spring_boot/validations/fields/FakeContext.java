package net.dunice.intensive.spring_boot.validations.fields;

import jakarta.validation.ClockProvider;
import jakarta.validation.ConstraintValidatorContext;

public final class FakeContext implements ConstraintValidatorContext {
    @Override
    public void disableDefaultConstraintViolation() {

    }

    @Override
    public String getDefaultConstraintMessageTemplate() {
        return "";
    }

    @Override
    public ClockProvider getClockProvider() {
        return null;
    }

    @Override
    public ConstraintViolationBuilder buildConstraintViolationWithTemplate(String messageTemplate) {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> type) {
        return null;
    }

    public static class FakeContextBuilder implements ConstraintValidatorContext.ConstraintViolationBuilder {

        @Override
        public NodeBuilderDefinedContext addNode(String name) {
            return null;
        }

        @Override
        public NodeBuilderCustomizableContext addPropertyNode(String name) {
            return null;
        }

        @Override
        public LeafNodeBuilderCustomizableContext addBeanNode() {
            return null;
        }

        @Override
        public ContainerElementNodeBuilderCustomizableContext addContainerElementNode(String name, Class<?> containerType, Integer typeArgumentIndex) {
            return null;
        }

        @Override
        public NodeBuilderDefinedContext addParameterNode(int index) {
            return null;
        }

        @Override
        public ConstraintValidatorContext addConstraintViolation() {
            return null;
        }
    }
}
