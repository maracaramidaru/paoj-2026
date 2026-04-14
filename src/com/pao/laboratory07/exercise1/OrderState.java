package com.pao.laboratory07.exercise1;

import com.pao.laboratory07.exercise1.exceptions.OrderIsAlreadyFinalException;

public enum OrderState {
    PLACED,
    PROCESSED,
    SHIPPED,
    DELIVERED,
    CANCELED;

    public boolean isFinal() {
        return this == DELIVERED || this == CANCELED;
    }

    public OrderState next() throws OrderIsAlreadyFinalException {
        if (isFinal()) {
            throw new OrderIsAlreadyFinalException();
        }
        return switch (this) {
            case PLACED -> PROCESSED;
            case PROCESSED -> SHIPPED;
            case SHIPPED -> DELIVERED;
            default -> this;
        };
    }
}