# Indy Repository Service

A Quarkus-based microservice for managing artifact repository configurations in the Indy ecosystem. This service provides comprehensive CRUD operations for different types of artifact stores including hosted, remote, and group repositories.

## Overview

The Indy Repository Service is designed to manage repository definitions and configurations for various package types (Maven, NPM, generic HTTP, etc.). It serves as the central configuration management component for artifact storage systems, supporting both in-memory and persistent storage backends.

## Architecture

### Core Components

- **Repository Management**: CRUD operations for artifact stores
- **Storage Backends**: Memory-based and Cassandra-based storage options
- **REST API**: JAX-RS endpoints for repository administration
- **Event System**: Kafka-based event publishing for repository changes
- **Security**: Keycloak integration for authentication and authorization

### Repository Types

The service supports three main types of artifact stores:

1. **Hosted Repositories** (`HostedRepository`): Local storage for artifacts
2. **Remote Repositories** (`RemoteRepository`): Proxy to external repositories
3. **Group Repositories** (`Group`): **Hierarchical aggregation of multiple repositories**

#### Group Repository Hierarchy

Group repositories provide the most flexible way to organize and access artifacts from multiple sources:

- **Nested Groups**: Groups can contain other groups, creating complex hierarchical structures
- **Ordered Membership**: Constituent repositories are ordered, determining artifact lookup priority
- **Prepend Mode**: New repositories can be added to the front of the group for highest priority
- **Recursive Resolution**: The system can recursively traverse group hierarchies to find all member repositories
- **Build Flexibility**: Build systems can reference a single group and automatically access artifacts from all constituent repositories

**Example Group Hierarchy**:
```
Maven Public Group
├── Maven Central (remote)
├── Internal Libraries Group
│   ├── Internal Hosted Repo
│   └── Third-party Group
│       ├── JBoss Repository (remote)
│       └── Spring Repository (remote)
└── Local Snapshots (hosted)
```

This hierarchical approach allows build systems to:
- Reference a single group endpoint
- Automatically search across multiple repositories in priority order
- Easily add/remove repositories without changing build configurations
- Create complex organizational structures for different environments (dev, staging, prod)

### Package Types

Supports multiple package types including:
- Maven
- NPM
- Generic HTTP
- Custom package types via `PackageTypeDescriptor`

## Technology Stack

- **Framework**: Quarkus 3.x
- **Runtime**: Java 11+
- **Storage**: Infinispan (in-memory) / Cassandra (persistent)
- **Messaging**: Apache Kafka
- **Security**: Keycloak + OIDC
- **Observability**: OpenTelemetry
- **API Documentation**: OpenAPI/Swagger

## API Endpoints

### Repository Administration
- `GET/POST/PUT/DELETE /api/admin/stores/{packageType}/{type}/{name}` - Manage individual repositories
- `HEAD /api/admin/stores/{packageType}/{type}/{name}` - Check repository existence

### Repository Querying
- `GET /api/admin/stores/query/all` - List all repositories with filtering
- `GET /api/admin/stores/query/endpoints/{packageType}` - Get repository endpoints
- `GET /api/admin/stores/query/keys` - List all repository keys

### Group Management
- `GET /api/admin/stores/{packageType}/group/{name}` - Get group details and constituents
- `POST /api/admin/stores/{packageType}/group/{name}` - Create new group with constituents
- `PUT /api/admin/stores/{packageType}/group/{name}` - Update group membership and settings
- `DELETE /api/admin/stores/{packageType}/group/{name}` - Delete group
- **Group Constituents**: Groups can contain any combination of hosted, remote, and other group repositories
- **Hierarchical Queries**: Query operations support recursive group traversal to find all member repositories

### Statistics & Health
- `GET /api/stats/version-info` - Service version information
- `GET /api/stats/endpoints` - Repository endpoint listings

## Configuration

Key configuration options in `application.yaml`:

```yaml
repository:
  data-storage: mem  # or "cassandra" for persistent storage
  affectedGroupsExclude: "^build-.+|^g-.+-build-.+"
  disposableStorePattern: "^build-.+|^[ghr]-.+|^httprox_.+"
  query:
    cache:
      enabled: false

cassandra:
  enabled: false

kafka:
  bootstrap:
    servers: "localhost:9092"
```

## Group Repository Examples

### Creating a Hierarchical Group Structure

```json
// Create a main Maven group
POST /api/admin/stores/maven/group/public
{
  "name": "public",
  "packageType": "maven",
  "type": "group",
  "constituents": [
    {"packageType": "maven", "type": "remote", "name": "central"},
    {"packageType": "maven", "type": "group", "name": "internal-libs"},
    {"packageType": "maven", "type": "hosted", "name": "snapshots"}
  ],
  "prependConstituent": false
}

// Create a sub-group for internal libraries
POST /api/admin/stores/maven/group/internal-libs
{
  "name": "internal-libs",
  "packageType": "maven", 
  "type": "group",
  "constituents": [
    {"packageType": "maven", "type": "hosted", "name": "internal-releases"},
    {"packageType": "maven", "type": "group", "name": "third-party"}
  ]
}
```

### Build System Integration

Maven `settings.xml`:
```xml
<repositories>
  <repository>
    <id>indy-public</id>
    <url>http://localhost:8080/api/content/maven/group/public</url>
    <releases><enabled>true</enabled></releases>
    <snapshots><enabled>true</enabled></snapshots>
  </repository>
</repositories>
```

With this configuration, Maven will:
1. First check Maven Central
2. Then check the internal-libs group (which includes internal releases and third-party repos)
3. Finally check the snapshots repository

## Development Setup

### Prerequisites
- JDK 11+
- Maven 3.6.2+
- Docker 20+ (for dependencies)
- Docker Compose 1.20+

### Quick Start

1. **Clone and build**:
   ```bash
   git clone https://github.com/Commonjava/indy-repository-service.git
   cd indy-repository-service
   mvn clean compile
   ```

2. **Start dependencies**:
   ```bash
   docker-compose up -d
   ```

3. **Run in development mode**:
   ```bash
   mvn quarkus:dev
   ```

The service will be available at `http://localhost:8080` with Swagger UI at `http://localhost:8080/q/swagger-ui/`.

### Docker Services

The `docker-compose.yml` provides:
- **Cassandra** (port 9042): For persistent storage
- **Kafka + Zookeeper** (port 9092): For event messaging
- **Keycloak** (port 8180): For authentication

## Storage Options

### Memory Storage (Default)
- Uses Infinispan for in-memory caching
- Suitable for development and testing
- Data is lost on restart

### Cassandra Storage
- Persistent storage option
- Configure with `repository.data-storage: cassandra`
- Requires Cassandra instance running

## Event System

Repository changes are published to Kafka topics:
- **Topic**: `store-event`
- **Events**: Repository creation, updates, deletions

## Security

- **Authentication**: Keycloak OIDC integration
- **Authorization**: Role-based access control
- **API Security**: JAX-RS security annotations

## Monitoring & Observability

- **OpenTelemetry**: Distributed tracing
- **Logging**: Structured logging with configurable levels
- **Health Checks**: Built-in Quarkus health endpoints

## Testing

The project includes comprehensive test suites:
- **Unit Tests**: JUnit 5 with Quarkus test framework
- **Integration Tests**: REST Assured for API testing
- **Test Utilities**: Mock services and test data setup

Run tests:
```bash
mvn test
```

## Building for Production

```bash
mvn clean package -Pnative  # Native executable
# or
mvn clean package           # JAR file
```

## License

Licensed under the Apache License, Version 2.0. See [LICENSE](LICENSE) for details.
