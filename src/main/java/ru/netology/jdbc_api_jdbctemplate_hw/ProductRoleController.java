package ru.netology.jdbc_api_jdbctemplate_hw;

import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductRoleController {

    private final ProductRepository productRepository;

    @Autowired
    public ProductRoleController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/products/read-only")
    @Secured("ROLE_READ")
    public List<String> getProductsForReadRole() {
        return productRepository.getAllProductNames();
    }

    @GetMapping("/products/write-only")
    @RolesAllowed("ROLE_WRITE")
    public List<String> getProductsForWriteRole() {
        return productRepository.getAllProductNames();
    }

    @GetMapping("/products/write-or-delete")
    @PreAuthorize("hasRole('WRITE') or hasRole('DELETE')")
    public List<String> getProductsForWriteOrDeleteRole() {
        return productRepository.getAllProductNames();
    }

    @GetMapping("/products/username-check")
    @PreAuthorize("#username == authentication.principal.username")
    public List<String> getProductsIfUsernameMatches(@RequestParam String username) {
        return productRepository.getAllProductNames();
    }
}