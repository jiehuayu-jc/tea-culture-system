// var webpack = require('webpack');
//vue2
const path = require('path')
function resolve(dir) {
    return path.join(__dirname, dir)
}
function publicPath(){
    if (process.env.NODE_ENV == 'production') {
        // 部署在 Spring Boot 的 /springbootj8kskvkr/admin/ 路径下，必须用绝对路径。
        // 原值为 "././"（相对路径）：生产构建会同时生成 rel="preload" 与
        // rel="stylesheet" 两条链接且都带 ./ 前缀，preload 缺失 crossorigin 时会
        // 污染缓存条目，导致浏览器跳过样式表加载 —— 表现为 HTML/JS 正常但页面
        // 完全无样式。
        return "/springbootj8kskvkr/admin/";
    } else {
        return "/";
    }
}
// vue.config.js
module.exports = {
    // publicPath:"././",
    publicPath: publicPath(),
    // 国际化配置 使用其它语言，默认情况下中文语言包依旧是被引入的
    configureWebpack: {
        // plugins: [
        //     new webpack.NormalModuleReplacementPlugin(/element-ui[\/\\]lib[\/\\]locale[\/\\]lang[\/\\]zh-CN/, 'element-ui/lib/locale/lang/en')
        // ]
        resolve: {
            alias: {
                '@': resolve('src')
            }
        }
    },
    css: {
        loaderOptions: {
            scss: {
                implementation: require('sass')
            },
            sass: {
                implementation: require('sass')
            }
        }
    },
lintOnSave: false,
    devServer: {
        host: "0.0.0.0", //指定使用一个 host。默认是 localhost，这里默认值即可
        port: 8081, //指定端口
        hot: true, // 开启热更新
        https: false, // 是否开启https模式
        proxy: {
            '/springbootj8kskvkr': {
                target: 'http://127.0.0.1:8080',
                changeOrigin: true,
                secure: false,
            }
        }
    },
chainWebpack(config) {
    config.module
      .rule('svg')
      .exclude.add(resolve('src/icons'))
      .end()
    config.module
      .rule('icons')
      .test(/\.svg$/)
      .include.add(resolve('src/icons'))
      .end()
      .use('svg-sprite-loader')
      .loader('svg-sprite-loader')
      .options({
        symbolId: 'icon-[name]'
      })
      .end()
}
}
