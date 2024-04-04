package org.commonjava.indy.service.repository.model.dto;

import org.commonjava.indy.service.repository.model.ArtifactStore;

import java.util.Set;

public class ListArtifactStoreDTO
{
    private Set<ArtifactStore> items;

    private String currentPage;

    private String nextPage;

    public ListArtifactStoreDTO()
    {

    }

    public ListArtifactStoreDTO( Set<ArtifactStore> items, String currentPage, String nextPage )
    {
        this.items = items;
        this.currentPage = currentPage;
        this.nextPage = nextPage;
    }

    public ListArtifactStoreDTO( Set<ArtifactStore> items )
    {
        this.items = items;
    }

    public Set<ArtifactStore> getItems()
    {
        return items;
    }

    public void setItems( Set<ArtifactStore> items )
    {
        this.items = items;
    }

    public String getCurrentPage()
    {
        return currentPage;
    }

    public void setCurrentPage( String currentPage )
    {
        this.currentPage = currentPage;
    }

    public String getNextPage()
    {
        return nextPage;
    }

    public void setNextPage( String nextPage )
    {
        this.nextPage = nextPage;
    }
}
