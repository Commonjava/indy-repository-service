/**
 * Copyright (C) 2020 Red Hat, Inc.
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
package org.commonjava.indy.services.repository.event;

import org.commonjava.indy.services.repository.model.pkg.PackageTypeConstants;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EventMetadata
        implements Iterable<Map.Entry<Object, Object>>
{
    private String packageType;

    private final Map<Object, Object> metadata = new HashMap<>();

    public EventMetadata()
    {
        this.packageType = PackageTypeConstants.PKG_TYPE_MAVEN;
    }

    public EventMetadata( String packageType )
    {
        this.packageType = packageType;
    }

    public EventMetadata( final EventMetadata original )
    {
        this.packageType = original.packageType;
        this.metadata.putAll( original.metadata );
    }

    public Map<Object, Object> getMetadata()
    {
        return metadata;
    }

    public EventMetadata set( final Object key, final Object value )
    {
        metadata.put( key, value );
        return this;
    }

    public Object get( final Object key )
    {
        return metadata.get( key );
    }

    public boolean containsKey( final Object key )
    {
        return metadata.containsKey( key );
    }

    public boolean containsValue( final Object value )
    {
        return metadata.containsValue( value );
    }

    public String getPackageType()
    {
        return packageType;
    }

    public void setPackageType( String packageType )
    {
        this.packageType = packageType;
    }

    @Override
    public Iterator<Map.Entry<Object, Object>> iterator()
    {
        return metadata.entrySet()
                       .iterator();
    }

    @Override
    public String toString()
    {
        return String.format( "EventMetadata [metadata=%s]", metadata );
    }

}
