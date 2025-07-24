package com.makersacademy.acebook.model;

import com.makersacademy.acebook.controller.PostsController;
import com.makersacademy.acebook.repository.PostRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Nested
@WebMvcTest(controllers = PostsController.class)
@AutoConfigureMockMvc(addFilters = false)
class PostsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostRepository repository;

    @Test
    void index_returnsPostsViewWithModel() throws Exception {
        User user = new User("Chris", "123");
        Post p1 = new Post("p1", user);
        Post p2 = new Post("p2", user);
        List<Post> posts = List.of(p2, p1);
        Sort sort = Sort.by(DESC, "id");

        when(repository.findAll(sort)).thenReturn(posts);

        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts/index"))
                .andExpect(model().attribute("posts", posts))
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attribute("post", instanceOf(Post.class)));

        verify(repository).findAll(sort);
    }

    @Test
    void index_usesDescendingSortOnId() throws Exception {
        when(repository.findAll(ArgumentMatchers.any(Sort.class))).thenReturn(List.of());

        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk());

        ArgumentCaptor<Sort> sortCaptor = ArgumentCaptor.forClass(Sort.class);
        verify(repository).findAll(sortCaptor.capture());

        Sort captured = sortCaptor.getValue();
        assertThat(captured).isNotNull();
        assertThat(captured.getOrderFor("id")).isNotNull();
        assertThat(captured.getOrderFor("id").getDirection()).isEqualTo(DESC);
    }
}