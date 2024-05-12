const {defineConfig} = require('@vue/cli-service')

const AutoImport = require('unplugin-auto-import/webpack')
const Components = require("unplugin-vue-components/webpack")
const {ElementPlusResolver} = require('unplugin-vue-components/resolvers')
const CompressionWebpackPlugin = require('compression-webpack-plugin')
const productionGzipExtensions = ['js', 'css']
const WebpackCdnPlugin = require('webpack-cdn-plugin');

module.exports = defineConfig({
    transpileDependencies: true,
    lintOnSave: false,
    publicPath: '/',
    configureWebpack: {
        devtool: "source-map",
        plugins: [
            AutoImport({
                resolvers: [ElementPlusResolver()],
            }),
            Components({
                resolvers: [ElementPlusResolver()],
            }),
            // 使用CompressionWebpackPlugin插件减小项目打包的文件大小
            new CompressionWebpackPlugin({
                algorithm: 'gzip',
                //test: /\.js$|\.css$|\.html$/, // 压缩匹配的文件
                test: new RegExp('\\.(' + productionGzipExtensions.join('|') + ')$'), // 压缩匹配的文件
                threshold: 10240, // 对超过10k的数据进行压缩
                deleteOriginalAssets: false, // 是否删除原文件
                minRatio: 0.8 // 最小压缩比达到80%就不再压缩
            }),
            new WebpackCdnPlugin({
                modules: [
                    {
                        name: 'echarts',
                        var: 'echarts',
                        path: 'dist/echarts.min.js'
                    }
                ],
                publicPath: '/node_modules'
            })
        ]
    },
    devServer: {
        client: {
            overlay: false // 关闭浏览器错误提示,不弹出全屏错误
        }
    }
})
