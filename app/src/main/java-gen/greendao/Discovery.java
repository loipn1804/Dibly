package greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table DISCOVERY.
 */
public class Discovery {

    private Long id;
    private String name;
    private String imageLink;
    private Integer position;
    private Integer num_deal;

    public Discovery() {
    }

    public Discovery(Long id) {
        this.id = id;
    }

    public Discovery(Long id, String name, String imageLink, Integer position, Integer num_deal) {
        this.id = id;
        this.name = name;
        this.imageLink = imageLink;
        this.position = position;
        this.num_deal = num_deal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getNum_deal() {
        return num_deal;
    }

    public void setNum_deal(Integer num_deal) {
        this.num_deal = num_deal;
    }

}
