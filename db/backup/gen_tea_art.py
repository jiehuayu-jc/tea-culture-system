# -*- coding: utf-8 -*-
"""生成夜茶·墨绿金统一风格的茶主题插画，覆盖模板库存图"""
import math, random
from PIL import Image, ImageDraw, ImageFilter, ImageFont

FONT = r"C:\Users\14483\Desktop\springbootj8kskvkr\src\main\resources\front\front\src\assets\fonts\SourceHanSerifCN-Heavy.otf"
OUT = r"C:\Users\14483\Desktop\springbootj8kskvkr\src\main\resources\static\upload"

INK = (12, 27, 20)
DEEP = (20, 53, 42)
GREEN = (62, 107, 79)
SOFT = (124, 155, 132)
GOLD = (212, 175, 55)
CHAMP = (230, 206, 154)
CINNABAR = (165, 61, 52)
RICE = (246, 243, 236)
CREAM = (239, 233, 218)

def lerp(a, b, t):
    return tuple(int(a[i] + (b[i] - a[i]) * t) for i in range(3))

def vgrad(w, h, c1, c2):
    img = Image.new("RGB", (w, h))
    d = ImageDraw.Draw(img)
    for y in range(h):
        d.line([(0, y), (w, y)], fill=lerp(c1, c2, y / max(h - 1, 1)))
    return img

def font(sz):
    return ImageFont.truetype(FONT, sz)

def stamp(img, text, box, fg=RICE, bg=CINNABAR, fs=44):
    """朱砂印章"""
    d = ImageDraw.Draw(img, "RGBA")
    x0, y0, x1, y1 = box
    d.rounded_rectangle(box, radius=10, fill=bg + (235,))
    f = font(fs)
    tw = d.textlength(text, font=f)
    d.text(((x0 + x1 - tw) / 2, y0 + (y1 - y0 - fs * 1.25) / 2), text, font=f, fill=fg)

def vtext(img, text, x, y, fs=40, color=CHAMP, spacing=1.35):
    """竖排题字"""
    f = font(fs)
    d = ImageDraw.Draw(img)
    step = int(fs * spacing)
    for i, ch in enumerate(text):
        d.text((x, y + i * step), ch, font=f, fill=color)

def hill_layer(img, base_y, color, peaks, alpha=255, jitter=0):
    """一层连绵山影"""
    ov = Image.new("RGBA", img.size, (0, 0, 0, 0))
    d = ImageDraw.Draw(ov)
    w, h = img.size
    pts = [(0, h)]
    rnd = random.Random(sum(color) + base_y)
    x = 0
    y = base_y
    while x < w:
        pts.append((x, y))
        x += w / peaks
        y = base_y - abs(math.sin(x / 180 + jitter)) * (h * 0.09) - rnd.randint(0, int(h * 0.03))
    pts.append((w, h))
    d.polygon(pts, fill=color + (alpha,))
    img.paste(ov, (0, 0), ov)

def mist(img, y, wfrac, alpha=26):
    ov = Image.new("RGBA", img.size, (0, 0, 0, 0))
    d = ImageDraw.Draw(ov)
    w, h = img.size
    d.ellipse([w * (1 - wfrac) / 2 - 60, y, w * wfrac + w * (1 - wfrac) / 2 + 60, y + h * 0.07],
              fill=(236, 240, 230, alpha))
    img.paste(ov, (0, 0), ov)

def moon(img, cx, cy, r, ring=True):
    ov = Image.new("RGBA", img.size, (0, 0, 0, 0))
    d = ImageDraw.Draw(ov)
    d.ellipse([cx - r, cy - r, cx + r, cy + r], fill=CHAMP + (52,))
    if ring:
        d.ellipse([cx - r - 14, cy - r - 14, cx + r + 14, cy + r + 14], outline=GOLD + (150,), width=2)
    img.paste(ov, (0, 0), ov)

def leaf(img, cx, cy, ln, ang, color=GREEN, alpha=220):
    ov = Image.new("RGBA", (ln * 2, ln * 2), (0, 0, 0, 0))
    d = ImageDraw.Draw(ov)
    d.ellipse([ln // 2, ln // 4, ln * 3 // 2, ln * 7 // 4], fill=color + (alpha,))
    d.line([(ln, ln * 7 // 4), (ln, ln // 4)], fill=(236, 240, 230, 160), width=3)
    ov = ov.rotate(ang, expand=False, resample=Image.BICUBIC)
    img.paste(ov, (int(cx - ln), int(cy - ln)), ov)

def steam(img, x, y, h, alpha=70):
    ov = Image.new("RGBA", img.size, (0, 0, 0, 0))
    d = ImageDraw.Draw(ov)
    for k, dx in enumerate((-14, 4, 20)):
        pts = []
        for i in range(h):
            t = i / h
            pts.append((x + dx + math.sin(i / 14 + k) * 9 * (1 - t), y - i))
        d.line(pts, fill=(240, 240, 230, int(alpha * (1 - 0.7 * (k / 3)))), width=4)
    img.paste(ov, (0, 0), ov)

def frame(img, inset=28, color=GOLD, alpha=140):
    d = ImageDraw.Draw(img, "RGBA")
    w, h = img.size
    d.rectangle([inset, inset, w - inset, h - inset], outline=color + (alpha,), width=3)

def save(img, name):
    img.convert("RGB").save(f"{OUT}\\{name}", "JPEG", quality=86)
    print(name)

# ---------------- 商品图 1200x900（浅底，适合白卡内嵌） ----------------
def product_base(title, seal="茶"):
    img = vgrad(1200, 900, (238, 238, 228), (216, 226, 210))
    d = ImageDraw.Draw(img, "RGBA")
    d.ellipse([880, 620, 1160, 900], fill=GREEN + (36,))
    hill_layer(img, 780, (124, 155, 132), 6, alpha=60, jitter=1)
    hill_layer(img, 830, (94, 130, 104), 5, alpha=80, jitter=2)
    stamp(img, seal, (64, 60, 150, 146))
    vtext(img, title, 1090, 90, fs=52, color=(62, 80, 66))
    frame(img)
    return img

# 1 龙井杯
img = product_base("明前龙井")
d = ImageDraw.Draw(img, "RGBA")
d.polygon([(420, 560), (760, 560), (710, 760), (470, 760)], fill=(250, 250, 244, 255))
d.ellipse([405, 540, 775, 600], fill=(226, 232, 220, 255))
d.ellipse([440, 552, 740, 592], fill=(196, 214, 160, 255))
for i, lx in enumerate((560, 610, 660)):
    leaf(img, lx, 430 + i * 8, 46, -20 + i * 18, alpha=230)
steam(img, 590, 470, 210)
save(img, "shangpinxinxi_shangpintupian1.jpg")

# 2 茶叶罐
img = product_base("碧螺春", seal="罐")
d = ImageDraw.Draw(img, "RGBA")
d.rounded_rectangle([470, 380, 710, 760], radius=44, fill=DEEP + (255,))
d.ellipse([470, 330, 710, 440], fill=(30, 66, 50, 255))
d.ellipse([500, 350, 680, 420], outline=GOLD + (200,), width=4)
d.text((560, 500), "茶", font=font(96), fill=CHAMP + (230,))
for i, (lx, ly) in enumerate(((830, 700), (890, 730), (860, 660))):
    leaf(img, lx, ly, 52, 10 + i * 24, alpha=235)
save(img, "shangpinxinxi_shangpintupian2.jpg")

# 3 盖碗
img = product_base("盖碗", seal="碗")
d = ImageDraw.Draw(img, "RGBA")
d.polygon([(400, 520), (780, 520), (700, 720), (480, 720)], fill=(250, 250, 244, 255))
d.ellipse([385, 495, 795, 560], fill=(238, 240, 232, 255))
d.ellipse([520, 620, 660, 780], fill=(250, 250, 244, 255))
d.ellipse([452, 700, 728, 760], fill=(226, 230, 220, 255))
d.ellipse([545, 440, 635, 505], fill=(226, 230, 220, 255))
d.ellipse([565, 455, 615, 492], fill=(196, 214, 160, 255))
steam(img, 590, 420, 200)
save(img, "shangpinxinxi_shangpintupian3.jpg")

# 4 紫砂壶
img = product_base("紫砂壶", seal="壶")
d = ImageDraw.Draw(img, "RGBA")
d.ellipse([420, 480, 760, 740], fill=(122, 62, 44, 255))
d.polygon([(430, 520), (330, 470), (345, 510), (440, 560)], fill=(122, 62, 44, 255))
d.ellipse([715, 545, 790, 620], fill=(122, 62, 44, 255))
d.ellipse([540, 440, 640, 510], fill=(108, 54, 38, 255))
d.ellipse([555, 455, 625, 495], fill=(122, 62, 44, 255))
d.ellipse([500, 560, 680, 700], outline=(214, 178, 96, 120), width=4)
save(img, "shangpinxinxi_shangpintupian4.jpg")

# 5 普洱茶饼
img = product_base("普洱陈饼", seal="饼")
d = ImageDraw.Draw(img, "RGBA")
d.ellipse([400, 380, 780, 760], fill=(96, 72, 50, 255))
d.ellipse([430, 410, 750, 730], fill=(116, 88, 60, 255))
d.ellipse([545, 525, 635, 615], outline=(214, 178, 96, 200), width=5)
d.text((562, 538), "普", font=font(64), fill=(214, 178, 96, 220))
d.ellipse([420, 400, 760, 740], outline=(80, 58, 40, 255), width=8)
save(img, "shangpinxinxi_shangpintupian5.jpg")

# 6 白毫银针
img = product_base("白毫银针", seal="白")
d = ImageDraw.Draw(img, "RGBA")
for i in range(7):
    x = 470 + i * 42
    d.line([(x, 740), (x + 18, 430 - (i % 3) * 30)], fill=(226, 220, 200, 255), width=10)
    d.ellipse([x + 8, 400 - (i % 3) * 30, x + 34, 448 - (i % 3) * 30], fill=(240, 236, 220, 255))
d.rounded_rectangle([420, 730, 760, 780], radius=24, fill=DEEP + (255,))
save(img, "shangpinxinxi_shangpintupian6.jpg")

# 7 红茶杯
img = product_base("祁门红茶", seal="红")
d = ImageDraw.Draw(img, "RGBA")
d.polygon([(430, 540), (750, 540), (700, 750), (480, 750)], fill=(250, 250, 244, 255))
d.ellipse([415, 520, 765, 585], fill=(238, 240, 232, 255))
d.ellipse([450, 534, 730, 578], fill=(190, 96, 58, 255))
d.arc([735, 470, 835, 610], start=-70, end=90, fill=(214, 178, 96, 220), width=9)
steam(img, 590, 460, 190)
save(img, "shangpinxinxi_shangpintupian7.jpg")

# 8 茶道组
img = product_base("茶道六君子", seal="器")
d = ImageDraw.Draw(img, "RGBA")
d.rounded_rectangle([380, 660, 800, 760], radius=30, fill=DEEP + (255,))
d.ellipse([660, 520, 800, 700], fill=(226, 230, 220, 255))
d.polygon([(560, 700), (600, 460), (640, 700)], fill=(94, 130, 104, 255))
d.line([(470, 700), (450, 500)], fill=(214, 178, 96, 230), width=8)
d.ellipse([428, 468, 472, 512], outline=(214, 178, 96, 230), width=6)
save(img, "shangpinxinxi_shangpintupian8.jpg")

# ---------------- 封面图 1600x1000（深底，栏目/讲座/资讯/论坛通用） ----------------
def cover_base(title_ch, en, moon_x=1240):
    img = vgrad(1600, 1000, (16, 42, 30), INK)
    moon(img, moon_x, 260, 130)
    hill_layer(img, 640, (24, 66, 46), 5, alpha=200, jitter=1)
    mist(img, 600, 0.8, 34)
    hill_layer(img, 760, (16, 46, 32), 4, alpha=235, jitter=2)
    mist(img, 800, 0.9, 26)
    hill_layer(img, 880, (10, 26, 18), 6, alpha=255, jitter=3)
    stamp(img, "茶", (72, 68, 148, 148))
    d = ImageDraw.Draw(img)
    d.text((86, 190), title_ch, font=font(88), fill=CHAMP)
    d.text((92, 300), en, font=font(30), fill=(147, 163, 150))
    frame(img, 34)
    return img

save(cover_base("茶山叠翠", "TEA MOUNTAINS"), "forum_cover1.jpg")
img = cover_base("煮水候汤", "BOILING WATER", moon_x=1180)
d = ImageDraw.Draw(img, "RGBA")
d.ellipse([620, 640, 900, 800], fill=(30, 66, 50, 255))
d.ellipse([680, 560, 840, 680], fill=(24, 54, 40, 255))
steam(img, 760, 560, 240, alpha=90)
save(img, "forum_cover2.jpg")
img = cover_base("茶席一隅", "TEA TABLE", moon_x=1220)
d = ImageDraw.Draw(img, "RGBA")
d.rounded_rectangle([420, 700, 1180, 790], radius=22, fill=(30, 66, 50, 255))
d.polygon([(600, 700), (760, 700), (730, 620), (630, 620)], fill=(238, 240, 232, 255))
d.ellipse([860, 640, 980, 710], fill=(238, 240, 232, 255))
d.ellipse([885, 655, 955, 700], fill=(190, 96, 58, 255))
leaf(img, 520, 660, 60, 14, alpha=230)
save(img, "forum_cover3.jpg")
save(cover_base("采茶时节", "PICKING SEASON", moon_x=300), "forum_cover4.jpg")
img = cover_base("茶经卷页", "CLASSIC OF TEA", moon_x=1200)
d = ImageDraw.Draw(img, "RGBA")
d.polygon([(560, 660), (1040, 660), (1000, 780), (600, 780)], fill=(238, 233, 218, 255))
for i in range(4):
    d.line([(620 + i * 6, 690 + i * 4), (980 - i * 6, 690 + i * 4)], fill=(120, 130, 116, 200), width=5)
save(img, "forum_cover5.jpg")
img = cover_base("月下茶窗", "MOONLIT WINDOW", moon_x=1150)
d = ImageDraw.Draw(img, "RGBA")
d.rounded_rectangle([540, 420, 1060, 820], radius=18, outline=GOLD + (170,), width=6)
d.line([(800, 420), (800, 820)], fill=GOLD + (120,), width=4)
d.ellipse([580, 540, 700, 660], fill=(238, 240, 232, 255))
d.ellipse([905, 560, 1005, 650], fill=(190, 96, 58, 255))
save(img, "forum_cover6.jpg")
save(cover_base("古 道 茶 香", "TEA HORSE ROAD", moon_x=1280), "forum_cover7.jpg")
img = cover_base("雨 后 茶 园", "AFTER THE RAIN", moon_x=330)
d = ImageDraw.Draw(img, "RGBA")
for i, (x, y, r) in enumerate(((500, 700, 60), (700, 720, 70), (900, 700, 58), (1100, 730, 66))):
    leaf(img, x, y, r, 10 + i * 16, alpha=225)
save(img, "forum_cover8.jpg")

# ---------------- 横幅 1920x820（首页 Hero / 轮播） ----------------
def banner(title, sub, moon_x):
    img = vgrad(1920, 820, (16, 42, 30), (10, 25, 17))
    moon(img, moon_x, 300, 170)
    hill_layer(img, 540, (24, 66, 46), 5, alpha=210, jitter=1)
    mist(img, 520, 0.75, 36)
    hill_layer(img, 650, (16, 46, 32), 4, alpha=240, jitter=2)
    mist(img, 700, 0.9, 24)
    hill_layer(img, 730, (10, 26, 18), 6, alpha=255, jitter=3)
    d = ImageDraw.Draw(img)
    d.text((120, 300), title, font=font(120), fill=CHAMP)
    d.text((128, 470), sub, font=font(34), fill=(147, 163, 150))
    stamp(img, "夜茶", (128, 200, 268, 260), fs=40)
    frame(img, 36)
    return img

save(banner("一 盏 春 色", "半席山河 · 好茶集市", 1420), "picture1.jpg")
save(banner("茶 事 美 学", "六大茶类 · 冲泡技艺 · 茶席之道", 1380), "picture2.jpg")
save(banner("以 茶 会 友", "茶友圈 · 线上讲座 · 每周上新", 1440), "picture3.jpg")

print("全部完成")
