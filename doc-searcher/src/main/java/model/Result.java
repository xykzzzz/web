package model;

//这个类是展示搜索结果在网页上
public class Result {
    //前两个合并文档，排序用
    private Integer id;//文档合并标识用通过id合并
    private int weight; //同一个文档合并后，权限相加，在排序
    private String title;//docInfo 标题
    private String url; //docInfo url
    private String desc; //docInfo 描述信息 （截取文章内容作为描述信息）

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Result{" +
                "id=" + id +
                ", weight=" + weight +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
