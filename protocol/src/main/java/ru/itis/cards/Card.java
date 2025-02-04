package ru.itis.cards;

import java.util.Objects;

public record Card(int value, CardType type, CardColor color) {
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Card card)) return false;
        if (type == CardType.PLUS4 || type == CardType.COLOR_EDIT) {
            return type == card.type;
        }
        if (type == CardType.PLUS2 || type == CardType.FLIP || type == CardType.BLOCK) {
            return type == card.type && color == card.color;
        }
        return value == card.value && type == card.type && color == card.color;
    }

    @Override
    public int hashCode() {
        if (type == CardType.PLUS4 || type == CardType.COLOR_EDIT) {
            return Objects.hash(type);
        }
        if (type == CardType.PLUS2 || type == CardType.FLIP || type == CardType.BLOCK) {
            return Objects.hash(type, color);
        }
        return Objects.hash(value, type, color);
    }

    @Override
    public String toString() {
        return "Card{" +
                "value=" + value +
                ", type=" + type +
                ", color=" + color +
                '}';
    }
}
