# Diversity frontend

This project contains the sources for the CSS and JavaScript of the public-facing application.

The CSS is written using [Sass](https://sass-lang.com/) (although most of it is actually provided
by the designer, Jean-Baptiste Giffard, which uses plain CSS, but CSS is valid Sass).

The JavaScript is written in [TypeScript](https://www.typescriptlang.org/).

Everything is built with [webpack](https://webpack.js.org/), and dependencies are managed with [Yarn](https://classic.yarnpkg.com/lang/en/).

## Available commands

 - `yarn watch`: generates the JS bundle and CSS stylesheet in watch mode (i.e. it watches changes in the 
   source files and rebuilds automatically when they change). This is what should be run during development.
   Inline source maps are generated and the code is not minified.
 - `yarn watch:prod`: same as `watch` but in production mode: the files are minified and no source map is generated.
 - `yarn build`: same as `watch`, but without watch mode: the command builds only once and exits.
 - `yarn build:prod`: same as `watch:prod`, but without watch mode: the command builds only once and exits. This
   is what is used by the Gradle build to generate the files and include them in the application jar file.
