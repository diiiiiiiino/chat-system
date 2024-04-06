package com.dino.chat.api.invite.command.domain;

import com.dino.chat.api.common.entity.BaseEntity;
import com.dino.chat.api.member.command.domain.Mobile;
import com.dino.chat.api.member.command.domain.converter.MobileConverter;
import com.dino.chat.api.util.VerifyUtil;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.dino.chat.api.common.enumeration.TextLengthRange.MEMBER_INVITE_CODE;

/**
 * <p>초대코드 엔티티</p>
 * <p>모든 메서드와 생성자에서 아래와 같은 경우 {@code CustomIllegalArgumentException}를 발생한다.</p>
 * {@code code}가 {@code null}이거나 문자가 없을 경우, 길이가 6이 아닌 경우 <br>
 * <p>모든 메서드와 생성자에서 아래와 같은 경우 {@code CustomNullPointerException}를 발생한다.</p>
 * {@code houseHold}가 {@code null}인 경우 <br>
 * {@code mobile}이 {@code null}인 경우 <br>
 * {@code expireDateTime}가 {@code null}인 경우
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInvite extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Convert(converter = MobileConverter.class)
    private Mobile mobile;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private LocalDateTime expireDateTime;

    /**
     * @param mobile 전화번호 객체
     * @param code 회원 초대코드
     * @param expireDateTime 초대코드 만료시간
     */
    private MemberInvite(Mobile mobile, String code, LocalDateTime expireDateTime) {
        setMobile(mobile);
        setCode(code);
        setExpireDate(expireDateTime);
    }

    /**
     * @param mobile 전화번호 객체
     * @param code 회원 초대코드
     * @param expireDateTime 초대코드 만료시간
     */
    public static MemberInvite of(Mobile mobile, String code, LocalDateTime expireDateTime){
        return new MemberInvite(mobile, code, expireDateTime);
    }

    /**
     * @param expireDateTime 초대코드 만료시간
     * @return 초대코드 만료 여부
     */
    public boolean isExpired(LocalDateTime expireDateTime){
        return expireDateTime.isAfter(this.expireDateTime);
    }

    /**
     * @param mobile 전화번호 객체
     */
    private void setMobile(Mobile mobile) {
        this.mobile = VerifyUtil.verifyNull(mobile, "mobile");
    }

    /**
     * @param code 회원 초대코드
     */
    private void setCode(String code) {
        this.code = VerifyUtil.verifyTextLength(code, "memberInviteCode", MEMBER_INVITE_CODE.getMin(), MEMBER_INVITE_CODE.getMax());
    }

    /**
     * @param expireDateTime 초대코드 만료시간
     */
    private void setExpireDate(LocalDateTime expireDateTime) {
        this.expireDateTime = VerifyUtil.verifyNull(expireDateTime, "expireDateTime");
    }
}
