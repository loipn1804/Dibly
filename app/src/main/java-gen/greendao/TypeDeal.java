package greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table TYPE_DEAL.
 */
public class TypeDeal {

    private Integer id;
    private String text;
    private Integer order;
    private Boolean isSelect;
    private Integer f_deleted;

    public TypeDeal() {
    }

    public TypeDeal(Integer id, String text, Integer order, Boolean isSelect, Integer f_deleted) {
        this.id = id;
        this.text = text;
        this.order = order;
        this.isSelect = isSelect;
        this.f_deleted = f_deleted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Boolean getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(Boolean isSelect) {
        this.isSelect = isSelect;
    }

    public Integer getF_deleted() {
        return f_deleted;
    }

    public void setF_deleted(Integer f_deleted) {
        this.f_deleted = f_deleted;
    }

}
