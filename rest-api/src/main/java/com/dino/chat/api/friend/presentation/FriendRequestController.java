package com.dino.chat.api.friend.presentation;

import com.dino.chat.api.common.http.response.DataResponse;
import com.dino.chat.api.common.http.response.Response;
import com.dino.chat.api.friend.command.application.service.FriendRequestService;
import com.dino.chat.api.friend.query.FriendRequestDto;
import com.dino.chat.api.friend.query.FriendRequestQueryService;
import com.dino.chat.api.member.command.application.security.SecurityMember;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friend-request")
public class FriendRequestController {

    private FriendRequestService friendRequestService;
    private FriendRequestQueryService friendRequestQueryService;

    @PostMapping("/{friend-id}")
    public Response requestFriend(@AuthenticationPrincipal SecurityMember securityMember,
                                  @PathVariable("friend-id") Long friendId){
        friendRequestService.request(securityMember.getMember(), friendId);

        return Response.ok();
    }

    @GetMapping
    public DataResponse<List<FriendRequestDto>> findAllFriendRequest(@AuthenticationPrincipal SecurityMember securityMember){
        return DataResponse.ok(friendRequestQueryService.findAllByMemberId(securityMember.getMemberId()));
    }

    @PostMapping("/accept/{id}")
    public Response acceptRequest(@AuthenticationPrincipal SecurityMember securityMember,
                                  @PathVariable Long id){
        friendRequestService.accept(securityMember.getMember(), id);

        return Response.ok();
    }

    @PostMapping("/reject/{id}")
    public Response rejectRequest(@PathVariable Long id){
        friendRequestService.reject(id);

        return Response.ok();
    }
}
