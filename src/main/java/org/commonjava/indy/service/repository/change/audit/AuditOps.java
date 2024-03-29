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
package org.commonjava.indy.service.repository.change.audit;

public class AuditOps
{
    public final static String OPS_DEL = "deleted";

    public final static String OPS_ADD = "created";

    public final static String OPS_DISABLE = "disabled";

    public final static String OPS_ENABLE = "enabled";

    public final static String OPS_UPDATE = "update";
}
