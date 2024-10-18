package org.test.teamproject_back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.test.teamproject_back.dto.request.ReqAddProductDto;
import org.test.teamproject_back.dto.request.ReqModifyProductDto;
import org.test.teamproject_back.dto.response.RespSearchProductDto;
import org.test.teamproject_back.entity.Category;
import org.test.teamproject_back.entity.Product;
import org.test.teamproject_back.entity.ProductCategory;
import org.test.teamproject_back.exception.InvalidInputException;
import org.test.teamproject_back.repository.ProductMapper;
import org.test.teamproject_back.security.principal.PrincipalUser;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Value("${user.profile.thumbnailImg.default}")
    private String defaultThumbnailImg;

    @Transactional(rollbackFor = SQLException.class)
    public void addProduct(ReqAddProductDto dto) {
        Product product = null;

        if (dto.getThumbnailImg().isBlank() || dto.getThumbnailImg() == null) {
            product = dto.toProduct(defaultThumbnailImg);
            productMapper.addProduct(product);
        } else {
            product = dto.toProduct(dto.getThumbnailImg());
            productMapper.addProduct(product);
        }

        ProductCategory productCategory = ProductCategory.builder()
                .productId(product.getProductId())
                .categoryId(dto.getCategory())
                .build();
        productMapper.addProductCategory(productCategory);
    }

    public RespSearchProductDto getAllProducts() {
        List<Product> productList = productMapper.getAllProductsList();

        if (productList == null || productList.isEmpty()) {
            throw new InvalidInputException("해당 상품 정보가 존재하지 않습니다.");
        }

        return RespSearchProductDto.builder()
                .products(productList)
                .build();
    }

    public RespSearchProductDto searchProducts(String title) {

        List<Product> response = productMapper.findProductByTitle(title.trim());

        if (response == null || response.isEmpty()) {
            throw new InvalidInputException("해당 상품 정보가 존재하지 않습니다.");
        }

        return RespSearchProductDto.builder()
                .products(response)
                .build();
    }

    @Transactional(rollbackFor = SQLException.class)
    public void deleteProduct(Long id) {
        if (!(Optional.ofNullable(productMapper.findProductById(id))).isPresent()) {
            throw new InvalidInputException("해당 상품 정보가 존재하지 않습니다.");
        }
        productMapper.deleteProductById(id);
        productMapper.deleteProductCategoryById(id);
    }

    @Transactional(rollbackFor = SQLException.class)
    public void modifyProduct(ReqModifyProductDto dto) {
        if (!(Optional.ofNullable(productMapper.findProductById(dto.getProductId()))).isPresent()) {
            throw new InvalidInputException("해당 상품 정보가 존재하지 않습니다.");
        }
        productMapper.updateProduct(dto.toProduct());
        productMapper.updateProductCategory(dto.getCategoryId());
    }

}
