const { merge } = require('webpack-merge');
const common = require('./webpack.common.js');
const CompressionPlugin = require('compression-webpack-plugin');

module.exports = {
  plugins: [new CompressionPlugin()],
};

module.exports = merge(common, {
  mode: 'production',
  plugins: [
    new CompressionPlugin({
      filename: '[path][base].gz'
    }),
    new CompressionPlugin({
      filename: '[path][base].br',
      algorithm: 'brotliCompress',
      compressionOptions: {
        level: 11
      }
    })
  ]
});
