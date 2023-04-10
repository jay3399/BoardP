package Jay.BoardP.domain;


import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    private String code;

    @OneToMany(mappedBy = "category" , cascade = CascadeType.ALL)
    private List<Board> boards = new ArrayList<>();

    private void setCategory(String code) {
        this.code = code;
    }

    public static Category createCategory(String code) {
        Category category = new Category();
        category.setCategory(code);
        return category;
    }




}
