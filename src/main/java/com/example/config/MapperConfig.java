package com.example.config;

import com.example.dto.CategoryDto;
import com.example.dto.ProductDto;
import com.example.entity.Category;
import com.example.entity.Product;
import com.example.entity.ProductImage;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        // 1) Chuyển Strategy sang STRICT: tắt hẳn fuzzy matching
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);

        // 2) Tạo TypeMap riêng giữa CategoryDto → Category
        TypeMap<CategoryDto, Category> typeMap = mapper.createTypeMap(CategoryDto.class, Category.class);

        // 3) Chỉ định rõ ràng: map dto.getId() → entity.setId(...)
        typeMap.addMapping(CategoryDto::getId, Category::setId);

        // 4) Map dto.getName() → entity.setName(...)
        typeMap.addMapping(CategoryDto::getName, Category::setName);

        // 5) Skip hoàn toàn việc map tới setter setParent(...) của Category
        typeMap.addMappings(map -> map.skip(Category::setParent));


        // ----- THÊM vào: chiều Entity → DTO -----
        TypeMap<Category, CategoryDto> entityToDto =
                mapper.createTypeMap(Category.class, CategoryDto.class);

        // Map entity.getId() → dto.setId(...)
        entityToDto.addMapping(Category::getId, CategoryDto::setId);

        // Map entity.getName() → dto.setName(...)
        entityToDto.addMapping(Category::getName, CategoryDto::setName);

        // Map entity.getParent().getId() → dto.setCategoryParentId(...)
        entityToDto.addMapping(src -> {
            Category p = src.getParent();
            return (p != null ? p.getId() : null);
        }, CategoryDto::setCategoryParentId);

        // Map entity.getParent().getName() → dto.setCategoryParentName(...)
        entityToDto.addMapping(src -> {
            Category p = src.getParent();
            return (p != null ? p.getName() : null);
        }, CategoryDto::setCategoryParentName);

        TypeMap<Product, ProductDto> productToDto = mapper.createTypeMap(Product.class, ProductDto.class);

        productToDto.addMapping(
                src -> {
                    ProductImage image = src.getImage();
                    return (image != null ? image.getUrl() : null);
                },
                ProductDto::setUrl
        );

        // cấu hình nếu cần custom (nested, date format…)
        return mapper;
    }
}
