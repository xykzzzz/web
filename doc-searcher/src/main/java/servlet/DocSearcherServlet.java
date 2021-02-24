package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Result;
import model.Weight;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.nlpcn.commons.lang.finger.SimHashService;
import searcher.Index;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@WebServlet(value = "/search",loadOnStartup = 0)
public class DocSearcherServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        Index.buildForwardIndex();
        Index.buildInvertedIndex();
        System.out.println("构建成功");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json");//前端ajax请求 响应格式为json
        //构造给返回给前端内容：使用对像，之后在序列化json为字符串
        Map<String,Object> map=new HashMap<>();
        //解析请求数据
        String query=req.getParameter("query");
        List<Result> results=new ArrayList<>();
        try {
            //根据搜索内容处理业务
            if (query == null && query.trim().length() == 0) {
                map.put("ok", false);
                map.put("msg", "搜索内容为空");
            } else {
                //1.根据搜索内容进行分词，遍历每个分词
                for (Term t : ToAnalysis.parse(query).getTerms()) {
                    String fenCi = t.getName();
                    //如果没有意义的分词就跳过
                    //TODO 定义一个数组，包含没有数组的关键词 if(isValid(fenCi)) continue
                    //2.每个分词在倒排中查找对应文档(一个分词对应多个文档)
                    List<Weight> weights = Index.get(fenCi);
                    for (Weight w : weights) {
                        //转化weight为result
                        Result r = new Result();
                        r.setId(w.getDoc().getId());
                        r.setTitle(w.getDoc().getTitle());
                        r.setWeight(w.getWeight());
                        r.setUrl(w.getDoc().getUrl());
                        //内容超过60长度就隐藏为...
                        String content = w.getDoc().getContent();
                        r.setDesc(content.length() <= 60 ? content : content.substring(0, 60) + "...");
                        //TODO 暂时不做合并
                        results.add(r);
                    }
                }
            }
            //4，合并完成后，对List<result> 中进行排序根据权重
            results.sort(new Comparator<Result>() {
                @Override
                public int compare(Result o1, Result o2) {
                    return Integer.compare(o2.getWeight(), o1.getWeight());
                }
            });

            map.put("ok", true);
            map.put("data", results);
        }catch (Exception e){
            e.printStackTrace();
            map.put("ok",false);
            map.put("msg","未知错误");
        }
        PrintWriter pw = resp.getWriter();
        pw.println(new ObjectMapper().writeValueAsString(map));
        pw.flush();
        pw.close();
    }
}
