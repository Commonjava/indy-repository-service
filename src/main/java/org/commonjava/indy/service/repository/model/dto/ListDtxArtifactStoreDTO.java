package org.commonjava.indy.service.repository.model.dto;

import org.commonjava.indy.service.repository.data.cassandra.DtxArtifactStore;

import java.util.Set;

public class ListDtxArtifactStoreDTO
{
    private Set<DtxArtifactStore> items;

    private String currentPage;

    private String nextPage;

    public ListDtxArtifactStoreDTO()
    {

    }

    public ListDtxArtifactStoreDTO( Set<DtxArtifactStore> items, String currentPage, String nextPage )
    {
        this.items = items;
        this.currentPage = currentPage;
        this.nextPage = nextPage;
    }

    public Set<DtxArtifactStore> getItems()
    {
        return items;
    }

    public void setItems( Set<DtxArtifactStore> items )
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
