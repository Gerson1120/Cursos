package utez.edu.mx.melimas.categories.model;

public class CategoryDTO {
    private Long id;
    private String name;
    private String description;
    private Boolean enabled;

    public CategoryDTO() {
    }

    public CategoryDTO(String name, String description, Boolean enabled) {
        this.name = name;
        this.description = description;
        this.enabled = enabled;
    }

    public CategoryDTO(Long id, String name, String description, Boolean enabled) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.enabled = enabled;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
