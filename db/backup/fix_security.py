import re, pathlib

CTRL = pathlib.Path(r"C:\Users\14483\Desktop\springbootj8kskvkr\src\main\java\com\controller")
total = 0
for path in sorted(CTRL.glob("*Controller.java")):
    lines = path.read_text(encoding='utf-8').splitlines(keepends=True)
    idx = None
    for i, l in enumerate(lines):
        if '"/security"' in l:
            idx = i; break
    if idx is None:
        continue
    # 向前删除 javadoc（/** ... */）与紧邻注解行
    start = idx
    while start > 0 and (lines[start-1].strip().startswith('@') or lines[start-1].strip() == ''):
        start -= 1
    if start > 0 and lines[start-1].strip().endswith('*/'):
        start -= 1
        while start > 0 and not lines[start].strip().startswith('/**'):
            start -= 1
    # 向后找到方法结束的第一个独立 "}\s*$"
    end = idx
    opened = False
    while end < len(lines):
        if 'public R security' in lines[end]:
            opened = True
        if opened and re.match(r'^\s*}\s*$', lines[end]):
            break
        end += 1
    del lines[start:end+1]
    path.write_text(''.join(lines), encoding='utf-8')
    total += 1
    print(f"已删除 {path.name} 的 /security 方法（行 {start+1}-{end+1}）")
print(f"\n共处理 {total} 个文件")
