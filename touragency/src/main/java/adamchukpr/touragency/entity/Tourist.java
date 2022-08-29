package adamchukpr.touragency.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@RequiredArgsConstructor
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@Entity(name = "tourist")
public class Tourist {
    @Id
    private Long id;
    private String fullName;
    private String phone;
    private String email;
    private String address;
}
