package com.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.utils.DeepSeekClient;

/**
 * RAG 检索效果评测（消融对比）。
 *
 * <p>内置茶文化领域问答评测集，对同一组问题分别跑三种检索策略：
 * <pre>
 *   A 单路 BM25          —— 稀疏检索基线
 *   B 双路 RRF           —— BM25 + 稠密向量，倒数排名融合（无重排）
 *   C 双路 + LLM 重排    —— 完整链路
 * </pre>
 * 指标：
 * <ul>
 *   <li>Recall@K —— 期望文档是否出现在前 K 条</li>
 *   <li>MRR       —— 首次命中排名的倒数均值，越接近 1 表示相关结果排得越靠前</li>
 * </ul>
 *
 * <p>评测问题刻意改写措辞（不与知识库标题字面重合），用于检验语义召回能力，
 * 而非字面匹配能力。
 */
@Service
public class RagEvalService {

    @Autowired
    private TeaRagService ragService;

    @Value("${ai.deepseek.api-key:}")
    private String apiKey;

    @Value("${ai.deepseek.base-url:https://api.deepseek.com}")
    private String baseUrl;

    @Value("${ai.deepseek.model:deepseek-chat}")
    private String model;

    /** 评测集：{用户会怎么问, 期望命中的标题片段} */
    private static final String[][] CASES = {
            // ---- 茶文化文章 ----
            {"茶是什么时候开始出现的", "中国茶文化简史"},
            {"茶叶一共分成几大类", "六大茶类图鉴"},
            {"泡绿茶要注意哪些地方", "绿茶冲泡的三个关键"},
            {"用盖碗泡茶的手法怎么练", "功夫茶入门"},
            {"紫砂壶平时要怎么保养", "紫砂壶入门"},
            {"工夫茶里说的关公巡城是什么意思", "潮汕工夫茶俗"},
            {"茶桌怎么摆设才好看", "茶席布置基础"},
            {"有没有写过茶的古诗", "茶联与茶诗"},
            // ---- 线上讲座 ----
            {"绿茶怎么泡才不苦", "绿茶冲泡实战"},
            {"铁观音和单丛有什么区别", "一泡看懂乌龙茶"},
            {"生普和熟普怎么区分", "普洱生熟之辨"},
            {"老白茶怎么判断年份", "白茶工艺与年份茶鉴别"},
            {"红茶可以怎么调着喝", "红茶调饮创意工作坊"},
            {"古人怎么把茶打出泡沫", "宋代点茶"},
            {"带孩子一起学茶艺", "亲子茶艺"},
            {"泡茶到底用盖碗还是紫砂壶", "盖碗与紫砂"},
            // ---- 购物资讯 ----
            {"秋天上市的乌龙茶有什么讲究", "秋茶上市"},
            {"今年新茶什么时候开始卖", "明前龙井限量"},
            {"茶叶包装上的等级标识怎么看", "等级与标准号"},
            {"绿茶平时要放冰箱吗", "要不要放冰箱"},
            {"玻璃杯泡茶的具体步骤", "三投法"},
            {"茶饼怎么弄开才不会碎", "普洱饼怎么撬"},
            {"喝茶对身体到底有什么影响", "茶多酚"},
            {"茶具展上一般能看到什么", "茶博会"},
            // ---- 商品 ----
            {"最好的龙井是哪种", "西湖龙井"},
            {"碧螺春产自哪里", "碧螺春"},
            {"清香型的铁观音", "铁观音清香型"},
            {"岩茶有什么代表品种", "大红袍"},
            {"熟普的茶饼怎么买", "普洱熟茶饼"},
            {"白毫银针属于什么茶", "白毫银针"},
            {"正山小种是红茶吗", "正山小种"},
            {"祁门红茶好在哪里", "祁门红茶"},
            {"黄茶有什么名品", "君山银针"},
            {"鸭屎香是什么茶", "鸭屎香"},
            {"茉莉花茶推荐", "茉莉花茶"},
            {"西施壶是什么器型", "西施壶"},
            {"白瓷盖碗怎么挑", "白瓷盖碗"},
            {"茶道六件套都有什么", "茶道六君子"},
            {"配茶的点心买什么好", "桂花绿豆糕"},
            {"喝茶配什么甜点合适", "凤梨酥"},
            // ---- 领域种子库（六大茶类 / 名茶 / 冲泡） ----
            {"传说里最早发现茶的是谁", "茶史：从神农到茶经"},
            {"盖碗为什么叫三才碗", "盖碗的使用要领"},
            {"紫砂壶泡茶隔夜不馊是什么原理", "紫砂壶的双气孔结构"},
            {"茯砖茶里长的金花是霉吗", "安化黑茶与茯砖金花"},
            {"那种蜜香的乌龙茶名字为什么怪怪的", "鸭屎香单丛"},
            {"被虫咬过的茶叶反而更贵是哪种", "东方美人"},
            {"清明节前买的绿茶为什么那么贵", "明前茶与雨前茶"},
            {"六大茶类是按什么来划分的", "六大茶类的划分依据"},
            {"金骏眉凭什么卖那么贵", "金骏眉"},
            {"武夷山的肉桂和水仙怎么选", "武夷肉桂"},
            {"牛栏坑的岩茶为什么是天价", "武夷山三坑两涧"},
            {"老班章和冰岛到底指什么", "普洱名山：班章、冰岛、易武"},
            {"宋代人喝茶的碗为什么是黑色的", "建盏与兔毫"},
            {"分茶的那个杯子叫什么", "公道杯与均匀出汤"},
            // ---- 领域种子库（健康 / 储存 / 日常） ----
            {"茶多酚对身体真有好处吗", "茶多酚与抗氧化"},
            {"晚上喝茶失眠怎么破", "饮茶与睡眠"},
            {"放了一夜的茶还能喝吗", "隔夜茶能不能喝"},
            {"空腹喝浓茶为什么会心慌", "空腹与浓茶"},
            {"怀孕之后还能不能喝茶", "孕妇与儿童饮茶"},
            {"吃药的时候能拿茶水送下去吗", "服药与饮茶"},
            {"绿茶买回来要不要放冰箱", "绿茶的储存：低温密封"},
            {"白茶想存成老茶应该怎么放", "白茶与普洱的长期存放"},
            {"泡茶用什么水比较好", "水质对茶汤的影响"},
            {"一泡茶放多少茶叶合适", "投茶量的黄金比例"},
            {"老白茶可以直接煮着喝吗", "老白茶与黑茶的煮饮"},
            {"夏天不想喝热茶有什么办法", "冷泡茶的做法"},
            {"别人给我倒茶时敲手指是什么意思", "叩指礼"},
            {"结婚的时候敬茶有什么讲究", "客来敬茶"},
            {"藏族的酥油茶是怎么打出来的", "藏族酥油茶"},
            {"大理的三道茶是哪三道", "白族三道茶"}
    };

    /** 执行一轮完整评测（全部题目） */
    public Map<String, Object> run(int topK) {
        return run(topK, 0);
    }

    /**
     * 执行一轮评测。
     *
     * @param limit 只跑前 N 题（便于快速验证）；<=0 或超过总题数时跑全部
     */
    public Map<String, Object> run(int topK, int limit) {
        String[][] cases = (limit > 0 && limit < CASES.length)
                ? java.util.Arrays.copyOfRange(CASES, 0, limit)
                : CASES;
        DeepSeekClient llm = new DeepSeekClient(baseUrl, apiKey, model);
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("case_count", cases.length);
        out.put("top_k", topK);
        out.put("knowledge_count", ragService.count());

        List<Map<String, Object>> strategies = new ArrayList<>();
        strategies.add(evaluate("A 单路 BM25",
                q -> ragService.searchSparseOnly(q, topK), topK, cases));
        strategies.add(evaluate("B 双路 RRF（BM25 + 向量）",
                q -> ragService.searchHybridNoRerank(q, topK), topK, cases));
        strategies.add(evaluate("C 双路 + LLM 语义重排",
                q -> ragService.search(q, topK, llm), topK, cases));

        out.put("strategies", strategies);

        // 逐题明细：记录 C 策略下的命中情况，便于答辩时展示个例
        List<Map<String, Object>> details = new ArrayList<>();
        for (String[] c : cases) {
            Map<String, Object> d = new LinkedHashMap<>();
            d.put("question", c[0]);
            d.put("expected", c[1]);
            List<Map<String, Object>> hits = ragService.search(c[0], topK, llm);
            List<String> titles = new ArrayList<>();
            int rank = 0;
            for (int i = 0; i < hits.size(); i++) {
                String t = String.valueOf(hits.get(i).get("title"));
                titles.add(t);
                if (rank == 0 && t.contains(c[1])) rank = i + 1;
            }
            d.put("top_titles", titles);
            d.put("rank", rank);
            details.add(d);
        }
        out.put("details", details);
        return out;
    }

    /** 对给定题目集跑一种策略，计算 Recall@K 与 MRR */
    private Map<String, Object> evaluate(String name,
                                         Function<String, List<Map<String, Object>>> retriever,
                                         int topK, String[][] cases) {
        int hit = 0;
        double mrrSum = 0;
        List<Map<String, Object>> misses = new ArrayList<>();
        for (String[] c : cases) {
            List<Map<String, Object>> hits;
            try {
                hits = retriever.apply(c[0]);
            } catch (Exception e) {
                hits = new ArrayList<>();
            }
            int rank = 0;
            for (int i = 0; i < hits.size(); i++) {
                if (String.valueOf(hits.get(i).get("title")).contains(c[1])) {
                    rank = i + 1;
                    break;
                }
            }
            if (rank > 0) {
                hit++;
                mrrSum += 1.0 / rank;
            } else {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("question", c[0]);
                m.put("expected", c[1]);
                misses.add(m);
            }
        }
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("strategy", name);
        r.put("recall_at_k", round(hit / (double) cases.length));
        r.put("mrr", round(mrrSum / cases.length));
        r.put("hit", hit);
        r.put("total", cases.length);
        r.put("miss_count", misses.size());
        r.put("misses", misses.size() > 8 ? misses.subList(0, 8) : misses);
        return r;
    }

    private double round(double v) {
        return Math.round(v * 10000) / 10000.0;
    }
}
