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
package org.commonjava.indy.service.repository.config;

import io.quarkus.runtime.Startup;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;

import javax.enterprise.context.ApplicationScoped;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Startup
@ConfigMapping( prefix = "trace" )
@ApplicationScoped
public interface TraceConfiguration
{

    String DEFAULT_BASE_SAMPLE_RATE = "100";

    @WithDefault( "false" )
    Boolean enabled();

    @WithDefault( "honeycomb" )
    String tracer();

    @WithName( "console.transport" )
    @WithDefault( "false" )
    Boolean consoleTransport();

    @WithName( "honeycomb.write.key" )
    Optional<String> writeKey();

    @WithName( "honeycomb.dataset" )
    Optional<String> dataSet();

    @WithName( "otel.grpc.uri" )
    Optional<String> grpcUri();

    @WithName( "otel.grpc.headers" )
    Optional<String> grpcHeaders();

    @WithName( "fields" )
    Optional<Set<String>> fields();

    @WithName( "base.sample.rate" )
    @WithDefault( DEFAULT_BASE_SAMPLE_RATE )
    Integer baseSampleRate();

    @WithName( "environment.mappings" )
    Optional<String> environmentMappings();

    @WithName( "cp.names" )
    Optional<String> cpNames();

    @WithName( "nodeid" )
    Optional<String> nodeId();

    Map<String, Integer> sample();

}
