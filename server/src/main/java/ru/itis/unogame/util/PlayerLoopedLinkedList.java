package ru.itis.unogame.util;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.itis.unogame.model.GamePlayer;

@Component
@Scope("prototype")
public class PlayerLoopedLinkedList implements LoopedList<GamePlayer> {

    private int size;
    private boolean isReversed;
    private Node current;

    @Override
    public int size() {
        return size;
    }

    @Override
    public GamePlayer getCurrent() {
        return current != null ? current.value : null;
    }

    @Override
    public void next() {
        if (isReversed) {
            current = current.previous;
        } else {
            current = current.next;
        }
    }

    @Override
    public void reverse() {
        isReversed = !isReversed;
    }

    @Override
    public void add(GamePlayer value) {
        Node node = new Node(value);
        if (current == null) {
            current = node;
            node.next = node;
            node.previous = node;
        } else {
            Node temp = current.next;
            current.next = node;
            node.previous = current;
            node.next = temp;
            temp.previous = node;
        }
        next();
        size++;
    }

    @Override
    public boolean isReversed() {
        return isReversed;
    }

    private static class Node {
        private Node next;
        private Node previous;
        private final GamePlayer value;

        public Node(GamePlayer value) {
            this.value = value;
        }
    }
}
