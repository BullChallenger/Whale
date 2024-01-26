package com.example.whale;

import com.example.whale.constant.Role;
import com.example.whale.domain.ArticleEntity;
import com.example.whale.domain.CommentEntity;
import com.example.whale.domain.UserEntity;
import com.example.whale.repository.ArticleRepository;
import com.example.whale.repository.CommentRepository;
import com.example.whale.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Transactional
@SpringBootTest
class WhaleApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @Commit
    void dataInsert() {
        for (int i = 200; i < 400; i++) {
            UserEntity user = new UserEntity("test" + i + "@test.com", "user " + i, "testAccount " + i,
                    "1234", Role.USER);
            userRepository.save(user);
            entityManager.flush();

            for (int j = 0; j < 3; j++) {
                ArticleEntity article = new ArticleEntity(user, "Test Article " + i + "_" + j, "This is Article Content");
                articleRepository.save(article);
                entityManager.flush();

                for (int k = 0; k < 3; k++) {
                    CommentEntity comment = new CommentEntity(user, "Wow Root Comment Content!", null, article);
                    commentRepository.save(comment);
                    entityManager.flush();

                    for (int l = 0; l < 3; l++) {
                        CommentEntity child = new CommentEntity(user, "Wow Child Comment Content!", comment, article);
                        commentRepository.save(child);
                        entityManager.flush();
                    }
                }
            }
        }
    }

}
