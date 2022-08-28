package adamchukpr.touragency.stub;

import adamchukpr.touragency.dto.PostDTO;
import adamchukpr.touragency.entity.Post;


import java.util.ArrayList;

public final class PostStub {

    private static final Long ID = 1L;
    private static final String POSITION_NAME = "Administrator";
    private static final String DESCRIPTION = "Some description for administrator";

    public static Post getRandomPost() {
        return Post.builder()
                   .id(ID)
                   .positionName(POSITION_NAME)
                   .description(DESCRIPTION)
                   .employees(new ArrayList<>())
                   .build();
    }

    public static PostDTO getPostRequest() {
        var dto = new PostDTO();
        dto.setPositionName(POSITION_NAME + " - updated");
        dto.setDescription(DESCRIPTION + " - updated");
        return dto;
    }
}
