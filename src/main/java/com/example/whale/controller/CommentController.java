package com.example.whale.controller;

import com.example.whale.dto.ResponseDTO;
import com.example.whale.dto.comment.CreateCommentDTO.CreateCommentRequestDTO;
import com.example.whale.dto.comment.CreateCommentDTO.CreateCommentResponseDTO;
import com.example.whale.dto.comment.GetCommentResponseDTO;
import com.example.whale.dto.comment.GetOneCommentDTO;
import com.example.whale.dto.comment.UpdateCommentRequestDTO;
import com.example.whale.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/comments")
public class CommentController extends BaseController {

    private final CommentService commentService;

    @PostMapping(value = "/save")
    public ResponseDTO<CreateCommentResponseDTO> saveComment(@RequestBody CreateCommentRequestDTO dto) {
        return ResponseDTO.ok(commentService.saveComment(dto));
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
