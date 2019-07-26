package com.hj.tj.gohome;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hj.tj.gohome.entity.Owner;
import com.hj.tj.gohome.entity.SpeedDynamic;
import com.hj.tj.gohome.enums.StatusEnum;
import com.hj.tj.gohome.mapper.OwnerMapper;
import com.hj.tj.gohome.mapper.SpeedDynamicMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author tangj
 * @description
 * @since 2018/11/29 20:32
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OwnerServiceTest {

    @Resource
    private OwnerMapper ownerMapper;

    @Test
    public void testInsert(){

        Owner owner = new Owner();
        owner.setAvatarUrl("fdasfds");
        owner.setCity("guangzhou");
        owner.setGender(2);
        owner.setOpenId("23532532");
        owner.setPhone("fdafd");
        owner.setWxNickname("fdafdsa");

        ownerMapper.insert(owner);

        System.out.println(owner.getId());
    }

    @Test
    public void testSelect(){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("open_id", "oZvH64kTWTs_fvz-eJy0LJPFlP4E");
        Owner owner = ownerMapper.selectOne(queryWrapper);

        System.out.println(owner);
    }

    @Test
    public void testUpdate(){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("wx_account", "Tackey-takki");

        Owner owner = new Owner();
        owner.setStatus(StatusEnum.DELETED.getStatus());

        ownerMapper.update(owner, queryWrapper);
    }

    @Test
    public void testUpdateById(){
        Owner owner = new Owner();
        owner.setId(118);
        owner.setWxAccount("fdsafggd");

        ownerMapper.updateById(owner);
    }
}
