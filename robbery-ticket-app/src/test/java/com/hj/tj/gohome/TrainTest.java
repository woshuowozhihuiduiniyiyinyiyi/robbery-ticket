package com.hj.tj.gohome;

import com.hj.tj.gohome.entity.Station;
import com.hj.tj.gohome.mapper.StationMapper;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ClassUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author tangj
 * @description
 * @since 2019/2/28 18:05
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TrainTest {

    @Resource
    private StationMapper trainMapper;

    @Test
    public void testInsert() {
        InputStream stream = ClassUtils.class.getClassLoader().getResourceAsStream("chezhan.txt");
        try {
            List<String> strs = IOUtils.readLines(stream, "UTF-8");
            StringBuilder stringBuilder = new StringBuilder();
            strs.forEach((str) -> stringBuilder.append(str));

            String[] splitArr = stringBuilder.toString().split("@");
            for (int i = 1; i < splitArr.length; i++) {
                Station train = new Station();
                String[] trainDetail = splitArr[i].split("\\|");
                train.setId(Integer.parseInt(trainDetail[5]));
                train.setName(trainDetail[1]);
                train.setNumber(trainDetail[2]);
                trainMapper.insert(train);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
