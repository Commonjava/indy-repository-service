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
package org.commonjava.indy.service.repository.ftests;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.commonjava.indy.service.repository.ftests.matchers.RepoEqualMatcher;
import org.commonjava.indy.service.repository.ftests.profile.ISPNFunctionProfile;
import org.commonjava.indy.service.repository.model.HostedRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.commonjava.indy.service.repository.model.pkg.MavenPackageTypeDescriptor.MAVEN_PKG_KEY;

@QuarkusTest
@TestProfile( ISPNFunctionProfile.class )
@Tag( "function" )
@Disabled( "Duplicated to AddAndDeleteGroupTest" )
@Deprecated
public class AddAndRetrieveHostedRepoTest
        extends AbstractStoreManagementTest
{

    @Test
    public void addMinimalHostedRepositoryAndRetrieveIt()
            throws Exception
    {
        final String name = newName();
        final HostedRepository repo = new HostedRepository( MAVEN_PKG_KEY, name );
        final String json = mapper.writeValueAsString( repo );
        given().body( json )
               .contentType( APPLICATION_JSON )
               .post( getRepoTypeUrl( repo.getKey()))
               .then()
               .body( new RepoEqualMatcher<>( mapper, repo, HostedRepository.class ) );

    }

}