export default {
	// 图片等静态资源拼接前缀：走同源代理（vue.config.js 中 /springbootj8kskvkr → 8080），不再指向外部 IP
	baseUrl: '/springbootj8kskvkr/',
	name: '/springbootj8kskvkr',
	indexNav: [
		{
			name: '好茶集市',
			url: '/index/shangpinxinxi',
		},
		{
            name: '茶文化',
            url: '/index/jiaoxueshipin'
        },
		{
			name: '购物资讯',
			url: '/index/news',
		},
		
		{
			name: '线上讲座',
			url: '/index/xinlizixun',
		},
		{
			name: '茶友圈',
			url: '/index/forum',
		},
		{
			name: '留言板',
			url: '/index/messages'
		},
	],
	cateList: [
	]
}
