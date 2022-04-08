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

import org.commonjava.o11yphant.metrics.conf.ConsoleConfig;
import org.commonjava.o11yphant.metrics.conf.ELKConfig;
import org.commonjava.o11yphant.metrics.conf.GraphiteConfig;
import org.commonjava.o11yphant.metrics.conf.MetricsConfig;
import org.commonjava.o11yphant.metrics.conf.PrometheusConfig;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class MetricsConfigAdaptor implements MetricsConfig
{
    @Inject
    MetricsConfig metricsConfig;

    @Override
    public String getNodePrefix()
    {
        return null;
    }

    @Override
    public boolean isEnabled()
    {
        return false;
    }

    @Override
    public String getReporter()
    {
        return null;
    }

    @Override
    public ConsoleConfig getConsoleConfig()
    {
        return null;
    }

    @Override
    public GraphiteConfig getGraphiteConfig()
    {
        return null;
    }

    @Override
    public PrometheusConfig getPrometheusConfig()
    {
        return null;
    }

    @Override
    public ELKConfig getELKConfig()
    {
        return null;
    }

    @Override
    public int getMeterRatio()
    {
        return 0;
    }
}
