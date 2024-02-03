package com.example.whale.domain.comment.controller;

import com.example.whale.domain.common.controller.BaseController;
import com.example.whale.global.util.AuthenticationUtil;
import com.example.whale.domain.common.dto.ResponseDTO;
import com.example.whale.domain.comment.dto.CreateCommentDTO.CreateCommentRequestDTO;
import com.example.whale.domain.comment.dto.CreateCommentDTO.CreateCommentResponseDTO;
import com.example.whale.domain.comment.dto.GetCommentResponseDTO;
import com.example.whale.domain.comment.dto.GetOneCommentDTO;
import com.example.whale.domain.comment.dto.UpdateCommentRequestDTO;
import com.example.whale.domain.user.model.AuthenticationUser;
import com.example.whale.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/comments")
public class CommentController extends BaseController {

    private final CommentService commentService;

    @PostMapping(value = "/save")
    public ResponseDTO<CreateCommentResponseDTO> saveComment(@RequestBody CreateCommentRequestDTO dto,
                                                             Authentication authentication) {
        AuthenticationUser principal = AuthenticationUtil.convertAuthentication(authentication);
        return ResponseDTO.ok(commentService.saveComment(principal.getId(), dto));
    }

    @GetMapping(value = "/find/{commentId}")
    public ResponseDTO<GetCommentResponseDTO> findCommentById(@PathVariable(value = "commentId") Long commentId) {
        return ResponseDTO.ok(commentService.findCommentById(commentId));
    }

    @GetMapping(value = "/findOne/{commentId}")
    public ResponseDTO<GetOneCommentDTO> findOneCommentById(@PathVariable(value = "commentId") Long commentId) {
        return ResponseDTO.ok(commentService.findOneCommentById(commentId));
    }

    @PatchMapping(value = "/update")
    public ResponseDTO<GetOneCommentDTO> updateComment(@RequestBody UpdateCommentRequestDTO dto) {
        return ResponseDTO.ok(commentService.updateComment(dto));
    }

    @DeleteMapping(value = "/delete/{commentId}")
    public ResponseDTO<Void> deleteCommentById(@PathVariable(value = "commentId") Long commentId) {
        commentService.deleteCommentById(commentId);
        return ResponseDTO.ok();
    }

}
