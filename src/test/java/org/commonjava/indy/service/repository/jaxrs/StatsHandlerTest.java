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
package org.commonjava.indy.service.repository.jaxrs;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;

@QuarkusTest
@TestProfile( MockTestProfile.class )
public class StatsHandlerTest
{
    @Test
    public void testGetAppVersion()
    {
        given().get( "/api/stats/version-info" ).then().statusCode( OK.getStatusCode() ).body( notNullValue() );
    }

    @Test
    public void testGetPackageTypeNames()
    {
        given().get( "/api/stats/responseCode-type/keys" )
               .then()
               .statusCode( OK.getStatusCode() )
               .body( containsString( "maven" ) );
    }

    @Test
    public void testGetPackageTypeMap()
    {
        given().get( "/api/stats/package-type/map" )
               .then()
               .statusCode( OK.getStatusCode() )
               .body( containsString( "maven" ) );
    }

    @Test
    public void testGetAllEndpoints()
    {
        given().get( "/api/stats/all-endpoints" )
               .then()
               .statusCode( OK.getStatusCode() )
               .body( "size()", is( 1 ) )
               .body( "items.size()", greaterThan( 1 ) );
    }
}