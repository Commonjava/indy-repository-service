/**
 * Copyright (C) 2011-2020 Red Hat, Inc. (https://github.com/Commonjava/indy)
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

import org.commonjava.o11yphant.honeycomb.HoneycombTracePlugin;
import org.commonjava.o11yphant.otel.OtelTracePlugin;
import org.commonjava.o11yphant.trace.SpanFieldsDecorator;
import org.commonjava.o11yphant.trace.TraceManager;
import org.commonjava.o11yphant.trace.spi.O11yphantTracePlugin;
import org.commonjava.o11yphant.trace.spi.SpanFieldsInjector;
import org.commonjava.o11yphant.trace.thread.TraceThreadContextualizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public class TraceManagerProducer
{
    private TraceManager traceManager;

    private TraceThreadContextualizer<?> traceThreadContextualizer;

    @Inject
    private MetricsTracerConfigAdaptor config;

    @Inject
    private RepoServiceTrafficClassifier trafficClassifier;

    @Inject
    private Instance<SpanFieldsInjector> rsfInstance;

    private final Logger logger = LoggerFactory.getLogger( getClass() );

    @PostConstruct
    public void init()
    {
        O11yphantTracePlugin<?> plugin;
        if ( config.getTracer() == TracerPlugin.opentelemetry )
        {
            logger.info( "Initializing Opentelemetry trace plugin" );
            plugin = new OtelTracePlugin( config, config );
        }
        else
        {
            logger.info( "Initializing Honeycomb trace plugin" );
            if ( config.isEnabled() )
            {
                Set<String> fields = config.validateForHoneycomb();
                if ( !fields.isEmpty() )
                {
                    logger.error( "Invalid Honeycomb configuration detected!" );
                    throw new RuntimeException(
                            String.format( "Cannot initialize Honeycomb tracer. Missing configuration fields: %s",
                                           fields ) );
                }

            }

            plugin = new HoneycombTracePlugin( config, config, Optional.of( trafficClassifier ) );
        }

        traceManager = new TraceManager<>( plugin, new SpanFieldsDecorator( getRootSpanFields() ), config );
        traceThreadContextualizer = traceManager.getTraceThreadContextualizer();
    }

    @Produces
    @Default
    public TraceThreadContextualizer getTraceThreadContextualizer()
    {
        return traceThreadContextualizer;
    }

    @Produces
    @Default
    public TraceManager getTraceManager()
    {
        return traceManager;
    }

    private List<SpanFieldsInjector> getRootSpanFields()
    {
        List<SpanFieldsInjector> result = new ArrayList<>();
        if ( !rsfInstance.isUnsatisfied() )
        {
            rsfInstance.forEach( result::add );
        }
        return result;
    }
}
