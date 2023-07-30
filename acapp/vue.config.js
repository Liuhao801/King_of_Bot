const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  configureWebpack: {
    // No need for splitting 打包成一个文件
    optimization: {
      splitChunks: false
    }
  }
})