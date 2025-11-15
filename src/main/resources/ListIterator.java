import java_cup.runtime.AstNode;
import java.util.*;
import java.util.function.Consumer;

class NodeListIterator implements Iterator<Map.Entry<String, AstNode>> {

    private final List<? extends AstNode> list;
    private int index = 0;

    public NodeListIterator(List<? extends AstNode> list) {
        this.list = list;
    }

    @Override
    public void forEachRemaining(Consumer<? super Map.Entry<String, AstNode>> action) {
        Objects.requireNonNull(action);
        for (int i = index; i < list.size(); i++) {
            action.accept(new AbstractMap.SimpleEntry<>(String.valueOf(i), list.get(i)));
        }
    }

    @Override
    public boolean hasNext() {
        return index < list.size();
    }

    @Override
    public Map.Entry<String, AstNode> next() {
        return new AbstractMap.SimpleEntry<>(String.valueOf(index), list.get(index++));
    }

}