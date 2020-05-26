package com.beatshadow.mall.member.service.impl;

import com.beatshadow.mall.member.dao.MemberLevelDao;
import com.beatshadow.mall.member.entity.MemberLevelEntity;
import com.beatshadow.mall.member.exception.PhoneExistException;
import com.beatshadow.mall.member.exception.UsernameExistException;
import com.beatshadow.mall.member.vo.MemberLoginVo;
import com.beatshadow.mall.member.vo.MemberRegisterVo;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beatshadow.common.utils.PageUtils;
import com.beatshadow.common.utils.Query;

import com.beatshadow.mall.member.dao.MemberDao;
import com.beatshadow.mall.member.entity.MemberEntity;
import com.beatshadow.mall.member.service.MemberService;


/**
 * @author gnehcgnaw
 */
@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    private MemberLevelDao memberLevelDao ;

    public MemberServiceImpl(MemberLevelDao memberLevelDao) {
        this.memberLevelDao = memberLevelDao;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void register(MemberRegisterVo memberRegisterVo) {

        MemberEntity memberEntity = new MemberEntity() ;
        //默认：会员等级默认
        MemberLevelEntity memberLevelEntity = memberLevelDao.getDefaultLevel();
        memberEntity.setLevelId(memberLevelEntity.getId());
        //检查用户名和手机号的唯一性[异常处理]
        checkPhoneUnique(memberRegisterVo.getPhone());
        checkUsernameUnique(memberRegisterVo.getUserName());
        memberEntity.setMobile(memberRegisterVo.getPhone());
        memberEntity.setUsername(memberRegisterVo.getUserName());
        memberEntity.setNickname(memberRegisterVo.getUserName());

        //password 加密不可逆 ，MD5【信息摘要算法】 & MD5 盐值加密
        //MD5 盐值加密 ： 通过生成随机数与MD5生成字符串进行组合 ，数据库同时存储MD5值与salt值，验证正确性时使用salt进行MD5即可。
        //Spring提供的，自带盐值的MD5加密 BCryptPasswordEncoder
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode(memberRegisterVo.getPassword());
        memberEntity.setPassword(encode);
        baseMapper.insert(memberEntity);
    }

    @Override
    public void checkPhoneUnique(String phone) {
        Integer count = this.baseMapper.selectCount(new QueryWrapper<MemberEntity>().eq("mobile", phone));
        if (count>0){
            throw new PhoneExistException();
        }
    }

    @Override
    public void checkUsernameUnique(String username) {
        Integer count = this.baseMapper.selectCount(new QueryWrapper<MemberEntity>().eq("username", username));
        if (count>0){
            throw new UsernameExistException();
        }

    }

    @Override
    public MemberEntity login(MemberLoginVo memberLoginVo) {
        String loginacct = memberLoginVo.getLoginacct();
        MemberEntity memberEntity = this.baseMapper.selectOne(new QueryWrapper<MemberEntity>().eq("username", loginacct).or().eq("mobile", loginacct));
        if (memberEntity==null){
            //登录失败
            return  null ;
        }else {
            String sqlPassword = memberEntity.getPassword();
            boolean matches = new BCryptPasswordEncoder().matches(memberLoginVo.getPassword(), sqlPassword);
            if (matches){
                return memberEntity ;
            }else {
                return null ;
            }

        }
    }

    @Override
    public MemberEntity login(AuthUser authUser) {
        //社交账户ID
        String uuid = authUser.getUuid();
        //判断是否注册过
        MemberEntity memberEntity = this.baseMapper.selectOne(new QueryWrapper<MemberEntity>().eq("social_uid", uuid));
        //1、注册过，更新令牌和过期时间
        MemberEntity currentMemberEntity = new MemberEntity();

        if (memberEntity!=null){
            currentMemberEntity = memberEntity ;
            currentMemberEntity.setAccessToken(authUser.getToken().getAccessToken());
            currentMemberEntity.setExpireIn(authUser.getToken().getExpireIn());
            int update = this.baseMapper.updateById(currentMemberEntity);
            if (update>0){
                return currentMemberEntity ;
            }else {
                return memberEntity ;
            }
        }else {
            //2、没注册，注册
            currentMemberEntity.setLevelId(memberLevelDao.getDefaultLevel().getId());
            currentMemberEntity.setNickname(authUser.getNickname());
            currentMemberEntity.setSocialUid(authUser.getUuid());
            currentMemberEntity.setAccessToken(authUser.getToken().getAccessToken());
            currentMemberEntity.setExpireIn(authUser.getToken().getExpireIn());
            int insert = this.baseMapper.insert(currentMemberEntity);
            if (insert>0){
                return currentMemberEntity ;
            }else {
                return null ;
            }

        }
    }

}