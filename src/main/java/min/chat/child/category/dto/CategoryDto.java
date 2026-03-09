package min.chat.child.category.dto;

import lombok.Data;
import min.chat.child.category.entity.Category;

@Data
public class CategoryDto {

    private Long id;
    private String email;
    private String name;

    public static CategoryDto from(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.id = category.getId();
        dto.email = category.getAccount().getEmail();
        dto.name = category.getName();
        return dto;
    }
}