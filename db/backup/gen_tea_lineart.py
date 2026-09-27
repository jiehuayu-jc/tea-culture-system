# -*- coding: utf-8 -*-
"""夜茶·金线线稿风商品图：深墨绿底 + 香槟金线稿茶器 + 宋体竖排题字"""
import math
from PIL import Image, ImageDraw, ImageFont

FONT = r"C:\Users\14483\Desktop\springbootj8kskvkr\src\main\resources\front\front\src\assets\fonts\SourceHanSerifCN-Heavy.otf"
OUT = r"C:\Users\14483\Desktop\springbootj8kskvkr\src\main\resources\static\upload"

INK = (12, 27, 20)
DEEP = (18, 40, 30)
GOLD = (212, 175, 55)
CHAMP = (230, 206, 154)
CREAM = (238, 233, 218)
CINNABAR = (165, 61, 52)
MIST = (124, 155, 132)

W, H = 1200, 900
GOLD_SOFT = CHAMP + (200,)
GOLD_LINE = GOLD + (230,)

def font(sz):
    return ImageFont.truetype(FONT, sz)

def base():
    img = Image.new("RGB", (W, H))
    d = ImageDraw.Draw(img)
    for y in range(H):
        t = y / (H - 1)
        d.line([(0, y), (W, y)], fill=(int(20 - 6 * t), int(44 - 12 * t), int(33 - 9 * t)))
    d = ImageDraw.Draw(img, "RGBA")
    # 顶部金晕
    for i in range(90):
        a = int(26 * (1 - i / 90))
        d.ellipse([W*0.28 - 300 + i, -160 + i, W*0.28 + 300 - i, 320 - i], outline=(212, 175, 55, a), width=2)
    # 底部山影
    ov = Image.new("RGBA", (W, H), (0, 0, 0, 0))
    od = ImageDraw.Draw(ov)
    od.polygon([(0, 760), (260, 640), (520, 740), (820, 620), (1080, 730), (1200, 680), (1200, 900), (0, 900)],
               fill=(16, 36, 26, 220))
    img.paste(ov, (0, 0), ov)
    d = ImageDraw.Draw(img, "RGBA")
    # 细金内框
    d.rectangle([26, 26, W - 26, H - 26], outline=GOLD + (120,), width=2)
    return img

def stamp(img, ch, cx, cy, size=86):
    d = ImageDraw.Draw(img, "RGBA")
    d.rounded_rectangle([cx - size//2, cy - size//2, cx + size//2, cy + size//2], radius=12, fill=CINNABAR + (235,))
    f = font(int(size * 0.58))
    d.text((cx, cy - size * 0.02), ch, font=f, fill=CREAM + (245,), anchor="mm")

def vtext(img, text, x, y, fs=54, color=CHAMP):
    f = font(fs)
    d = ImageDraw.Draw(img)
    for i, ch in enumerate(text):
        d.text((x, y + i * int(fs * 1.32)), ch, font=f, fill=color)

def leaf_line(img, cx, cy, ln, ang, color=GOLD_LINE):
    ov = Image.new("RGBA", (ln * 3, ln * 3), (0, 0, 0, 0))
    d = ImageDraw.Draw(ov)
    c = ln * 1.5
    d.arc([c - ln, c - ln * 0.9, c + ln, c + ln * 0.9], start=250, end=290, fill=color, width=3)
    d.arc([c - ln * 0.6, c - ln * 0.55, c + ln * 0.6, c + ln * 0.55], start=255, end=285, fill=CHAMP + (150,), width=2)
    ov = ov.rotate(ang, resample=Image.BICUBIC)
    img.paste(ov, (int(cx - c), int(cy - c)), ov)

def steam(img, x, y, h=170):
    d = ImageDraw.Draw(img, "RGBA")
    for k, dx in enumerate((-16, 2, 18)):
        pts = [(x + dx + math.sin(i / 13 + k * 1.3) * 8 * (i / h), y - i) for i in range(0, h, 6)]
        d.line(pts, fill=CHAMP + (60 - k * 14,), width=2)

def save(img, name):
    img.convert("RGB").save(f"{OUT}\\{name}", "JPEG", quality=88)
    print(name)

# ---------- 1 龙井杯 ----------
img = base(); d = ImageDraw.Draw(img, "RGBA")
cx, cy = W//2, 520
# 碗身：计算轮廓（口宽收窄到底）
half_rim, depth, half_foot = 178, 210, 82
pts_r, pts_l = [], []
for i in range(0, 41):
    t = i / 40
    halfw = half_rim - (half_rim - half_foot) * (t ** 1.5)
    y = cy - 40 + depth * t
    pts_l.append((cx - halfw, y))
    pts_r.append((cx + halfw, y))
d.ellipse([cx - half_rim, cy - 86, cx + half_rim, cy + 6], outline=GOLD_LINE, width=4)        # 口沿
d.ellipse([cx - half_rim + 14, cy - 74, cx + half_rim - 14, cy - 6], fill=(196, 214, 160, 96))# 茶汤
d.line(pts_l, fill=GOLD_LINE, width=4)
d.line(pts_r, fill=GOLD_LINE, width=4)
d.arc([cx - half_foot, cy - 70 + depth - 30, cx + half_foot, cy - 70 + depth + 58],
      start=20, end=160, fill=GOLD_LINE, width=4)                                              # 碗底
d.ellipse([cx - 250, cy + 205, cx + 250, cy + 272], outline=CHAMP + (170,), width=3)          # 托盘
steam(img, cx, cy - 130, 210)
for i, (lx, ly, an) in enumerate(((cx - 210, 300, 18), (cx + 30, 268, -12), (cx + 220, 316, 4))):
    leaf_line(img, lx, ly, 60, an)
stamp(img, "茶", 118, 130)
vtext(img, "明前龙井", W - 150, 120)
save(img, "art_q1.jpg")

# ---------- 2 茶叶罐 ----------
img = base(); d = ImageDraw.Draw(img, "RGBA")
cx, cy = W//2, 560
d.ellipse([cx-150, cy-250, cx+150, cy-170], outline=GOLD_LINE, width=4)
d.line([(cx-150, cy-210), (cx-150, cy+160)], fill=GOLD_LINE, width=4)
d.line([(cx+150, cy-210), (cx+150, cy+160)], fill=GOLD_LINE, width=4)
d.arc([cx-150, cy+80, cx+150, cy+240], start=180, end=360, fill=GOLD_LINE, width=4)
d.ellipse([cx-56, cy-290, cx+56, cy-240], outline=CHAMP + (190,), width=3)                 # 盖钮
d.rounded_rectangle([cx-64, cy-40, cx+64, cy+60], radius=16, outline=CHAMP + (170,), width=3)
d.text((cx, cy+10), "茶", font=font(52), fill=CHAMP + (230,), anchor="mm")
leaf_line(img, cx+250, 640, 56, -12); leaf_line(img, cx-260, 620, 50, 14)
stamp(img, "罐", 118, 130)
vtext(img, "碧螺春", W-150, 120)
save(img, "art_q2.jpg")

# ---------- 3 盖碗 ----------
img = base(); d = ImageDraw.Draw(img, "RGBA")
cx, cy = W//2, 540
d.arc([cx-90, cy-290, cx+90, cy-170], start=180, end=360, fill=GOLD_LINE, width=4)         # 盖
d.line([(cx-90, cy-230), (cx+90, cy-230)], fill=GOLD_LINE, width=3)
d.ellipse([cx-26, cy-320, cx+26, cy-282], outline=CHAMP + (190,), width=3)                 # 盖钮
d.arc([cx-190, cy-200, cx+190, cy+60], start=0, end=180, fill=GOLD_LINE, width=4)          # 碗口
d.arc([cx-190, cy-120, cx+190, cy+120], start=180, end=360, fill=GOLD_LINE, width=4)       # 碗身
d.arc([cx-250, cy+110, cx+250, cy+230], start=200, end=340, fill=CHAMP + (190,), width=4)  # 托
steam(img, cx, cy-330, 180)
leaf_line(img, cx-250, 330, 54, 18); leaf_line(img, cx+240, 300, 58, -10)
stamp(img, "碗", 118, 130)
vtext(img, "德化盖碗", W-150, 120)
save(img, "art_q3.jpg")

# ---------- 4 紫砂壶 ----------
img = base(); d = ImageDraw.Draw(img, "RGBA")
cx, cy = W//2, 560
d.arc([cx-200, cy-150, cx+200, cy+170], start=0, end=360, fill=GOLD_LINE, width=4)         # 壶身
d.arc([cx-80, cy-230, cx+80, cy-110], start=180, end=360, fill=GOLD_LINE, width=4)         # 壶口
d.ellipse([cx-30, cy-268, cx+30, cy-232], outline=CHAMP + (190,), width=3)
d.arc([cx+150, cy-170, cx+300, cy-10], start=270, end=90, fill=GOLD_LINE, width=4)         # 壶把
d.polygon([(cx-195, cy-60), (cx-300, cy-130), (cx-285, cy-70), (cx-190, cy-10)], outline=GOLD_LINE, width=3)
d.arc([cx-120, cy+90, cx+120, cy+190], start=20, end=160, fill=CHAMP + (150,), width=3)    # 壶足
steam(img, cx+60, cy-250, 150)
stamp(img, "壶", 118, 130)
vtext(img, "西施紫砂", W-150, 120)
save(img, "art_q4.jpg")

# ---------- 5 普洱茶饼 ----------
img = base(); d = ImageDraw.Draw(img, "RGBA")
cx, cy = W//2, 560
d.ellipse([cx-230, cy-220, cx+230, cy+240], outline=GOLD_LINE, width=5)
d.ellipse([cx-196, cy-186, cx+196, cy+206], outline=CHAMP + (120,), width=2)
d.ellipse([cx-70, cy-60, cx+70, cy+80], outline=CHAMP + (200,), width=4)
d.text((cx, cy+10), "普洱", font=font(58), fill=CHAMP + (235,), anchor="mm")
d.arc([cx-150, cy+270, cx+150, cy+340], start=180, end=360, fill=CHAMP + (120,), width=3)  # 饼托
leaf_line(img, cx+270, 330, 60, -8)
stamp(img, "饼", 118, 130)
vtext(img, "勐海陈饼", W-150, 120)
save(img, "art_q5.jpg")

# ---------- 6 白毫银针 ----------
img = base(); d = ImageDraw.Draw(img, "RGBA")
cx = W//2
for i in range(7):
    x = cx - 126 + i * 42
    lean = (i - 3) * 6
    d.line([(x, 720), (x + lean, 420 - abs(i-3)*26)], fill=CHAMP + (210,), width=5)
    d.ellipse([x + lean - 9, 388 - abs(i-3)*26, x + lean + 9, 424 - abs(i-3)*26],
              outline=GOLD_LINE, width=3)
d.rounded_rectangle([cx-170, 726, cx+170, 782], radius=22, outline=GOLD_LINE, width=4)     # 针盒
stamp(img, "白", 118, 130)
vtext(img, "白毫银针", W-150, 120)
save(img, "art_q6.jpg")

# ---------- 7 红茶杯 ----------
img = base(); d = ImageDraw.Draw(img, "RGBA")
cx, cy = W//2, 540
d.ellipse([cx-150, cy-140, cx+150, cy-60], outline=GOLD_LINE, width=4)                     # 杯口
d.ellipse([cx-120, cy-118, cx+120, cy-66], fill=(166, 96, 58, 130))                        # 红茶汤
d.line([(cx-150, cy-100), (cx-110, cy+120)], fill=GOLD_LINE, width=4)
d.line([(cx+150, cy-100), (cx+110, cy+120)], fill=GOLD_LINE, width=4)
d.arc([cx-170, cy+40, cx+170, cy+240], start=180, end=360, fill=GOLD_LINE, width=4)        # 杯底
d.arc([cx-60, cy+150, cx+60, cy+230], start=180, end=360, fill=CHAMP + (170,), width=3)    # 高脚
steam(img, cx, cy-190, 210)
leaf_line(img, cx-240, 330, 56, 12); leaf_line(img, cx+230, 360, 52, -14)
stamp(img, "红", 118, 130)
vtext(img, "祁门香螺", W-150, 120)
save(img, "art_q7.jpg")

# ---------- 8 茶道组 ----------
img = base(); d = ImageDraw.Draw(img, "RGBA")
cx, cy = W//2, 560
d.rounded_rectangle([cx-190, cy-40, cx-40, cy+180], radius=26, outline=GOLD_LINE, width=4) # 茶筒
d.line([(cx-140, cy-20), (cx-140, cy+150)], fill=CHAMP + (120,), width=2)
d.line([(cx-90, cy-20), (cx-90, cy+150)], fill=CHAMP + (120,), width=2)
d.line([(cx+80, cy+170), (cx+120, cy-60)], fill=GOLD_LINE, width=4)                        # 茶夹
d.arc([cx+60, cy-120, cx+160, cy-40], start=270, end=90, fill=GOLD_LINE, width=4)
d.line([(cx+210, cy+170), (cx+230, cy-40)], fill=GOLD_LINE, width=4)                       # 茶针
d.ellipse([cx+222, cy-72, cx+242, cy-52], outline=CHAMP + (190,), width=3)
d.arc([cx-260, cy+210, cx+300, cy+300], start=180, end=360, fill=CHAMP + (140,), width=3)  # 案
leaf_line(img, cx+280, 340, 54, -6)
stamp(img, "器", 118, 130)
vtext(img, "茶道六君子", W-150, 120)
save(img, "art_q8.jpg")

print("金线线稿 8 张完成")
