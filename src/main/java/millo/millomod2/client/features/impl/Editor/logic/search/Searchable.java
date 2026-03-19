package millo.millomod2.client.features.impl.Editor.logic.search;

import java.util.Collection;

public interface Searchable {
    Collection<? extends SearchResult> search(String searchQuery);
}
