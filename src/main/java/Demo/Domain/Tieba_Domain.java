package Demo.Domain;

public class Tieba_Domain {
    private int id;

    private String listUrl = null;

    private String title = null;

    private String content = null;

    private String fbsj = null;

    private String comment = null;

    private int commentCount;

    public int getId() {
        return id;
    }

    public String getListUrl() {
        return listUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getFbsj() {
        return fbsj;
    }

    public String getComment() {
        return comment;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setListUrl(String listUrl) {
        this.listUrl = listUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setFbsj(String fbsj) {
        this.fbsj = fbsj;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    @Override
    public String toString() {
        return "Tieba_Domain{" +
                "listUrl='" + listUrl + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}

