# Admin

This project contains the frontend of the administration application, developed using Angular,
and relying on HTTP services exposed by the backend project.

It's a typical Angular-CLI project, using [Yarn](https://classic.yarnpkg.com/lang/en/) as 
dependency manager.

During development, it's advised to run `ng serve` from this directory to serve the application
on `http://localhost:4200`, in watch mode.
Note that the `ng serve` server is also configured to act as a reverse proxy for the backend,
so all the public-facing application and the HTTP services are also 
exposed on the port 4200. This replicates the production deployment, where everything is also
served by the backend.

## Additional tasks useful during development

 - `ng test` runs the unit tests
 - `yarn format` formats the sources with prettier
 - `ng generate` allows creating new components, services, etc.
 
Note that `ng e2e` can't be used on this project: the end-to-end tests are written using 
cypress, in the [`e2e`](../e2e/README.md) project.
