package greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table COMMENT.
 */
public class Comment {

    private Long comment_id;
    private String text;
    private String created_at;
    private Long consumer_id;
    private String first_name;
    private String last_name;
    private String profile_image;
    private Long deal_id;
    private String title;

    public Comment() {
    }

    public Comment(Long comment_id) {
        this.comment_id = comment_id;
    }

    public Comment(Long comment_id, String text, String created_at, Long consumer_id, String first_name, String last_name, String profile_image, Long deal_id, String title) {
        this.comment_id = comment_id;
        this.text = text;
        this.created_at = created_at;
        this.consumer_id = consumer_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.profile_image = profile_image;
        this.deal_id = deal_id;
        this.title = title;
    }

    public Long getComment_id() {
        return comment_id;
    }

    public void setComment_id(Long comment_id) {
        this.comment_id = comment_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Long getConsumer_id() {
        return consumer_id;
    }

    public void setConsumer_id(Long consumer_id) {
        this.consumer_id = consumer_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public Long getDeal_id() {
        return deal_id;
    }

    public void setDeal_id(Long deal_id) {
        this.deal_id = deal_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
