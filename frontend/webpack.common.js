const path = require('path');
const webpack = require('webpack');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const TerserJSPlugin = require('terser-webpack-plugin');
const CssMinimizerPlugin = require("css-minimizer-webpack-plugin");
const IgnoreEmitPlugin = require('ignore-emit-webpack-plugin');

module.exports = {
  optimization: {
    // this is applied only in production mode
    minimizer: [new TerserJSPlugin(), new CssMinimizerPlugin()],
  },
  entry: {
    // a JS bundle is generated for the index.ts entry point. Since the application is really small
    // and does not have much JavaScript logic, a single bundle is sufficient
    bundle: './src/index.ts',
    // A CSS bundle is generated for the style.scss entry point.
    style: './src/style/style.scss'
  },
  plugins: [
    // cleans the output directory before each build
    new CleanWebpackPlugin(),
    // allows extracting the CSS into a CSS file instead of bundling it in a JS file
    new MiniCssExtractPlugin(),
    // allows to avoid emitting the useless style\.js file in addition to the style.css file
    new IgnoreEmitPlugin(/^style\.js$/),
    // displays progress while building
    new webpack.ProgressPlugin()
  ],
  module: {
    rules: [
      // .ts files are loaded by the ts-loader
      {
        test: /\.ts$/,
        use: 'ts-loader',
        exclude: /node_modules/
      },
      // .scss (and .sass) files are loaded by the sass-loader, which transforms them into css loaded by the cdd-loader
      // which are then bundled into a css file by the MiniCssExtractPlugin loader
      {
        test: /\.s[ac]ss$/i,
        use: [
          {
            loader: MiniCssExtractPlugin.loader
          },
          {
            loader: 'css-loader',
            options: {
              url: false
            }
          },
          {
            loader: 'sass-loader'
          }
        ],
      },
    ],
  },
  resolve: {
    // our files are .ts files, but libraries are bundled in .js files
    extensions: [ '.ts', '.js' ],
  },
  output: {
    path: path.resolve(__dirname, 'build/dist'),
    filename: '[name].js'
  },
  // this configures what the progress plugin logs
  stats: {
    // nothing is logged except what is being set to true
    all: false,
    timings: true,
    builtAt: true,
    chunks: true,
    errors: true,
    errorDetails: true
  }
};
