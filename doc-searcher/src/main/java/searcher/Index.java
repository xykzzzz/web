package searcher;

import model.DocInfo;
import model.Weight;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 构建索引:
 * 正排索引:从本地文件数据中读取到java内存
 * 倒排索引:构建Map<String List<信息>>
 *     Map建:关键词(分词来做)
 *    Map值信息:docInfo对象引用或是docInfo的id,
 *    权重(正文关键词数量*1+标题对应关键词数量*10) 1和10自己任意取
 */
public class Index {
    //正排索引,下标对应docId
    private static final List<DocInfo> FORWARD_INDEX=new ArrayList<>();
    //倒排索引
    private static final Map<String,List<Weight>> INVERTED_INDEX=new HashMap<>();
/**
 * 构建正排索引，从本地raw_data.txt读取并保存
 */
    public static void buildForwardIndex(){
        try {
            FileReader fr=new FileReader(Parser.RAW_DATA);
            BufferedReader br=new BufferedReader(fr);
            int id=0;//行号设置为docInfo的Id
            String line;
            while ((line=br.readLine())!=null){
                if(line.trim().equals("")){
                    continue;
                }
                //一行对应一个docInfo对象，类似数据库一行对应一个java对象
                DocInfo doc=new DocInfo();
                doc.setId(++id);
                String[]parts=line.split("\3");//每一行按\3间隔符切割
                doc.setTitle(parts[0]);
                doc.setUrl(parts[1]);
                doc.setContent(parts[2]);
                FORWARD_INDEX.add(doc);
            }
        } catch (IOException e) {
            //不要吃异常,初始化操作异常让线程捕获，从而结束程序
            //初始化（启动tomcat）有问题，尽早暴露
            throw new RuntimeException(e);
        }
    }
    /**
     * 构建倒排索引：从java内存中正排索引获取信息来构建
     */
    public static void buildInvertedIndex(){
        for (DocInfo doc:FORWARD_INDEX){
            //一个doc，分别对标题和正文分词，每一个分词生成一个weight对象，需要计算权重
            //实现逻辑构建Map结构，用来保存分词（键）和weight（值）对象
            Map<String,Weight> cache=new HashMap<>();
            List<Term> titleFeiCis= ToAnalysis.parse(doc.getTitle()).getTerms();
            for (Term titleFeiCi:titleFeiCis){//标题分词并遍历处理
                Weight w=cache.get(titleFeiCi.getName());//获取标题分词键对应的weight对象
                if(w==null){
                    w=new Weight();
                    w.setDoc(doc);
                    w.setKeyWord(titleFeiCi.getName());
                    cache.put(titleFeiCi.getName(),w);
                }
                //标题分词 权重加10
                w.setWeight(w.getWeight()+10);
            }
            List<Term> contentFenCis=ToAnalysis.parse(doc.getContent()).getTerms();
            for (Term contentFenCi:contentFenCis) {
                Weight w=cache.get(contentFenCi.getName());
                if(w==null){
                    w=new Weight();
                    w.setDoc(doc);
                    w.setKeyWord(contentFenCi.getName());
                    cache.put(contentFenCi.getName(),w);
                }
                w.setWeight(w.getWeight()+1);
            }
            //把临时保存的map数据（k-w）全部保存到倒排索引中
            for (Map.Entry<String,Weight> e:cache.entrySet()) {
                String keyWord=e.getKey();
                Weight w = e.getValue();
                //更新到倒排索引Map<String,List<Weight>> 多个文档同一个关键词保存在一个list中
                List<Weight> weights=INVERTED_INDEX.get(keyWord);
                if(weights==null){
                    weights=new ArrayList<>();
                    INVERTED_INDEX.put(keyWord,weights);
                }
               // System.out.println(keyWord+": ("+w.getDoc().getId());
                weights.add(w);
            }
        }
    }
    public static List<Weight> get(String keyWord){
        return INVERTED_INDEX.get(keyWord);
    }
    public static void main(String[] args) {
        Index.buildForwardIndex();
  //      FORWARD_INDEX.stream().forEach(System.out::println);
        Index.buildInvertedIndex();
        for (Map.Entry<String,List<Weight>> e:INVERTED_INDEX.entrySet()){
            String keyWord=e.getKey();
            System.out.print(keyWord+": ");
            List<Weight> weights=e.getValue();
            weights.stream().
                    map(w->{
                        return "（"+w.getDoc().getId()+", "+w.getWeight()+"）";
                    }).forEach(System.out::print);
            System.out.println();
        }
    }
}
/*public class Index{
    //建立正排索引
    private static final List<DocInfo> FORWARD_INDEX=new ArrayList<>();
    private static final Map<String,List<Weight>> INVERTED_INDEX=new HashMap<>();
    public static void buildForwardIndex(){
        try {
            FileReader fileReader=new FileReader(Parser.RAW_DATA);
            BufferedReader bufferedReader=new BufferedReader(fileReader);
            int id=0;
            String line;
            while ((line=bufferedReader.readLine())!=null){
                if(line.trim().equals("")){
                    continue;
                }
                String[] parts = line.split("\3");
                DocInfo doc=new DocInfo();
                doc.setId(++id);
                doc.setTitle(parts[0]);
                doc.setUrl(parts[1]);
                doc.setContent(parts[2]);
                FORWARD_INDEX.add(doc);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void buildInvertedIndex(){
        for (DocInfo doc:FORWARD_INDEX) {
            Map<String,Weight> cache=new HashMap<>();
            List<Term> titleFeiCis = ToAnalysis.parse(doc.getTitle()).getTerms();
            for (Term titleFeiCi:titleFeiCis) {
                Weight weight = cache.get(titleFeiCi.getName());
                if(weight==null){
                    weight=new Weight();
                    weight.setDoc(doc);
                    weight.setKeyWord(titleFeiCi.getName());
                    cache.put(titleFeiCi.getName(),weight);
                }
                weight.setWeight(weight.getWeight()+10);
            }
            List<Term> contentFeiCis=ToAnalysis.parse(doc.getContent()).getTerms();
            for (Term contentFeiCi:contentFeiCis) {
                Weight weight = cache.get(contentFeiCi.getName());
                if(weight==null){
                    weight=new Weight();
                    weight.setDoc(doc);
                    weight.setKeyWord(contentFeiCi.getName());
                }
                weight.setWeight(weight.getWeight()+1);
            }
            for (Map.Entry<String,Weight> e:cache.entrySet()) {
                String keyWord = e.getKey();
                Weight w = e.getValue();
                List<Weight> weights=INVERTED_INDEX.get(keyWord);
                if(weights==null){
                    weights=new ArrayList<>();
                    INVERTED_INDEX.put(keyWord,weights);
                }
                weights.add(w);
            }
        }
    }

    public static void main(String[] args) {
        Index.buildForwardIndex();
        //FORWARD_INDEX.stream().forEach(System.out::println);
        Index.buildInvertedIndex();
        for (Map.Entry<String,List<Weight>> e:INVERTED_INDEX.entrySet()){
            String keyWord=e.getKey();
            System.out.print(keyWord+": ");
            List<Weight> weights=e.getValue();
            weights.stream().
                    map(w->{
                        return "（"+w.getDoc().getId()+", "+w.getWeight()+"）";
                    }).forEach(System.out::print);
            System.out.println();
        }
    }
}*/
