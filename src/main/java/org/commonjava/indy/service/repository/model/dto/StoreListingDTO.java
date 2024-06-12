/**
 * Copyright (C) 2022-2023 Red Hat, Inc. (https://github.com/Commonjava/indy-repository-service)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.commonjava.indy.service.repository.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.commonjava.indy.service.repository.model.ArtifactStore;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Schema( type = SchemaType.OBJECT, discriminatorProperty = "type", description = "List of artifact store definitions" )
public class StoreListingDTO<T extends ArtifactStore>
                implements Iterable<T>
{

    @JsonProperty
    @Schema( implementation = ArtifactStore.class, description = "The store definition list", required = true )
    private List<T> items;

    @JsonProperty( "current_page" )
    private String currentPage;

    @JsonProperty( "next_page" )
    private String nextPage;

    public StoreListingDTO()
    {
    }

    public StoreListingDTO( final List<T> items )
    {
        this.items = items;
    }

    public StoreListingDTO( final List<T> items, final String currentPage, final String nextPage )
    {
        this.items = items;
        this.currentPage = currentPage;
        this.nextPage = nextPage;
    }

    public List<T> getItems()
    {
        return items == null ? Collections.emptyList() : items;
    }

    public void setItems( final List<T> items )
    {
        this.items = items;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder();
        sb.append( "StoreListingDTO[" );
        if ( !currentPage.isEmpty() )
        {
            sb.append( "\ncurrentPage=" ).append( currentPage );
        }
        if ( !nextPage.isEmpty() )
        {
            sb.append( "\nnextPage=" ).append( nextPage );
        }
        if ( !currentPage.isEmpty() || !nextPage.isEmpty() )
        {
            sb.append( "\n [" );
        }
        if ( items == null || items.isEmpty() )
        {
            sb.append( "NO STORES" );
        }
        else
        {
            for ( final T item : items )
            {
                sb.append( "\n  " ).append( item );
            }
        }
        if ( !currentPage.isEmpty() || !nextPage.isEmpty() )
        {
            sb.append( "\n ]" );
        }
        sb.append( "\n]" );
        return sb.toString();
    }

    @Override
    public Iterator<T> iterator()
    {
        return getItems().iterator();
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
