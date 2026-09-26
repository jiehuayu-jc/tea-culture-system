import re, pathlib, sys

CTRL = pathlib.Path(r"C:\Users\14483\Desktop\springbootj8kskvkr\src\main\java\com\controller")

# 每个文件里允许保留 @IgnoreAuth 的接口路径；其余全部移除注解
KEEP = {
    'AddressController.java': set(),
    'CartController.java': set(),
    'ChargerecordController.java': set(),
    'ChatController.java': set(),
    'ChatmessageController.java': set(),
    'CommonController.java': {'/option/{tableName}/{columnName}', '/encrypt/md5', '/baidu/askai'},
    'ConfigController.java': {'/list'},
    'CouponController.java': {'/list', '/detail/{id}'},
    'DiscussshangpinxinxiController.java': {'/list'},
    'FileController.java': {'/download'},
    'ForumController.java': {'/list/{id}', '/detail/{id}', '/flist'},
    'ForumreportController.java': set(),
    'ForumtypeController.java': {'/list'},
    'FriendController.java': set(),
    'JiaoxueshipinController.java': {'/list', '/detail/{id}', '/autoSort'},
    'MessagesController.java': set(),
    'MycouponController.java': set(),
    'NewsController.java': {'/list', '/detail/{id}', '/autoSort'},
    'NewstypeController.java': {'/list'},
    'OrdersController.java': set(),
    'ShangjiaController.java': set(),
    'ShangpinfenleiController.java': {'/list'},
    'ShangpinxinxiController.java': {'/list', '/detail/{id}', '/autoSort'},
    'StoreupController.java': set(),
    'SyslogController.java': set(),
    'XinlizixunController.java': {'/list', '/detail/{id}'},
    'YonghuController.java': set(),
    'YuyuezixunController.java': set(),
}

RM_RE = re.compile(r'@RequestMapping\s*(?:\(\s*value\s*=\s*)?\(\s*"([^"]+)"')

total_removed, total_kept = 0, 0
for name, keep in KEEP.items():
    path = CTRL / name
    if not path.exists():
        print(f"!! 缺少 {name}"); continue
    lines = path.read_text(encoding='utf-8').splitlines(keepends=True)
    out, removed, kept = [], 0, 0
    i = 0
    while i < len(lines):
        line = lines[i]
        if '@IgnoreAuth' in line:
            # 向后找最近的一个 @RequestMapping 提取路径
            j = i + 1
            mapping = None
            while j < len(lines) and j < i + 6:
                m = RM_RE.search(lines[j])
                if m:
                    mapping = m.group(1); break
                j += 1
            if mapping is not None and mapping in keep:
                out.append(line); kept += 1
            else:
                removed += 1  # 丢弃该注解行
            i += 1
        else:
            out.append(line); i += 1
    if removed or kept:
        path.write_text(''.join(out), encoding='utf-8')
    total_removed += removed; total_kept += kept
    print(f"{name}: 移除 {removed} 处, 保留 {kept} 处")

print(f"\n合计: 移除 {total_removed}, 保留 {total_kept}")
