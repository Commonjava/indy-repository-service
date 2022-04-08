package org.commonjava.indy.service.repository.config;

import io.smallrye.config.ConfigMapping;

import javax.enterprise.context.ApplicationScoped;
import java.util.Map;

//@ConfigMapping( prefix = "trace.sample" )
@ApplicationScoped
public interface TraceSampleMapInterface
{
    Map<String, String> sampleMap();
}