package utez.edu.mx.melimas.categories.model;

import jakarta.validation.constraints.Pattern;

public class CategoryDTO {
    private Long id;
    @Pattern(
            regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9 \\-]{3,100}$",
            message = "El nombre debe tener entre 3 y 100 caracteres y solo puede contener letras, números, espacios y guiones."
    )
    private String name;
    @Pattern(
            regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9 ,.\\-()!¡¿?\"']{0,255}$",
            message = "La descripción no debe exceder los 255 caracteres y solo puede contener caracteres válidos."
    )
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
