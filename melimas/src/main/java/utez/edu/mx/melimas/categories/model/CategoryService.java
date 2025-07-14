package utez.edu.mx.melimas.categories.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.melimas.utils.Message;
import utez.edu.mx.melimas.utils.TypesResponse;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<Message> create(CategoryDTO dto) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            return new ResponseEntity<>(new Message("Category name is required", null, TypesResponse.WARNING), HttpStatus.BAD_REQUEST);
        }

        CategoryEntity category = new CategoryEntity();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setEnabled(true);

        category = categoryRepository.saveAndFlush(category);
        if (category == null) {
            log.warn("Category not saved");
            return new ResponseEntity<>(new Message("Category could not be created", null, TypesResponse.ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.info("Category created successfully: {}", category.getName());
        return new ResponseEntity<>(new Message("Category created successfully", category, TypesResponse.SUCCESS), HttpStatus.OK);
    }

    public ResponseEntity<Message> findAll() {
        List<CategoryEntity> categories = categoryRepository.findAll();
        log.info("Categorias Encontradas {} ", categories.size());
        return ResponseEntity.ok(new Message("All categories fetched", categories, TypesResponse.SUCCESS));
    }

    public ResponseEntity<Message> findEnabled() {
        List<CategoryEntity> categories = categoryRepository.findAll()
                .stream()
                .filter(CategoryEntity::isEnabled)
                .toList();
        log.info("Categorias Activas encontradas: {} ", categories.size());
        return ResponseEntity.ok(new Message("Enabled categories fetched", categories, TypesResponse.SUCCESS));
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<Message> update(Long id, CategoryDTO dto) {
        Optional<CategoryEntity> optional = categoryRepository.findById(id);
        if (optional.isEmpty()) {
            log.warn("Category with ID {} not found", id);
            return new ResponseEntity<>(new Message("Category not found", null, TypesResponse.WARNING), HttpStatus.NOT_FOUND);
        }
        CategoryEntity category = optional.get();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        categoryRepository.saveAndFlush(category);

        log.info("Category with ID {} updated", id);
        return new ResponseEntity<>(new Message("Category updated", category, TypesResponse.SUCCESS), HttpStatus.OK);
    }

    @Transactional(rollbackFor = SQLException.class)
    public ResponseEntity<Message> toggleStatus(Long id) {
        Optional<CategoryEntity> optional = categoryRepository.findById(id);
        if (optional.isEmpty()) {
            log.warn("Category with ID {} not found for status change", id);
            return new ResponseEntity<>(new Message("Category not found", null, TypesResponse.WARNING), HttpStatus.NOT_FOUND);
        }

        CategoryEntity category = optional.get();
        category.setEnabled(!category.isEnabled());
        categoryRepository.saveAndFlush(category);

        log.info("Category ID {} status changed to {}", id, category.isEnabled() ? "ENABLED" : "DISABLED");
        return new ResponseEntity<>(new Message("Category status updated", category, TypesResponse.SUCCESS), HttpStatus.OK);
    }

}
