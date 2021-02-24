package model;

public class Weight {
    private DocInfo doc;
    private int weight;//权重初始为0 通过标题和正文来计算，关键词的数量
    private String keyWord;//关键词

    public DocInfo getDoc() {
        return doc;
    }

    public int getWeight() {
        return weight;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setDoc(DocInfo doc) {
        this.doc = doc;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    @Override
    public String toString() {
        return "Weight{" +
                "doc=" + doc +
                ", weight=" + weight +
                ", keyWord='" + keyWord + '\'' +
                '}';
    }

}
