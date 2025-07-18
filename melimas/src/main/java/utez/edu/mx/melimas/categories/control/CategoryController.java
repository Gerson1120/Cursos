package utez.edu.mx.melimas.categories.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import utez.edu.mx.melimas.categories.model.CategoryDTO;
import utez.edu.mx.melimas.categories.model.CategoryService;
import utez.edu.mx.melimas.utils.Message;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<Message> saveCategory(@RequestBody CategoryDTO dto) {
        return categoryService.create(dto);
    }

    @GetMapping("/")
    public ResponseEntity<Message> findAll() {
        return categoryService.findAll();
    }

    @GetMapping("/active/")
    public ResponseEntity<Message> findAllActive() {
        return categoryService.findEnabled();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<Message> update(@PathVariable Long id, @RequestBody CategoryDTO dto) {
        return categoryService.update(id, dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Message> delete(@PathVariable Long id) {
        return categoryService.toggleStatus(id);
    }
}
