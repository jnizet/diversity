# Database

The `setup/database.sql` file is used by docker-compose to start a PostgreSQL instance with the proper schema.

When the application starts, it creates the tables of the application using [Flyway](https://flywaydb.org/),
by executing the SQL files in `src/main/resources/db/migrations`.
The files are executed sequentially, following the order of their name.

If you want to add testing data, you can run:

    psql -U diversity -d diversity -h localhost -f backend/database/testing/test-data.sql
