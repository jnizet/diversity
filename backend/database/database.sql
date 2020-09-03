-- creates the diversity user and database
-- this script must be executed by an admin user. It can typically be executed from the root of the project using
-- psql -h localhost -U postgres -f backend/database/database.sql

create user diversity password 'diversity';
create database diversity owner diversity
    encoding 'UTF8'
    lc_collate 'fr_FR.UTF-8'
    lc_ctype 'fr_FR.UTF-8'
    template=template0;

create database diversity_test owner diversity
    encoding 'UTF8'
    lc_collate 'fr_FR.UTF-8'
    lc_ctype 'fr_FR.UTF-8'
    template=template0;

create database diversity_e2e owner diversity
    encoding 'UTF8'
    lc_collate 'fr_FR.UTF-8'
    lc_ctype 'fr_FR.UTF-8'
    template=template0;
