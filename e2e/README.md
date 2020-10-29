# End-to-end tests

This project contains the end-to-end tests for the project.
It uses [Cypress](https://www.cypress.io/) to run the tests on the running application.

## Launch tests during development

Launch the backend application with the `dev` and `e2e` Spring profiles, and then run:

    yarn e2e

Note that the `admin` test is expected to fail when run this way. See the `admin.spec.ts` file
for explanations. 

## Format the code

The backend HTML templates can be formatted using prettier by running

    yarn format:backend-templates
    
The code in this project can be formatted using prettier by running

    yarn format
