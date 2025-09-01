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
package org.commonjava.indy.service.repository.ftests.admin;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.commonjava.indy.service.repository.ftests.AbstractStoreManagementTest;
import org.commonjava.indy.service.repository.ftests.matchers.RepoEqualMatcher;
import org.commonjava.indy.service.repository.ftests.profile.MemoryFunctionProfile;
import org.commonjava.indy.service.repository.model.Group;
import org.commonjava.indy.service.repository.model.RemoteRepository;
import org.commonjava.test.http.expect.ExpectationServer;
import org.commonjava.test.http.quarkus.InjectExpected;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.OK;
import static org.commonjava.indy.service.repository.model.pkg.MavenPackageTypeDescriptor.MAVEN_PKG_KEY;
import static org.junit.Assert.assertTrue;

/**
 * This case tests the group member add
 *
 * <br/>
 * <b>GIVEN:</b>
 * <ul>
 *      <li>create group G repo</li>
 *      <li>create remote M repo</li>
 * </ul>
 *
 * <br/>
 * <b>WHEN:</b>
 * <ul>
 *      <li>add remote M as member into group G></li>
 * </ul>
 *
 * <br/>
 * <b>THEN:</b>
 * <ul>
 *      <li>get the group G and verify the member M existence</li>
 * </ul>
 */
@QuarkusTest
@TestProfile( MemoryFunctionProfile.class )
@Tag( "function" )
public class AddGroupConstituentTest
        extends AbstractStoreManagementTest
{

    @InjectExpected()
    public final ExpectationServer server = new ExpectationServer();

    @Test
    public void addGroupConstituentIt()
            throws Exception
    {
        final String name = "G";
        final Group group = new Group( MAVEN_PKG_KEY, name );
        String json = mapper.writeValueAsString( group );
        given().body( json )
               .contentType( APPLICATION_JSON )
               .post( getRepoTypeUrl( group.getKey() ) )
               .then()
               .body( new RepoEqualMatcher<>( mapper, group, Group.class ) );

        final String urlName = "urltest";
        final String url = server.formatUrl( urlName );
        final RemoteRepository member = new RemoteRepository( MAVEN_PKG_KEY, "M", url );
        json = mapper.writeValueAsString( member );
        given().body( json )
               .contentType( APPLICATION_JSON )
               .post( getRepoTypeUrl( member.getKey() ) )
               .then()
               .body( new RepoEqualMatcher<>( mapper, member, RemoteRepository.class ) );

        final String repoUrl = getRepoUrl( group.getKey() );
        given().body( json )
               .contentType( APPLICATION_JSON )
               .put( repoUrl + "addConstituent" )
               .then()
               .statusCode( OK.getStatusCode() );

        String result = given().get( repoUrl )
                               .then()
                               .statusCode( OK.getStatusCode() )
                               .body( new RepoEqualMatcher<>( mapper, group, Group.class ) )
                               .extract()
                               .asString();
        assertTrue( result.contains( "\"constituents\" : [ \"maven:remote:M\" ]" ) );
    }
}
