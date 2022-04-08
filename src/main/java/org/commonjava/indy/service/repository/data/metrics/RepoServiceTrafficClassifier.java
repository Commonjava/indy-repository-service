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

import org.commonjava.o11yphant.metrics.AbstractTrafficClassifier;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;

@ApplicationScoped
@Default
public class RepoServiceTrafficClassifier
        extends AbstractTrafficClassifier
{
    public static final String FN_REPO_MGMT = "repo.mgmt";

    @Override
    protected List<String> calculateCachedFunctionClassifiers( String restPath, String method,
                                                               Map<String, String> headers )
    {
        List<String> result = new ArrayList<>();

        String[] pathParts = restPath.split( "/" );
        if ( pathParts.length >= 2 )
        {
            String[] classifierParts = new String[pathParts.length - 1];
            System.arraycopy( pathParts, 1, classifierParts, 0, classifierParts.length );

            if ( "admin".equals( classifierParts[0] ) && "stores".equals( classifierParts[1] )
                    && classifierParts.length > 2 )
            {
                if ( MODIFY_METHODS.contains( method ) )
                {
                    // this is a store modification request
                    result = singletonList( FN_REPO_MGMT );
                }
            }
        }
        return result;
    }

}
