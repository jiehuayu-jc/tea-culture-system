const menu = {
    list() {
        return [{
            "backMenu": [{
                "child": [{
                    "allButtons": ["新增", "查看", "修改", "删除", "用户性别统计", "首页总数", "首页统计", "私聊"],
                    "appFrontIcon": "cuIcon-copy",
                    "buttons": ["新增", "查看", "修改", "删除", "首页总数", "首页统计"],
                    "menu": "用户",
                    "menuJump": "列表",
                    "tableName": "yonghu"
                }],
                "menu": "用户管理"
            }, {
                "child": [{
                    "allButtons": ["新增", "查看", "修改", "删除", "审核"],
                    "appFrontIcon": "cuIcon-vip",
                    "buttons": ["新增", "查看", "修改", "删除", "审核"],
                    "menu": "茶商",
                    "menuJump": "列表",
                    "tableName": "shangjia"
                }],
                "menu": "茶商管理"
            }, {
                "child": [{
                    "allButtons": ["新增", "查看", "修改", "删除", "查看评论"],
                    "appFrontIcon": "cuIcon-shop",
                    "buttons": ["新增", "查看", "修改", "删除", "查看评论"],
                    "menu": "茶文化",
                    "menuJump": "列表",
                    "tableName": "jiaoxueshipin"
                }],
                "menu": "茶文化管理"
            }, {
                "child": [{
                    "allButtons": ["新增", "查看", "修改", "删除", "商品词云图统计", "商品分类统计", "商品类型浏览量统计", "查看评论", "首页总数", "首页统计"],
                    "appFrontIcon": "cuIcon-copy",
                    "buttons": ["新增", "查看", "修改", "删除", "查看评论", "首页总数", "首页统计"],
                    "menu": "好茶集市",
                    "menuJump": "列表",
                    "tableName": "shangpinxinxi"
                }],
                "menu": "好茶集市管理"
            }, {
                "child": [{
                    "allButtons": ["新增", "查看", "修改", "删除"],
                    "appFrontIcon": "cuIcon-paint",
                    "buttons": ["新增", "查看", "修改", "删除"],
                    "menu": "商品分类",
                    "menuJump": "列表",
                    "tableName": "shangpinfenlei"
                }],
                "menu": "商品分类管理"
            }, {
                "child": [{
                    "allButtons": ["新增", "查看", "修改", "删除"],
                    "appFrontIcon": "cuIcon-keyboard",
                    "buttons": ["新增", "查看", "修改", "删除"],
                    "menu": "论坛分类",
                    "tableName": "forumtype"
                }],
                "menu": "论坛分类管理"
            }, {
                "child": [{
                    "allButtons": ["新增", "查看", "修改", "删除"],
                    "appFrontIcon": "cuIcon-present",
                    "buttons": ["新增", "查看", "修改", "删除"],
                    "menu": "举报记录",
                    "tableName": "forumreport"
                }],
                "menu": "举报记录管理"
            }, {
                "child": [{
                    "allButtons": ["新增", "查看", "修改", "删除"],
                    "appFrontIcon": "cuIcon-list",
                    "buttons": ["新增", "查看", "修改", "删除"],
                    "menu": "优惠券",
                    "tableName": "coupon"
                }],
                "menu": "优惠券管理"
            }, {
                "child": [{
                    "allButtons": ["查看", "修改", "回复", "删除"],
                    "appFrontIcon": "cuIcon-message",
                    "buttons": ["查看", "修改", "回复", "删除"],
                    "menu": "留言板",
                    "tableName": "messages"
                }],
                "menu": "留言板"
            }, {
                "child": [{
                    "allButtons": ["新增", "查看", "修改", "删除"],
                    "appFrontIcon": "cuIcon-group",
                    "buttons": ["查看", "删除"],
                    "menu": "茶友圈",
                    "tableName": "forum"
                }],
                "menu": "茶友圈"
            }, {
                "child": [{
                    "allButtons": ["新增", "查看", "修改", "删除"],
                    "appFrontIcon": "cuIcon-news",
                    "buttons": ["新增", "查看", "修改", "删除"],
                    "menu": "购物资讯",
                    "tableName": "news"
                }, {
                    "allButtons": ["新增", "查看", "修改", "删除"],
                    "appFrontIcon": "cuIcon-news",
                    "buttons": ["新增", "查看", "修改", "删除"],
                    "menu": "购物资讯分类",
                    "tableName": "newstype"
                }, {
                    "allButtons": ["新增", "查看", "修改", "删除"],
                    "appFrontIcon": "cuIcon-service",
                    "buttons": ["查看", "删除"],
                    "menu": "智能AI",
                    "tableName": "chat"
                }, {
                    "allButtons": ["新增", "查看", "修改", "删除"],
                    "appFrontIcon": "cuIcon-skin",
                    "buttons": ["查看", "修改"],
                    "menu": "轮播图管理",
                    "tableName": "config"
                }],
                "menu": "系统管理"
            }, {
                "child": [{
                    "allButtons": ["新增", "查看", "修改", "删除", "导出", "日销量", "月销量", "年销量", "品销量", "类销量", "日销额", "月销额", "年销额", "品销额", "类销额"],
                    "appFrontIcon": "cuIcon-list",
                    "buttons": ["查看", "删除"],
                    "menu": "已取消订单",
                    "tableName": "orders/已取消"
                }, {
                    "allButtons": ["新增", "查看", "修改", "删除", "导出", "日销量", "月销量", "年销量", "品销量", "类销量", "日销额", "月销额", "年销额", "品销额", "类销额", "物流"],
                    "appFrontIcon": "cuIcon-paint",
                    "buttons": ["查看", "删除"],
                    "menu": "已退款订单",
                    "tableName": "orders/已退款"
                }, {
                    "allButtons": ["新增", "查看", "修改", "删除", "导出", "日销量", "月销量", "年销量", "品销量", "类销量", "日销额", "月销额", "年销额", "品销额", "类销额", "确认收货", "物流"],
                    "appFrontIcon": "cuIcon-rank",
                    "buttons": ["查看", "删除"],
                    "menu": "已发货订单",
                    "tableName": "orders/已发货"
                }, {
                    "allButtons": ["新增", "查看", "修改", "删除", "导出", "日销量", "月销量", "年销量", "品销量", "类销量", "日销额", "月销额", "年销额", "品销额", "类销额"],
                    "appFrontIcon": "cuIcon-brand",
                    "buttons": ["查看", "删除"],
                    "menu": "未支付订单",
                    "tableName": "orders/未支付"
                }, {
                    "allButtons": ["新增", "查看", "修改", "删除", "导出", "日销量", "月销量", "年销量", "品销量", "类销量", "日销额", "月销额", "年销额", "品销额", "类销额", "发货", "物流", "核销"],
                    "appFrontIcon": "cuIcon-camera",
                    "buttons": ["查看", "删除"],
                    "menu": "已支付订单",
                    "tableName": "orders/已支付"
                }, {
                    "allButtons": ["新增", "查看", "修改", "删除", "导出", "日销量", "月销量", "年销量", "品销量", "类销量", "日销额", "月销额", "年销额", "品销额", "类销额", "物流", "退货审核"],
                    "appFrontIcon": "cuIcon-brand",
                    "buttons": ["查看", "删除", "日销量", "月销量", "年销量", "品销量", "日销额", "月销额", "年销额", "品销额"],
                    "menu": "已完成订单",
                    "tableName": "orders/已完成"
                }],
                "menu": "订单管理"
            }],
            "frontMenu": [{
                "child": [{
                    "allButtons": ["新增", "查看", "修改", "删除", "商品词云图统计", "商品分类统计", "商品类型浏览量统计", "查看评论", "首页总数", "首页统计"],
                    "appFrontIcon": "cuIcon-camera",
                    "buttons": ["查看"],
                    "menu": "好茶集市列表",
                    "menuJump": "列表",
                    "tableName": "shangpinxinxi"
                }],
                "menu": "好茶集市模块"
            }, {
                "child": [{
                    "allButtons": ["新增", "查看", "修改", "删除"],
                    "appFrontIcon": "cuIcon-news",
                    "buttons": ["查看"],
                    "menu": "购物资讯列表",
                    "tableName": "news"
                }],
                "menu": "购物资讯模块"
            }, {
                "child": [{
                    "allButtons": ["查看", "修改", "回复", "删除"],
                    "appFrontIcon": "cuIcon-message",
                    "buttons": ["查看"],
                    "menu": "留言板列表",
                    "tableName": "liuyanbanguanli"
                }],
                "menu": "留言板模块"
            }, {
                "child": [{
                    "allButtons": ["新增", "查看", "修改", "删除"],
                    "appFrontIcon": "cuIcon-group",
                    "buttons": ["查看"],
                    "menu": "茶友圈列表",
                    "tableName": "forum"
                }],
                "menu": "茶友圈模块"
            }],
            "hasBackLogin": "是",
            "hasBackRegister": "否",
            "hasFrontLogin": "否",
            "hasFrontRegister": "否",
            "roleName": "管理员",
            "tableName": "users"
        }, {
            "backMenu": [{
                "child": [{
                    "allButtons": ["新增", "查看", "修改", "删除"],
                    "appFrontIcon": "cuIcon-favor",
                    "buttons": ["新增", "查看", "修改", "删除"],
                    "menu": "我的收藏",
                    "menuJump": "1",
                    "tableName": "storeup"
                }],
                "menu": "我的收藏管理"
            }],
            "frontMenu": [{
                "child": [{
                    "allButtons": ["新增", "查看", "修改", "删除", "商品词云图统计", "商品分类统计", "商品类型浏览量统计", "查看评论", "首页总数", "首页统计"],
                    "appFrontIcon": "cuIcon-camera",
                    "buttons": ["查看"],
                    "menu": "好茶集市列表",
                    "menuJump": "列表",
                    "tableName": "shangpinxinxi"
                }],
                "menu": "好茶集市模块"
            }, {
                "child": [{
                    "allButtons": ["新增", "查看", "修改", "删除"],
                    "appFrontIcon": "cuIcon-news",
                    "buttons": ["查看"],
                    "menu": "购物资讯列表",
                    "tableName": "news"
                }],
                "menu": "购物资讯模块"
            }, {
                "child": [{
                    "allButtons": ["查看", "修改", "回复", "删除"],
                    "appFrontIcon": "cuIcon-message",
                    "buttons": ["查看"],
                    "menu": "留言板列表",
                    "tableName": "liuyanbanguanli"
                }],
                "menu": "留言板模块"
            }, {
                "child": [{
                    "allButtons": ["新增", "查看", "修改", "删除"],
                    "appFrontIcon": "cuIcon-group",
                    "buttons": ["查看"],
                    "menu": "茶友圈列表",
                    "tableName": "forum"
                }],
                "menu": "茶友圈模块"
            }],
            "hasBackLogin": "否",
            "hasBackRegister": "否",
            "hasFrontLogin": "是",
            "hasFrontRegister": "是",
            "roleName": "用户",
            "tableName": "yonghu"
        }, {
            "backMenu": [{
                "child": [{
                    "allButtons": ["新增", "查看", "修改", "删除", "商品词云图统计", "商品分类统计", "商品类型浏览量统计", "查看评论", "首页总数", "首页统计"],
                    "appFrontIcon": "cuIcon-copy",
                    "buttons": ["新增", "查看", "查看评论", "删除", "修改"],
                    "menu": "好茶集市",
                    "menuJump": "列表",
                    "tableName": "shangpinxinxi"
                }],
                "menu": "好茶集市管理"
            }, {
                "child": [{
                    "allButtons": ["新增", "查看", "修改", "删除"],
                    "appFrontIcon": "cuIcon-list",
                    "buttons": ["新增", "查看", "修改", "删除"],
                    "menu": "优惠券",
                    "tableName": "coupon"
                }],
                "menu": "优惠券管理"
            }, {
                "child": [{
                    "allButtons": ["新增", "查看", "修改", "删除"],
                    "appFrontIcon": "cuIcon-group",
                    "buttons": ["查看", "删除"],
                    "menu": "茶友圈",
                    "tableName": "forum"
                }],
                "menu": "茶友圈"
            }, {
                "child": [{
                    "allButtons": ["新增", "查看", "修改", "删除"],
                    "appFrontIcon": "cuIcon-present",
                    "buttons": ["新增", "查看", "修改", "删除"],
                    "menu": "举报记录",
                    "tableName": "forumreport"
                }],
                "menu": "举报记录管理"
            }, {
                "child": [{
                    "allButtons": ["新增", "查看", "修改", "删除"],
                    "appFrontIcon": "cuIcon-news",
                    "buttons": ["新增", "查看", "修改", "删除"],
                    "menu": "购物资讯",
                    "tableName": "news"
                }, {
                    "allButtons": ["新增", "查看", "修改", "删除"],
                    "appFrontIcon": "cuIcon-service",
                    "buttons": ["查看", "删除"],
                    "menu": "智能AI",
                    "tableName": "chat"
                }],
                "menu": "系统管理"
            }, {
                "child": [{
                    "allButtons": ["新增", "查看", "修改", "删除", "导出", "日销量", "月销量", "年销量", "品销量", "类销量", "日销额", "月销额", "年销额", "品销额", "类销额", "确认收货", "物流"],
                    "appFrontIcon": "cuIcon-rank",
                    "buttons": ["删除", "查看"],
                    "menu": "已发货订单",
                    "tableName": "orders/已发货"
                }, {
                    "allButtons": ["新增", "查看", "修改", "删除", "导出", "日销量", "月销量", "年销量", "品销量", "类销量", "日销额", "月销额", "年销额", "品销额", "类销额"],
                    "appFrontIcon": "cuIcon-brand",
                    "buttons": ["查看", "删除"],
                    "menu": "未支付订单",
                    "tableName": "orders/未支付"
                }, {
                    "allButtons": ["新增", "查看", "修改", "删除", "导出", "日销量", "月销量", "年销量", "品销量", "类销量", "日销额", "月销额", "年销额", "品销额", "类销额", "发货", "物流", "核销"],
                    "appFrontIcon": "cuIcon-camera",
                    "buttons": ["删除", "查看", "发货", "物流"],
                    "menu": "已支付订单",
                    "tableName": "orders/已支付"
                }, {
                    "allButtons": ["新增", "查看", "修改", "删除", "导出", "日销量", "月销量", "年销量", "品销量", "类销量", "日销额", "月销额", "年销额", "品销额", "类销额", "物流", "退货审核"],
                    "appFrontIcon": "cuIcon-brand",
                    "buttons": ["查看", "删除", "日销量", "月销量", "年销量", "品销量", "日销额", "月销额", "年销额", "品销额", "物流", "退货审核"],
                    "menu": "已完成订单",
                    "tableName": "orders/已完成"
                }, {
                    "allButtons": ["新增", "查看", "修改", "删除", "导出", "日销量", "月销量", "年销量", "品销量", "类销量", "日销额", "月销额", "年销额", "品销额", "类销额"],
                    "appFrontIcon": "cuIcon-list",
                    "buttons": ["删除", "查看"],
                    "menu": "已取消订单",
                    "tableName": "orders/已取消"
                }, {
                    "allButtons": ["新增", "查看", "修改", "删除", "导出", "日销量", "月销量", "年销量", "品销量", "类销量", "日销额", "月销额", "年销额", "品销额", "类销额", "物流"],
                    "appFrontIcon": "cuIcon-paint",
                    "buttons": ["查看", "删除"],
                    "menu": "已退款订单",
                    "tableName": "orders/已退款"
                }],
                "menu": "订单管理"
            }],
            "frontMenu": [{
                "child": [{
                    "allButtons": ["新增", "查看", "修改", "删除", "商品词云图统计", "商品分类统计", "商品类型浏览量统计", "查看评论", "首页总数", "首页统计"],
                    "appFrontIcon": "cuIcon-camera",
                    "buttons": ["查看"],
                    "menu": "好茶集市列表",
                    "menuJump": "列表",
                    "tableName": "shangpinxinxi"
                }],
                "menu": "好茶集市模块"
            }, {
                "child": [{
                    "allButtons": ["新增", "查看", "修改", "删除"],
                    "appFrontIcon": "cuIcon-news",
                    "buttons": ["查看"],
                    "menu": "购物资讯列表",
                    "tableName": "news"
                }],
                "menu": "购物资讯模块"
            }, {
                "child": [{
                    "allButtons": ["查看", "修改", "回复", "删除"],
                    "appFrontIcon": "cuIcon-message",
                    "buttons": ["查看"],
                    "menu": "留言板列表",
                    "tableName": "liuyanbanguanli"
                }],
                "menu": "留言板模块"
            }, {
                "child": [{
                    "allButtons": ["新增", "查看", "修改", "删除"],
                    "appFrontIcon": "cuIcon-group",
                    "buttons": ["查看"],
                    "menu": "茶友圈列表",
                    "tableName": "forum"
                }],
                "menu": "茶友圈模块"
            }],
            "hasBackLogin": "是",
            "hasBackRegister": "是",
            "hasFrontLogin": "否",
            "hasFrontRegister": "否",
            "roleName": "茶商",
            "tableName": "shangjia"
        }]
    }
}
export default menu;
