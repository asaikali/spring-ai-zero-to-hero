# PostgreSQL Docker Setup

This directory contains a Docker Compose setup for PostgreSQL and pgAdmin, along with a convenient script to manage the containers.

## Quick Start

The `pg` script provides a simple interface to manage the PostgreSQL and pgAdmin containers:

```bash
# Start PostgreSQL and pgAdmin
./pg start

# Check the status of containers and display connection information
./pg status

# Stop the containers
./pg stop

# Stop the containers and remove volumes (clean up)
./pg clean

# Fix port conflicts by shutting down conflicting containers
./pg fix
```

## Script Commands

- `start`: Starts the PostgreSQL and pgAdmin containers.
- `status`: Shows container status and connection information.
- `stop`: Stops the running containers.
- `clean`: Stops the containers and removes all associated volumes.
- `fix`: Detects and resolves port conflicts automatically.

## Services

### PostgreSQL
- Port: 15432 (customizable)
- Username: postgres
- Password: password

### pgAdmin
- URL: http://localhost:15433 (customizable)
- Email: admin@example.com
- Password: admin

## Customizing Ports

You can override the default ports using environment variables:

# Override both ports
PG_PORT=25432 PGADMIN_PORT=25433 ./pg start
```

This is useful when:
- The default ports are already in use by other services
- You need to run multiple instances of PostgreSQL on the same machine
- You want to avoid port conflicts with other Docker containers

## Connecting to PostgreSQL

### From Host Machine
- JDBC URL: `jdbc:postgresql://localhost:15432/postgres`
- PSQL: `psql -h localhost -p 15432 -U postgres -d postgres`

### From Docker Containers
- Host: postgres
- Port: 5432
- Username: postgres
- Password: password

## Notes
- Run `./pg status` to view all connection details.
- The `fix` command helps resolve port conflicts automatically.
- Use environment variables `PG_PORT` and `PGADMIN_PORT` to customize ports (see [Customizing Ports](#customizing-ports)).
- All other configuration values are hardcoded in the docker-compose.yaml file.
