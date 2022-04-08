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
package org.commonjava.indy.service.repository.data.metrics;

import org.commonjava.indy.service.repository.config.TraceConfiguration;
import org.commonjava.o11yphant.honeycomb.HoneycombConfiguration;
import org.commonjava.o11yphant.otel.OtelConfiguration;
import org.commonjava.o11yphant.trace.TracerConfiguration;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Collections.emptyMap;

@ApplicationScoped
public class MetricsTracerConfigAdaptor
        implements TracerConfiguration, OtelConfiguration, HoneycombConfiguration
{
    @Inject
    private TraceConfiguration config;

    @Override
    public boolean isEnabled()
    {
        return config.enabled();
    }

    @Override
    public boolean isConsoleTransport()
    {
        return config.consoleTransport();
    }

    @Override
    public String getWriteKey()
    {
        return config.writeKey().orElse( null );
    }

    @Override
    public String getDataset()
    {
        return config.dataSet().orElse( null );
    }

    @Override
    public String getServiceName()
    {
        return "indy-repository-service";
    }

    @Override
    public Set<String> getFieldSet()
    {
        return config.fields().orElse( DEFAULT_FIELDS );
    }

    @Override
    public String getEnvironmentMappings()
    {
        return config.environmentMappings().orElse( null );
    }

    @Override
    public String getCPNames()
    {
        return config.cpNames().orElse( null );
    }

    @Override
    public String getNodeId()
    {
        return config.nodeId().orElse( null );
    }

    @Override
    public Map<String, Integer> getSpanRates()
    {
        return config.sample() == null ? emptyMap() : config.sample();
    }

    @Override
    public Integer getBaseSampleRate()
    {
        return config.baseSampleRate();
    }

    public TracerPlugin getTracer()
    {
        return TracerPlugin.valueOf( config.tracer().trim().toLowerCase() );
    }

    @Override
    public Map<String, String> getGrpcHeaders()
    {
        if ( config.grpcHeaders().isPresent() )
        {
            String grpcHeaders = config.grpcHeaders().get();
            String[] kvs = grpcHeaders.trim().split( "\\s*,\\s*" );
            Map<String, String> headers = new HashMap<>();
            Stream.of( kvs )
                  .map( kv -> kv.split( "\\s*=\\s*" ) )
                  .filter( kv -> kv.length > 1 )
                  .forEach( kv -> headers.put( kv[0], kv[1] ) );
            return headers;
        }
        return emptyMap();
    }

    @Override
    public String getGrpcEndpointUri()
    {
        return config.grpcUri().orElse( DEFAULT_GRPC_URI );
    }

    public Set<String> validateForHoneycomb()
    {
        Set<String> ret = new HashSet<>();
        if ( config.writeKey().isEmpty() )
        {
            ret.add( "honeycomb.write.key" );
        }

        if ( config.writeKey().isEmpty() )
        {
            ret.add( "honeycomb.dataset" );
        }

        return ret;

    }
}
