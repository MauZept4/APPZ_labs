package adamchukpr.touragency.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@RequiredArgsConstructor
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@Entity(name = "post")
public class Post {
    @Id
    private Long id;
    private String positionName;
    private String description;
    private double salary;
    @OneToMany(mappedBy = "post")
    @JsonIgnore
    @ToString.Exclude
    private List<Employee> employees;
}
